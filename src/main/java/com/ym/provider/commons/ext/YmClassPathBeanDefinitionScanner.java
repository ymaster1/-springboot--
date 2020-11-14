package com.ym.provider.commons.ext;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author ymaster1
 * @date 2020/11/14 16:26
 * @description
 */
public class YmClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    /**
     * 被标记的类型
     */
    private Class clazz;

    /**
     * 调用父类的构造器
     *
     * @param registry
     */
    public YmClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> clazz) {
        super(registry);
        this.clazz = clazz;
    }

    /**
     * 添加被指定注解标记的class到容器
     */
    public void registerFillter() {
        addIncludeFilter(new AnnotationTypeFilter(clazz));
    }

    /**
     * 指定包扫描路径
     * @param basePackages
     * @return
     */
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }
}
