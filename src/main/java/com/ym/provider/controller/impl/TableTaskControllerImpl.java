package com.ym.provider.controller.impl;

import com.ym.provider.commons.exception.Response;
import com.ym.provider.controller.TableTaskController;
import com.ym.provider.entity.TimingTask;
import com.ym.provider.service.TimingTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ymaster1
 * @date 2020/11/13 17:50
 * @description
 */
@RestController
@Slf4j
public class TableTaskControllerImpl implements TableTaskController {
    @Autowired
    private TimingTaskService service;
    @Override
    public Response<Boolean> insertUser(TimingTask timingTask) {
        return Response.success(service.insert(timingTask));
    }
}
