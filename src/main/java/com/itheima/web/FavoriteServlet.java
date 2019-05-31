package com.itheima.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.Route;
import com.itheima.domain.User;
import com.itheima.service.FavoriteService;
import com.itheima.service.RouteService;
import com.itheima.util.BeanFactory;
import com.itheima.web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/favorite", name = "FavoriteServlet")
public class FavoriteServlet extends BaseServlet {
    //private FavoriteService favoriteService = new FavoriteServiceImpl();
    private FavoriteService favoriteService = (FavoriteService) BeanFactory.getBean("favoriteService");
    //private RouteService routeService = new RouteServiceImpl();
    private RouteService routeService = (RouteService) BeanFactory.getBean("routeService");

    /**
     * 查询当前登录的用户的收藏信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void myFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;
        try {
            //从session里得到当前登录的User
            User user = (User) request.getSession().getAttribute("loginUser");
            if (user != null) {
                //1.接收参数
                String pageNumberStr = request.getParameter("pageNumber");
                int pageNumber = 1;
                int pageSize = 12;
                if (pageNumberStr != null && !"".equals(pageNumberStr)) {
                    pageNumber = Integer.parseInt(pageNumberStr);
                }

                //2.封装实体：略
                //3.完成功能
                PageBean<Route> pageBean = favoriteService.myFavorite(user,pageNumber, pageSize);
                //4.处理结果
                info = new ResultInfo(true, pageBean);
            }else{
                //未登录状态，返回值：-1
                info = new ResultInfo(true, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false, "系统忙，请稍候");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;
        try {
            //先从session里获取当前登录的User对象
            User user = (User) request.getSession().getAttribute("loginUser");
            if (user != null) {//已登录状态
                //1.接收参数
                String rid = request.getParameter("rid");
                //2.完成功能
                boolean success = favoriteService.addFavorite(rid, user);
                //3.处理结果
                if (success) {
                    //收藏成功，查询最新的收藏数量
                    Route route = routeService.routeDetail(rid);
                    info = new ResultInfo(true, route.getCount());
                } else {
                    //收藏失败，返回结果：-2
                    info = new ResultInfo(true, -2);
                }
            } else {
                //未登录状态，返回值：-1
                info = new ResultInfo(true, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false, "系统忙，请稍候");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;
        try {
            //从session里获取当前登录的用户User对象
            User user = (User) request.getSession().getAttribute("loginUser");
            if (user != null) {
                //已登录状态，判断是否收藏

                //1.接收参数
                String rid = request.getParameter("rid");
                //2.完成功能
                boolean isFavorite = favoriteService.isFavorite(rid, user);
                //3.处理结果
                info = new ResultInfo(true, isFavorite);
            } else {
                //未登录状态，认为是未收藏，可收藏的状态
                info = new ResultInfo(true, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false, "系统忙，请稍候");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }

}
