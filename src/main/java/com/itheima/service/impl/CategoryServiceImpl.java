package com.itheima.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.dao.CategoryDao;
import com.itheima.domain.Category;
import com.itheima.service.CategoryService;
import com.itheima.util.BeanFactory;
import com.itheima.util.JedisUtils;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    //private CategoryDao categoryDao = new CategoryDaoImpl();
    private CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");

    @Override
    public String queryAll() throws JsonProcessingException {
        //1. 优先查找缓存
        String categories = JedisUtils.getCache("categories");
        //2. 如果找不到缓存，就要调用dao，查询MySql数据库
        if (categories == null || "".equals(categories)) {
            List<Category> categoryList = categoryDao.queryAll();
            //把categoryList转换成json格式字符串，再保存到Redis里
            ObjectMapper mapper = new ObjectMapper();
            categories = mapper.writeValueAsString(categoryList);
            JedisUtils.setCache("categories", categories);
        }
        return categories;
    }
}
