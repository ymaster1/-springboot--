package com.ym.provider.task.table;


import com.ym.provider.commons.redis.LockService;
import com.ym.provider.commons.redis.redisson.RedissonLockService;
import com.ym.provider.commons.utils.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author ymaster1
 * @date 2020/11/11 15:30
 * @description 所有的es同步任务都用这个task, 处理逻辑都一样，只是数据不一样
 */
@Slf4j
public class Task implements Runnable {
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 锁
     */
//    private LockService lockService;
    /**
     * 锁
     */
    private RedissonLockService lockService;
    /**
     * 要执行的方法所在service
     */
    private String serviceName;
    /**
     * 要执行的方法名称
     */
    private String methodName;

    @Override
    public int hashCode() {
        return Objects.hash(taskName, serviceName, methodName);
    }

    /**
     * 需要重写来保证属性相等的对象即是同一对象
     * Object默认的equals只能保证引用相等的对象是同一对象
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
//        如果类型不同返回false
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
//        最后比较属性，只要serviceName，methodName,taskName相同就表示对象相等
        Task that = (Task) o;
        return serviceName.equals(that.serviceName) &&
                methodName.equals(that.methodName) &&
                taskName.equals(that.taskName);
    }

    private static final String MODIFIER = "public final";

    public Task(String taskName, String serviceName, String methodName) {
        this.taskName = taskName;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.lockService = ApplicationContextHolder.getBean(RedissonLockService.class);
    }

    @Override
    public void run() {


        try {

            Boolean lock = lockService.lock(taskName);
            if (lock) {
                log.info("[{}]-->[{}]:[{}]中[{}]方法开始执行", taskName,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd " +
                                "HH:mm:ss")), serviceName, methodName);
                invoke();
                log.info("[{}]-->[{}]:[{}]中[{}]方法执行结束", taskName,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd " +
                                "HH:mm:ss")), serviceName, methodName);
            } else {
                log.error("未获取到分布式锁");
            }
        } catch (Exception e) {
            log.info("[{}]:[{}]中[{}]方法执行异常", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM" +
                    "-dd " +
                    "HH:mm:ss")), serviceName, methodName);
        } finally {
            lockService.unLock(taskName);
        }
    }

    /**
     * 通过反射获取目标方法并执行
     */
    private void invoke() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (!serviceIsExit()) {
            log.error("不存在该service[{}]", serviceName);
            return;
        }
        Object bean = ApplicationContextHolder.getBean(serviceName);
        Method invokeMethod = getMethod(bean);
        log.info(Modifier.toString(invokeMethod.getModifiers()));
//        必须执行public方法
        if (invokeMethod == null || !MODIFIER.equals(Modifier.toString(invokeMethod.getModifiers()))) {
            log.error("不存在该方法[{}]", methodName);
            return;
        }
        invokeMethod.invoke(bean);
    }

    /**
     * @return 该service是否存在
     */
    private boolean serviceIsExit() {
//        获取所有bean
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        List<String> list = Arrays.asList(applicationContext.getBeanDefinitionNames());
        log.info("容器中含有[{}]个bean", list.size());
        return list.contains(serviceName);
    }

    /**
     * 定时任务一般都是执行无参的方法
     *
     * @param bean
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethod(Object bean) throws NoSuchMethodException {
        return bean.getClass().getDeclaredMethod(methodName, null);
    }
}
