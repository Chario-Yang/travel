package com.itheima.dao;

import com.itheima.domain.User;

public interface UserDao {
    User findByUsername(String username);

    int add(User user);

    int active(String code);

    User login(String username, String password);
}
