package com.ym.provider.async;

import com.ym.provider.entity.UserInfo;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author ymaster1
 * @date 2020/10/22 14:53
 * @description
 */
public interface UserAsync {
    /**
     * 获取有效数据
     * @return
     */
    Future<List<UserInfo>> getTrue();

    /**
     * 获取无效数据
     * @return
     */
    Future<List<UserInfo>> getFalse();
}
