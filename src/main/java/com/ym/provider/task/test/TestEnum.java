package com.ym.provider.task.test;


import com.ym.provider.task.config.ITask;
import com.ym.provider.task.config.ITaskEnum;

/**
 * @author ymaster1
 * @date 2020/8/14 14:03
 * 任务直接添加就行
 */
public enum TestEnum implements ITaskEnum {
    /**
     * 测试
     */
    TEST(TestEnum.TEST_TASK, "0 0/1 * * * ?", "打印ymaster1 字符串，每隔一分钟执行", TestTask.class);

    public static final String TEST_TASK = "test-task";


    private String desc;
    private String code;
    private String corn;
    private Class<? extends ITask> clazz;

    TestEnum(String code, String corn, String desc, Class<? extends ITask> clazz) {
        this.code = code;
        this.corn = corn;
        this.desc = desc;
        this.clazz = clazz;
    }


    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getCorn() {
        return corn;
    }

    @Override
    public Class<? extends ITask> getClazz() {
        return clazz;
    }
}
