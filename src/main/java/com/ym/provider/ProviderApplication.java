package com.ym.provider;

import com.ym.provider.commons.exception.Response;
import com.ym.provider.commons.interceptor.ApiIdempotent;
import com.ym.provider.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

//import org.mybatis.spring.annotation.MapperScan;

/**
 * @author ymast
 * 需要引入的是tk的@MapperScan，但是不能直接扫到baseMapper
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.ym.provider.mapper")
@RestController
public class ProviderApplication {
    @Autowired
    private ConfigurableApplicationContext applicationContext;
    @Autowired
    private TokenService tokenService;
    @Value("${test.name}")
    private String myName;
    @Value("${ext.name}")
    private String extName;

    @PostMapping("/getConfig")
    @ApiIdempotent
    public String getConfig() {
//        return myName;
//    通过环境变量来获取，可以实现动态更新，不需要重启
        return applicationContext.getEnvironment().getProperty("test.name") + "---" + applicationContext.getEnvironment().getProperty("ext.name");
    }
    @PostMapping("/getToken")
    public Response getToken(){
        return Response.success(tokenService.createToken());
    }

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);

    }

}
