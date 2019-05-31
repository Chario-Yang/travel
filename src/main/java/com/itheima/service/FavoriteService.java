package com.itheima.service;

import com.itheima.domain.PageBean;
import com.itheima.domain.Route;
import com.itheima.domain.User;

public interface FavoriteService {
    boolean isFavorite(String rid, User user);

    boolean addFavorite(String rid, User user);

    PageBean<Route> myFavorite(User user, int pageNumber, int pageSize);
}
