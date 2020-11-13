package com.ym.provider.task.table;


import com.ym.provider.commons.redis.LockService;
import com.ym.provider.commons.redis.redisson.RedissonLockService;
import com.ym.provider.commons.utils.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private static final String MODIFIER = "public";

    public Task(String taskName, String serviceName, String methodName) {
        this.taskName = taskName;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.lockService = ApplicationContextHolder.getBean(RedissonLockService.class);
    }

    @Override
    public void run() {
        Boolean lock = lockService.lock(taskName);
        if (lock) {
            try {
                log.info("[{}]-->[{}]:[{}]中[{}]方法开始执行", taskName,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd " +
                        "HH:mm:ss")), serviceName, methodName);
                invoke();
                log.info("[{}]-->[{}]:[{}]中[{}]方法执行结束", taskName,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd " +
                        "HH:mm:ss")), serviceName, methodName);
            } catch (Exception e) {
                log.info("[{}]:[{}]中[{}]方法执行异常", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd " +
                        "HH:mm:ss")), serviceName, methodName);
            } finally {
                lockService.unLock(taskName);
            }
        } else {
            log.error("未获取到分布式锁");
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
        return true;
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
