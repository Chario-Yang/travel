package com.itheima.dao.impl;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import com.itheima.util.JdbcUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject("select * from tab_user where username = ?", new BeanPropertyRowMapper<>(User.class), username);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("找不到用户User[username="+username+"]");
        }
        return user;
    }

    @Override
    public int add(User user) {
        Object[] params = {user.getUid(),user.getUsername(),user.getPassword(),user.getName(),user.getBirthday(),user.getSex(),user.getTelephone(),user.getEmail(),user.getStatus(),user.getCode()};
        return jdbcTemplate.update("INSERT INTO tab_user (uid,username,PASSWORD,NAME,birthday,sex,telephone,email,STATUS,CODE) VALUES(?,?,?,?,?,?,?,?,?,?)",params);
    }

    @Override
    public int active(String code) {
        return jdbcTemplate.update("update tab_user set status = 'Y' where code = ?", code);
    }

    @Override
    public User login(String username, String password) {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject("select * from tab_user where username = ? and password = ?", new BeanPropertyRowMapper<>(User.class), username, password);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("找不到用户User[username="+username+", password="+password+"]");
        }
        return user;
    }
}
