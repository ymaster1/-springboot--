package com.ym.provider.controller;

import com.github.pagehelper.PageInfo;
import com.ym.provider.commons.exception.Response;
import com.ym.provider.commons.utils.excel.ImportResponse;
import com.ym.provider.entity.UserImportVo;
import com.ym.provider.entity.UserInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ymaster1
 * @date 2020/9/11 11:03
 * @description
 */
@RequestMapping("/test")
public interface Test {
    /**
     * 插入
     * @param userInfo
     * @return
     */
    @PostMapping("/insert")
    Response<Boolean> insertUser(@RequestBody UserInfo userInfo);

    /**
     * 通过tk查询list
     * @return
     */
    @PostMapping("/getAll/tk")
    Response<List<UserInfo>> getUsera();

    /**
     * list查询
     * @return
     */
    @PostMapping("/getAll/list")
    Response<List<UserInfo>> getList();

    /**
     * 更新
     * @param userInfo
     * @return
     */
    @PostMapping("/update")
    Response<Boolean> updateUsera(@RequestBody UserInfo userInfo);

    /**
     * 分页查询
     * @return
     */
    @PostMapping("/page")
    Response<PageInfo<UserInfo>> getPage();

    /**
     * 根据名称获取
     * @param name
     * @return
     */
    @GetMapping("/getByName")
    Response<UserInfo> getByName(@RequestParam("name") String name);

    /**
     * 根据userId获取
     * @param userId
     * @return
     */
    @GetMapping("/getByUserId")
    Response<UserInfo> getByUserId(@RequestParam("userId") String userId);

    /**
     * 测试异步
     * @return
     */
    @GetMapping("/getAllAsync")
    Response<UserInfo> getByAsync();

    /**
     * 导出
     * @param response
     * @return
     */
    @PostMapping("/importLine")
    Response<Boolean> importLine(HttpServletResponse response) throws IOException;
    /**
     * 导入
     * @param file
     * @return
     */
    @PostMapping("/importIn")
    Response<ImportResponse<UserImportVo>> importIn(MultipartFile file) throws IOException;

}
