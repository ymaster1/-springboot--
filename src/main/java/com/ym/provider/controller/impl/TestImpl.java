package com.ym.provider.controller.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ym.provider.commons.exception.Response;
import com.ym.provider.commons.utils.ApplicationContextHolder;
import com.ym.provider.commons.utils.excel.ImportResponse;
import com.ym.provider.controller.Test;
import com.ym.provider.entity.UserImportVo;
import com.ym.provider.entity.UserInfo;
import com.ym.provider.mapper.UserInfoMapper;
import com.ym.provider.repository.UserInfoRepository;
import com.ym.provider.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ymaster1
 * @date 2020/9/11 14:43
 * @description
 */
@RestController
@Slf4j
public class TestImpl implements Test {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public Response<UserInfo> getByName(String name) {
        return Response.success(userInfoService.getUserByName(name));
    }

    @Override
    public Response<ImportResponse<UserImportVo>> importIn(MultipartFile file) throws IOException {
        return Response.success(userInfoService.importIn(file));
    }

    @Override
    public Response<Boolean> importLine(HttpServletResponse response) throws IOException {
        return Response.success(userInfoService.importData(response));
    }

    @Override
    public Response<UserInfo> getByAsync() {
        return Response.success(userInfoService.getAllAsync());
    }

    @Override
    public Response<UserInfo> getByUserId(String userId) {
        return Response.success(userInfoService.getUserByUserId(userId));
    }

    @Override
    public Response<Boolean> insertUser(UserInfo userInfo) {
        return Response.success(userInfoMapper.insertSelective(userInfo) > 0);
    }

    @Override
    public Response<List<UserInfo>> getUsera() {
        UserInfo info = new UserInfo();
        info.setStatusFlag(true);
        return Response.success(userInfoMapper.select(info));
    }

    @Override
    public Response<List<UserInfo>> getList() {
        return Response.success(userInfoMapper.getList());
    }

    @Override
    public Response<Boolean> updateUsera(UserInfo userInfo) {
        return Response.success(userInfoMapper.updateByPrimaryKeySelective(userInfo) > 0);
    }

    @Override
    public Response getPage() {
//        只对接下来的第一个查询能有效分页
        PageHelper.startPage(1,3);
//        只能获取每页的数据内容
        Page<UserInfo> page = userInfoMapper.getPage();
//        额外获取页信息
        PageInfo<UserInfo> pageInfo = new PageInfo<>(page);
        return Response.success(pageInfo);
    }
}
