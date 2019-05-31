package com.itheima.service;

import com.itheima.domain.PageBean;
import com.itheima.domain.QueryVO;
import com.itheima.domain.Route;

import java.util.List;
import java.util.Map;

public interface RouteService {
    Map<String, List<Route>> usefulRoutes();

    PageBean<Route> search(String cid, String rname, int pageNumber, int pageSize);

    Route routeDetail(String rid);

    PageBean<Route> rankRoutes(QueryVO vo, int pageNumber, int pageSize);
}
