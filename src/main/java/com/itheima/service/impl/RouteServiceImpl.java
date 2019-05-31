package com.itheima.service.impl;

import com.itheima.dao.RouteDao;
import com.itheima.domain.*;
import com.itheima.service.RouteService;
import com.itheima.util.BeanFactory;
import com.itheima.util.BeanUtils;
import com.itheima.util.PageUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteServiceImpl implements RouteService {
    //private RouteDao routeDao = new RouteDaoImpl();
    private RouteDao routeDao = (RouteDao) BeanFactory.getBean("routeDao");

    @Override
    public Map<String, List<Route>> usefulRoutes() {
        //1.人气旅游
        List<Route> hotRoutes = routeDao.hotRoutes();
        //2.最新旅游
        List<Route> newRoutes = routeDao.newRoutes();
        //3.主题旅游
        List<Route> themeRoutes = routeDao.themeRoutes();


        Map<String, List<Route>> map = new HashMap<>();
        map.put("hotRoutes", hotRoutes);
        map.put("newRoutes", newRoutes);
        map.put("themeRoutes", themeRoutes);
        return map;
    }

    @Override
    public PageBean<Route> search(String cid, String rname, int pageNumber, int pageSize) {
        PageBean<Route> pageBean = new PageBean<>();
        /*当前页码 */
        pageBean.setPageNumber(pageNumber);
        /*每页多少条*/
        pageBean.setPageSize(pageSize);
        /*总共多少数据*/
        int totalCount = routeDao.getTotalCount(cid,rname);
        pageBean.setTotalCount(totalCount);
        /*分了多少页 = Math.ceil(totalCount*1.0 / pageSize)*/
        int pageCount = (int) Math.ceil(totalCount * 1.0 / pageSize);
        pageBean.setPageCount(pageCount);
        /*页码条从几开始显示*/
        int[] pagination = PageUtils.pagination(pageNumber, pageCount);
        pageBean.setStart(pagination[0]);
        /*页码条显示到几结束*/
        pageBean.setEnd(pagination[1]);
        /*当前页的数据集合*/
        int index = (pageNumber - 1) * pageSize;
        List<Route> routeList = routeDao.search(cid,rname,index, pageSize);
        pageBean.setData(routeList);

        return pageBean;
    }

    @Override
    public Route routeDetail(String rid) {
        //路线本身 Route对象
        Map<String, Object> map = routeDao.findById2(rid);
        //把Map封装成一个Route对象
        Route route = BeanUtils.populate(map, Route.class);
        //把Map封装成一个Category对象
        Category category = BeanUtils.populate(map, Category.class);
        route.setCategory(category);
        //把Map封装成一个Seller对象
        Seller seller = BeanUtils.populate(map, Seller.class);
        route.setSeller(seller);

        //图片列表 List<RouteImg>
        List<RouteImg> routeImgList = routeDao.findImages(rid);
        route.setRouteImgList(routeImgList);

        return route;
    }

    @Override
    public PageBean<Route> rankRoutes(QueryVO vo, int pageNumber, int pageSize) {
        PageBean<Route> pageBean = new PageBean<>();
        /*当前页码 */
        pageBean.setPageNumber(pageNumber);
        /*每页多少条*/
        pageBean.setPageSize(pageSize);
        /*总共多少数据*/
        int totalCount = routeDao.getTotalCount2(vo);
        pageBean.setTotalCount(totalCount);
        /*分了多少页*/
        int pageCount = PageUtils.calcPageCount(totalCount, pageSize);
        pageBean.setPageCount(pageCount);
        /*页码条从几开始显示*/
        int[] pagination = PageUtils.pagination(pageNumber, pageCount);
        pageBean.setStart(pagination[0]);
        /*页码条显示到几结束*/
        pageBean.setEnd(pagination[1]);
        /*当前页的数据集合*/
        int index = PageUtils.calcSqlLimitIndex(pageNumber, pageSize);
        List<Route> routeList = routeDao.rankRoutes(vo, index, pageSize);
        pageBean.setData(routeList);
        return pageBean;
    }

/*    @Override
    public Route routeDetail(String rid) {
        //路线本身 Route对象
        Route route = routeDao.findById(rid);
        //商家信息 Seller对象
        Seller seller = routeDao.findSellerById(route.getSid());
        route.setSeller(seller);
        //分类信息 Category
        Category category = routeDao.findCategoryById(route.getCid());
        route.setCategory(category);
        //图片列表 List<RouteImg>
        List<RouteImg> routeImgList = routeDao.findImages(rid);
        route.setRouteImgList(routeImgList);

        return route;
    }*/
}
