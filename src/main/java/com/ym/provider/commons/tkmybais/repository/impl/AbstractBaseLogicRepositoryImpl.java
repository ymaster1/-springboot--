package com.ym.provider.commons.tkmybais.repository.impl;

import com.github.pagehelper.PageInfo;
import com.ym.provider.commons.tkmybais.BaseLogicEntity;
import com.ym.provider.commons.tkmybais.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author : yinlin
 * @version : 1.0
 * @date : 2019-01-29 10:17
 * @Description :
 * @Copyright : Copyright (c) 2018
 * @Company : EasySite Technology Chengdu Co. Ltd.
 * @link : cn.org.easysite.spring.boot.tk.mybatis.core.repository.impl.AbstractBaseLogicRepositoryImpl
 */
@Slf4j
public abstract class AbstractBaseLogicRepositoryImpl<T extends BaseLogicEntity> extends AbstractBaseRepositoryImpl<T> implements BaseRepository<T> {

    /**
     * 创建一个Class的对象来获取泛型的class
     */
    private Class<?> clazz;

    public Class<?> getClazz() {
        if (clazz == null) {
            //获取泛型的Class对象,getGenericSuperclass()返回直接继承的父类（包含泛型参数）,ParameterizedType就是泛型
            if (this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
//getActualTypeArguments获取参数化类型的数组，泛型可能有多个
                clazz = ((Class<?>)
                        (((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
            }
        }
        return clazz;
    }

    @Override
    public int insert(T record) {
        record.setServerCreateTime(null);
        record.setServerUpdateTime(null);
        return super.insert(record);
    }

    @Override
    public int update(T record) {
        record.setServerUpdateTime(null);
        return super.update(record);
    }

    @Override
    public List<T> select(T param) {
        if (param != null) {
            param.setStatusFlag(true);
        }
        return super.select(param);
    }

    @Override
    public int delete(T param) {
        param.setStatusFlag(false);
        return update(param);
    }

    @Override
    public T selectByPk(Serializable pk) {
        T entity = super.selectByPk(pk);
        return entity != null && entity.getStatusFlag() ? entity : null;
    }

    @Override
    public List<T> selectAll() {
        T param = null;
        try {
            param = (T) getClazz().newInstance();
        } catch (InstantiationException e) {
            log.warn(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.warn(e.getMessage(), e);
        }
        return this.select(param);
    }

    @Override
    public List<T> selectByPks(Iterable<? extends Serializable> pks) {
        List<T> entities = super.selectByPks(pks);
        for (int i = entities.size() - 1; i >= 0; i --) {
            if (!entities.get(i).getStatusFlag()) {
                entities.remove(i);
            }
        }
        return entities;
    }

    @Override
    public T selectOne(T param) {
        param.setStatusFlag(true);
        return super.selectOne(param);
    }

    @Override
    public PageInfo<T> selectPage(T param, int pageNum, int pageSize) {
        param.setStatusFlag(true);
        return super.selectPage(param, pageNum, pageSize);
    }

    @Override
    public PageInfo<T> selectPageAndCount(T param, int pageNum,
                                          int pageSize) {
        param.setStatusFlag(true);
        return super.selectPageAndCount(param, pageNum, pageSize);
    }

}
