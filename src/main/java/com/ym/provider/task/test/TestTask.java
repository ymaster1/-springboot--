package com.ym.provider.task.test;

import com.ym.provider.task.config.ITask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ym.provider.task.test.TestEnum.TEST_TASK;

/**
 * @author ymaster1
 * @date 2020/8/14 14:06
 */
@Slf4j
@Service(TEST_TASK)
public class TestTask implements ITask {
    @Override
    public void run() {
        log.info("hello ymaster1!");
    }
}
