package com.itheima.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.domain.PageBean;
import com.itheima.domain.QueryVO;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.Route;
import com.itheima.service.RouteService;
import com.itheima.util.BeanFactory;
import com.itheima.util.BeanUtils;
import com.itheima.web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns="/route", name="RouteServlet")
public class RouteServlet extends BaseServlet {
    //private RouteService routeService = new RouteServiceImpl();
    private RouteService routeService = (RouteService) BeanFactory.getBean("routeService");

    public void rankRoutes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;
        try {
            //1.接收参数
            Map<String, String[]> map = request.getParameterMap();
            String pageNumberStr = request.getParameter("pageNumber");
            int pageNumber = 1;
            int pageSize = 8;
            if (pageNumberStr != null && !"".equals(pageNumberStr)) {
                pageNumber = Integer.parseInt(pageNumberStr);
            }

            //2.封装实体：要把搜索条件封装成一个QueryVO对象
            QueryVO vo = BeanUtils.populate(map, QueryVO.class);

            //3.完成功能
            PageBean<Route> pageBean = routeService.rankRoutes(vo, pageNumber, pageSize);

            //4.处理结果
            info = new ResultInfo(true, pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false, "系统忙，请稍候");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }

    public void routeDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;
        try {
            //1.接收参数
            String rid = request.getParameter("rid");
            //2.封装实体：略
            //3.完成功能：
            Route route = routeService.routeDetail(rid);
            //4.处理结果
            info = new ResultInfo(true, route);
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false, "系统忙，请稍候");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }

    /**
     * 搜索+分页
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;
        try {
            //1.接收参数：搜索条件和分页条件
            String cid = request.getParameter("cid");
            String rname = request.getParameter("rname");
            String pageNumberStr = request.getParameter("pageNumber");
            int pageNumber = 1;
            int pageSize = 8;
            if (pageNumberStr != null && !"".equals(pageNumberStr)) {
                pageNumber = Integer.parseInt(pageNumberStr);
            }

            //2.封装实体：略  如果搜索条件过多，可以把搜索条件封装成一个JavaBean。
            //3.完成功能：调用service，准备页面需要的所有数据
            PageBean<Route> pageBean =  routeService.search(cid,rname,pageNumber,pageSize);

            //4.处理结果
            info = new ResultInfo(true, pageBean);

        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false, "系统忙，请稍候");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }

    /**
     * 黑马精选
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void usefulRoutes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;
        try {
            //1.接收参数：略
            //2.封装实体：略
            //3.完成功能：
            Map<String, List<Route>> map = routeService.usefulRoutes();
            //4.处理结果：
            info = new ResultInfo(true, map);
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false, "系统忙，请稍候");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }

}
