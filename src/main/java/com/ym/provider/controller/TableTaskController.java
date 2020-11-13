package com.ym.provider.controller;

import com.ym.provider.commons.exception.Response;
import com.ym.provider.entity.TimingTask;
import com.ym.provider.entity.UserInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ymaster1
 * @date 2020/11/13 17:50
 * @description
 */
@RequestMapping("/table/task")
public interface TableTaskController {
    /**
     * 插入
     * @param timingTask
     * @return
     */
    @PostMapping("/insert")
    Response<Boolean> insertUser(@RequestBody TimingTask timingTask);
}
