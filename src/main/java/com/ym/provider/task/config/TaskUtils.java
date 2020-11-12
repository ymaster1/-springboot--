package com.ym.provider.task.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ymaster1
 * @date 2020/8/14 13:37
 */
@Slf4j
public class TaskUtils {
    public static List<ITaskEnum> getAllTask() throws IOException {
        List<ITaskEnum> list=new ArrayList<>();
//        指定扫描路径
        String basePackage = "com.ym.provider.task";
        String searchPaths = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
//      指定资源加载器
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        获取class资源
        Resource[] resources = resolver.getResources(searchPaths);
//        获取元数据工厂
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
        for (Resource resource : resources) {
//            获取元数据
            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
//            获取该类实现的所有接口的全限定类名
            String[] iterfaces=metadataReader.getClassMetadata().getInterfaceNames();
//            如果没有实现ITask就不要
            if(!ArrayUtils.contains(iterfaces, ITaskEnum.class.getName())){
                continue;
            }
            addTaskEnum(metadataReader,list);
        }
        return list;
    }

    /**
     * 处理获取到的class
     * @param metadataReader
     * @param taskEnumList
     */
    private static void addTaskEnum(MetadataReader metadataReader, List<ITaskEnum> taskEnumList){
        try {
//            加载当前class
            Class clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
//            所有任务都是写在枚举里面
            if(!clazz.isEnum()){
                return;
            }
//            添加当前enum里的所有任务到任务列表
            for(ITaskEnum taskEnum:(ITaskEnum[]) clazz.getEnumConstants()){
                taskEnumList.add(taskEnum);
            }
        } catch ( ClassNotFoundException e ) {
            log.error("类加载失败 {}",metadataReader.getClassMetadata().getClassName());
        }
    }
}
