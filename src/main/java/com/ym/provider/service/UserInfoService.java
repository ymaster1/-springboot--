package com.ym.provider.service;

import com.ym.provider.commons.utils.excel.ImportResponse;
import com.ym.provider.entity.UserImportVo;
import com.ym.provider.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 用户信息表(UserInfo)表服务接口
 *
 * @author ymaster1
 * @since 2020-09-10 17:09:45
 */
public interface UserInfoService {
    /**
     * 插入
     * @param info
     * @return
     */
    boolean insert(UserInfo info);

    /**
     * 根据name获取
     * @param name
     * @return
     */
    UserInfo getUserByName(String name);

    /**
     * 根据userId获取
     * @param userId
     * @return
     */
    UserInfo getUserByUserId(String userId);

    /**
     * 测试异步获取
     * @return
     */
    List<UserInfo> getAllAsync();

    /**
     * 导出
     * @param response
     * @return
     * @throws IOException
     */
    Boolean importData(HttpServletResponse response) throws IOException;

    /**
     * 导入
     * @param file
     * @return
     * @throws IOException
     */
    ImportResponse<UserImportVo> importIn(MultipartFile file) throws IOException;

}