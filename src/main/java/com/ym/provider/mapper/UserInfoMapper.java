package com.ym.provider.mapper;

import com.github.pagehelper.Page;
import com.ym.provider.commons.tkmybais.CommonsMapper;
import com.ym.provider.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户信息表(UserInfo)表数据库访问层
 *
 * @author ymaster1
 * @since 2020-09-10 19:36:01
 */
@Mapper
public interface UserInfoMapper extends CommonsMapper<UserInfo> {

    public List<UserInfo> getList();

    /**
     * 返回值为page能显示分页相关的详细信息，也可以直接返回list,只显示数据
     * @return
     */
    public Page<UserInfo> getPage();
}