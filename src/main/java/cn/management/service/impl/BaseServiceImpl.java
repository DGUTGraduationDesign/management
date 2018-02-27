package cn.management.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;

import cn.management.domain.BaseEntity;
import cn.management.service.BaseService;
import cn.management.util.MyMapper;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import tk.mybatis.mapper.entity.Example;

/**
 * 通用Service层实现类
 */
public class BaseServiceImpl<M extends MyMapper, E extends BaseEntity> implements BaseService<M, E> {

    @Autowired
    protected M mapper;

    @Override
    @Transactional
    public E add(E entity) {
        if (mapper.insert(entity) >= 1) {
            return entity;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public E addSelectiveMapper(E entity) {
        if (mapper.insertSelective(entity) >= 1) {
            return entity;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteById(Object id) {
        return mapper.deleteByPrimaryKey(id) >= 1;
    }

    @Override
    @Transactional
    public boolean delele(Object entity) {
        return mapper.delete(entity) >= 1;
    }

    @Override
    @Transactional
    public boolean delete(Example example) {
        return mapper.deleteByExample(example) >= 1;
    }

    @Override
    public List<E> getItemsByCondition(Object example) {
        return mapper.selectByExample(example);
    }

    @Override
    public List<E> getItemsByCondition(String condition) {
        Class<? extends BaseServiceImpl> aClass = this.getClass();
        ParameterizedTypeImpl genericSuperclass = (ParameterizedTypeImpl) aClass.getGenericSuperclass();
        Type type = genericSuperclass.getActualTypeArguments()[1];
        Example example = new Example((Class<?>) type);
        example.createCriteria().andCondition(condition);
        return mapper.selectByExample(example);
    }

    @Override
    public List<E> getAllItems() {
        return mapper.selectAll();
    }

    @Override
    public List<E> getItemsByPage(E entity, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.select(entity);
    }

    @Override
    public List<E> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.selectByExample(example);
    }

    @Override
    public List<E> getItems(E record) {
        return mapper.select(record);
    }

    @Override
    public E getItem(E record) {
        List<E> items = getItemsByPage(record, 1, 1);
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        return items.get(0);
    }

    @Override
    public E getItemById(Object id) {
        return (E) mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<E> getItemsByIds(List ids) {
    	if (ids == null || ids.size() == 0) {
    		return null;
    	}
        String idStr = String.join(",", ids);
        String condition = "id IN(" + idStr + ")";
        return this.getItemsByCondition(condition);
    }

    @Override
    public int countByCondition(Example example) {
    	return mapper.selectCountByExample(example);
    }
    
    @Override
    @Transactional
    public boolean update(E entity) {
        if (null != entity.getId()) {
            return mapper.updateByPrimaryKeySelective(entity) >= 1;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean fullUpdate(E entity) {
        if (null != entity.getId()) {
            return mapper.updateByPrimaryKey(entity) >= 1;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateByExampleSelective(E entity, Example example) {
        return mapper.updateByExampleSelective(entity, example) >= 1;
    }

}
