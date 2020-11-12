package com.ym.provider.service.impl;

import com.ym.provider.async.UserAsync;
import com.ym.provider.commons.exception.MyException;
import com.ym.provider.commons.utils.excel.ExcelUtil;
import com.ym.provider.commons.utils.excel.ImportAbstractService;
import com.ym.provider.commons.utils.excel.ImportResponse;
import com.ym.provider.commons.utils.excel.ImportRowInfo;
import com.ym.provider.entity.UserImportVo;
import com.ym.provider.entity.UserInfo;
import com.ym.provider.mapper.UserInfoMapper;
import com.ym.provider.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author ymaster1
 * @date 2020/9/10 20:08
 * @description
 * @CacheConfig(cacheNames = "user") 指定该类下所有缓存使用同一个缓存空间user，可以被方法value覆盖
 */
@Service
@CacheConfig(cacheNames = "user")
public class UserInfoServiceImpl extends ImportAbstractService implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserAsync async;

    @Override
    public boolean insert(UserInfo info) {
        int insert = userInfoMapper.insert(info);
        return insert > 0;
    }

    /**
     * 使用byName缓存空间，没有设置时间，则使用默认配置，过期时间1小时
     * redis-key:   "user::userId"(没有指定key生成算法默认=value::参数)
     *
     * @param name
     * @return
     */
    @Override
//    @Cacheable(value = "byName")
    @Cacheable
    public UserInfo getUserByName(String name) {
        UserInfo info = new UserInfo();
        info.setUserName(name);
        return userInfoMapper.selectOne(info);
    }

    @Override
    public ImportResponse<UserImportVo> importIn(MultipartFile file) {
        return this.importFile(file, UserImportVo.class);
    }

    /**
     * 导出数据
     */
    @Override
    public Boolean importData(HttpServletResponse response) throws IOException {
        UserInfo info = new UserInfo();
        info.setCity("达州");
        List<UserInfo> select = userInfoMapper.select(info);
        List<UserImportVo> list = new ArrayList<>();
        select.forEach(e -> {
            UserImportVo vo = new UserImportVo();
            BeanUtils.copyProperties(e, vo);
            list.add(vo);
        });
// 导出
        ExcelUtil.writeExcel(response, list, "ymaster1", "ymaster1", new UserImportVo());
        return true;
    }

    @Override
    public List<UserInfo> getAllAsync() {
        List<Future<List<UserInfo>>> tasks = new ArrayList<>();
        List<UserInfo> results = new ArrayList<>();
        try {
            tasks.add(async.getFalse());
            tasks.add(async.getTrue());
            //各个任务执行完毕
            for (Future<List<UserInfo>> task : tasks) {
                //每个任务都会再在此阻塞。
                results.addAll(task.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * 使用ymaster1命名空间，过期时间只有5分钟
     * redis-key:   "ymaster1::userId"(没有指定key生成算法默认=value::参数)
     *
     * @param userId
     * @return 可以自定义key策略
     */
    @Override
    @Cacheable(value = "ymaster1", keyGenerator = "keyGenerator")
    public UserInfo getUserByUserId(String userId) {
        ThreadLocal<String> localName = new ThreadLocal();
        localName.set("张三");
        String name = localName.get();
        localName.remove();
        UserInfo info = new UserInfo();
        info.setUserId(userId);
        return userInfoMapper.selectOne(info);
    }

    @Override
    public void validRow(ImportRowInfo rowInfo) {
        UserImportVo vo = (UserImportVo) rowInfo;

        if (vo.getSex() != 1) {
            throw new MyException("人家不是女生！");
        }

    }
}
