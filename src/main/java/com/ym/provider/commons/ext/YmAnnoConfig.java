package com.ym.provider.commons.ext;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ymaster1
 * @date 2020/11/15 0:45
 * @description
 * 导入实现了ImportBeanDefinitionRegistrar接口的逻辑注册的bean
 */
@Import(YmImportBeanDefinitionRegistrar.class)
@Configuration
public class YmAnnoConfig {
}
