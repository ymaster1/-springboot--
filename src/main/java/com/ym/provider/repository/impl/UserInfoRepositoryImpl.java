package com.ym.provider.repository.impl;

import com.ym.provider.commons.tkmybais.repository.impl.AbstractBaseLogicRepositoryImpl;
import com.ym.provider.entity.UserInfo;
import com.ym.provider.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ymaster1
 * @date 2020/9/14 11:38
 * @description 基于tk做的一层封装，tk里面有的方法用着不太方便
 */
@Service
public class UserInfoRepositoryImpl extends AbstractBaseLogicRepositoryImpl<UserInfo> implements UserInfoRepository {
    @Override
    public List<UserInfo> select(UserInfo param) {
        return super.select(param);
    }
}
