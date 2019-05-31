package com.itheima.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.util.BeanFactory;
import com.itheima.util.BeanUtils;
import com.itheima.util.MailUtils;
import com.itheima.util.UUIDUtils;
import com.itheima.web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns="/user", name="UserServlet")
public class UserServlet extends BaseServlet {

    //private UserService userService = new UserServiceImpl();
    private UserService userService = (UserService) BeanFactory.getBean("userService");

    /**
     * 给页面表单校验插件的remote规则进行调用的
     * 需要给客户端返回：true/false
     * true：校验通过，用户名可用，用户名不存在
     * false：校验不通过，用户名不可用，用户名已存在
     */
    public void isValidUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收参数
        String username = request.getParameter("username");
        //完成功能
        boolean isExist = userService.isExist(username);
        //处理结果
        response.getWriter().print(!isExist);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //把session对象销毁掉
        request.getSession().invalidate();
        //重定向跳转到登录页面
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    public void getLoginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;

        //1.从session里得到User对象
        User user = (User) request.getSession().getAttribute("loginUser");
        //2.判断User对象是否为空
        if (user != null) {//已登录状态
            //给客户端返回 用户的姓名
            info = new ResultInfo(true, user.getName(), "");
        }else{//未登录状态
            info = new ResultInfo(false);
        }
        //3. 返回结果
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }


    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;

        try {
            //0. 先校验验证码
            String check = request.getParameter("check");
            String CHECKCODE_SERVER = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
            if (check.equalsIgnoreCase(CHECKCODE_SERVER)) {//验证码正确，进行用户名和密码校验
                //1.接收参数
                String username = request.getParameter("username");
                String password = request.getParameter("password");

                //2.封装实体：略
                //3.完成功能
                User user = userService.login(username, password);
                //4.处理结果
                if (user != null) {//说明：用户名和密码正确
                    //判断是否激活
                    if ("Y".equals(user.getStatus())) {//说明：用户已激活
                        info = new ResultInfo(true);

                        //已经登录成功，把User对象放到session里
                        request.getSession().setAttribute("loginUser", user);

                    }else{
                        info = new ResultInfo(false, "请激活后再登录");
                    }
                }else{
                    info = new ResultInfo(false, "用户名或密码错误");
                }
            }else{//验证码错误
                info = new ResultInfo(false, "验证码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false, "系统忙，请稍候");
        }

        //返回结果
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }

    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收参数
        String code = request.getParameter("code");//激活码
        //2.完成功能
        boolean success = userService.active(code);
        //3.处理结果
        if (success) {
            //激活成功，跳转到登录页面
            response.sendRedirect(request.getContextPath() + "/login.html");
        }else{
            //激活失败，显示：激活失败
            response.getWriter().print("激活失败，请重试或联系管理员");
        }
    }

    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = null;

        try {
            //0.先校验验证码
            String check = request.getParameter("check");
            String CHECKCODE_SERVER = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
            if (check.equalsIgnoreCase(CHECKCODE_SERVER)) {//验证码正确
                //1.接收参数
                Map<String, String[]> map = request.getParameterMap();
                //2.封装实体
                User user = BeanUtils.populate(map, User.class);
                user.setStatus("N");//用户默认是未激活状态。
                user.setCode(UUIDUtils.getUuid());//生成一个激活码
                //3.完成功能
                boolean success = userService.register(user);
                if (success) {
                    //给用户发送激活邮件
                    String url = "http://localhost/travel/user?action=active&code=" + user.getCode();
                    String content = user.getName() + "你的帐号已经注册成功，请<a href='"+url+"'>点击激活</a>后再登录";
                    MailUtils.sendMail(user.getEmail(), content);
                }
                //4.处理结果
                info = new ResultInfo(success);
            }else{//验证码错误
                info = new ResultInfo(false, "验证码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false, "系统忙，请稍候");
        }

        //返回结果：要把info转换成json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().print(json);
    }

}
