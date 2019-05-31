package com.itheima.dao.impl;

import com.itheima.dao.CategoryDao;
import com.itheima.domain.Category;
import com.itheima.util.JdbcUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    @Override
    public List<Category> queryAll() {
        return jdbcTemplate.query("select * from tab_category order by cid ", new BeanPropertyRowMapper<>(Category.class));
    }
}
