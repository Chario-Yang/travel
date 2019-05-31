package com.itheima.dao.impl;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;

public class UserDaoImpl2 implements UserDao {
    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public int add(User user) {
        return 0;
    }

    @Override
    public int active(String code) {
        return 0;
    }

    @Override
    public User login(String username, String password) {
        return null;
    }
}
