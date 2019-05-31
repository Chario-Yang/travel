package com.itheima.dao;

import com.itheima.domain.*;

import java.util.List;
import java.util.Map;

public interface RouteDao {
    List<Route> hotRoutes();

    List<Route> newRoutes();

    List<Route> themeRoutes();

    int getTotalCount(String cid, String rname);

    List<Route> search(String cid, String rname, int index, int pageSize);

    Route findById(String rid);

    Seller findSellerById(Integer sid);

    Category findCategoryById(Integer cid);

    List<RouteImg> findImages(String rid);

    Map<String, Object> findById2(String rid);

    int getTotalCount2(QueryVO vo);

    List<Route> rankRoutes(QueryVO vo, int index, int pageSize);
}
