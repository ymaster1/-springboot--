package com.ym.provider.commons.ext;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * @author ymaster1
 * @date 2020/11/14 16:23
 * @description 实现该接口可以注册bean，然后可以通过@Import动态导入这些bean
 */
public class YmImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 注册被Ym注解标记的class
        YmClassPathBeanDefinitionScanner scanner = new YmClassPathBeanDefinitionScanner(registry,Ym.class);
        scanner.registerFillter();
//        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
//        scanner.addIncludeFilter(new AnnotationTypeFilter(Ym.class));
//        需要指定包扫描路径
        scanner.scan("com.ym.provider");
    }
}
