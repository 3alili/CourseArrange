package com.pxw.util;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by pxw on 2022/4/29 15:38
 *
 * @author pxw
 */

@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        //1. 判断session中是否有user
        HttpSession session = req.getSession();
        Object user = session.getAttribute("userName");
        System.out.println(user);
        //2. 判断user是否为null
        if (user != null) {
            System.out.println("!!!");
            // 登录过了 放行
            chain.doFilter(request, response);
        } else {

            //判断访问资源路径是否和登录注册相关
            //1,在数组中存储登陆和注册相关的资源路径
            String[] urls = {
                    "login.html",
                    "t/login.html",
                    "imgs/",
                    "css/",
                    "js/",
                    "element-ui/",
                    "user/login",
                    "user/checkCode"};
            //2,获取当前访问的资源路径
            String url = req.getRequestURL().toString();
            //3,遍历数组，获取到每一个需要放行的资源路径
            for (String u : urls) {
                //4,判断当前访问的资源路径字符串是否包含要放行的的资源路径字符串
                if (url.contains(u)) {
                    System.out.println(url);
                    //找到了，放行
                    chain.doFilter(request, response);
                    //break;
                    return;
                }
            }
            // 没有登陆，存储提示信息，跳转到登录页面
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendRedirect("/login.html?1");
        }







    }
}
