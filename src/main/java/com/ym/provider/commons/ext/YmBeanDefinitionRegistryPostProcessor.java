package com.ym.provider.commons.ext;

import com.ym.provider.entity.UserInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.support.AbstractBeanDefinition.AUTOWIRE_BY_NAME;


/**
 * @author ymaster1
 * @date 2020/11/14 16:24
 * @description
 */
@Component
public class YmBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    /**
     * 注册自己的bean
     *
     * @param registry
     * @throws BeansException
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //创建BeanDefinitionBuilder
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(UserInfo.class);
        //设置属性值
        builder.addPropertyValue("userName", "Ymaster1");
        //设置可通过@Autowire注解引用
        builder.setAutowireMode(AUTOWIRE_BY_NAME);
        //注册到BeanDefinitionRegistry
        registry.registerBeanDefinition("userInfo", builder.getBeanDefinition());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
