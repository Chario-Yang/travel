package com.itheima.service.impl;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.util.BeanFactory;
import com.itheima.util.Md5Utils;

public class UserServiceImpl implements UserService {

    //UserServiceImpl依赖于UserDaoImpl2/UserDaoImpl。耦合性过强，不利于维护
    //编译期依赖：在代码编译阶段，就必须正确的引入依赖；否则编译不通过
    //编译期依赖：通过反射来解决编译期依赖
    //private UserDao userDao = new UserDaoImpl();
    private UserDao userDao = (UserDao) BeanFactory.getBean("userDao");

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     *  如果存在，返回true；否则返回false
     */
    @Override
    public boolean isExist(String username) {
        User user = userDao.findByUsername(username);
        return user!=null;
    }

    @Override
    public boolean register(User user) throws Exception {
        String md5 = Md5Utils.encodeByMd5(user.getPassword());
        user.setPassword(md5);
        int count = userDao.add(user);
        return count > 0;
    }

    @Override
    public boolean active(String code) {
        int count = userDao.active(code);
        return count > 0;
    }

    @Override
    public User login(String username, String password) throws Exception {
        password = Md5Utils.encodeByMd5(password);

        return userDao.login(username, password);
    }
}
