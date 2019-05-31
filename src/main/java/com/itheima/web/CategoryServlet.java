package com.itheima.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.domain.ResultInfo;
import com.itheima.service.CategoryService;
import com.itheima.util.BeanFactory;
import com.itheima.web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns="/category", name="CategoryServlet")
public class CategoryServlet extends BaseServlet {
    //private CategoryService categoryService = new CategoryServiceImpl();
    private CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
    /**
     * 查询所有分类
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void queryAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;

        try {
            //1.接收参数：略
            //2.封装实体：略
            //3.完成功能
            String categories = categoryService.queryAll();
            //4.处理结果
            info = new ResultInfo(true, categories,"");
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false, "系统忙，请稍候");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }
}
