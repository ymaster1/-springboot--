package com.ym.provider.async.impl;

import com.ym.provider.async.UserAsync;
import com.ym.provider.entity.UserInfo;
import com.ym.provider.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author ymaster1
 * @date 2020/10/22 14:53
 * @description
 */
@Service
public class UserAsyncImpl implements UserAsync {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    @Async
    public Future<List<UserInfo>> getTrue() {
        UserInfo userInfo = new UserInfo();
        userInfo.setStatusFlag(true);
        return new AsyncResult<>(userInfoMapper.select(userInfo));
    }

    @Override
    @Async(value = "taskExecutorService")
    public Future<List<UserInfo>> getFalse() {
        UserInfo userInfo = new UserInfo();
        userInfo.setStatusFlag(false);
        return new AsyncResult<>(userInfoMapper.select(userInfo));
    }
}
