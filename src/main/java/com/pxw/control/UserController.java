package com.pxw.control;

import com.pxw.pojo.User;
import com.pxw.service.UserService;
import com.pxw.util.CheckCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;



@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //登录
    @RequestMapping("/login")
    public String login(@RequestBody User user, @RequestParam String checkCode, HttpServletRequest request) throws IOException {

        // 获取输入的验证码
        HttpSession session = request.getSession();
        String checkCodeGen = (String) session.getAttribute("checkCodeGen");
        //防止空指针
        if (checkCodeGen.equalsIgnoreCase(checkCode)){
            User result = userService.login(user.getUserName(), user.getPassword());
            if (result != null){
                session.setAttribute("userName",result.getUserName());
                return "success" ;
            }
        }else{
            return "验证码错误";
        }
        return "用户名或密码错误";

    }

    //验证码生成
    @RequestMapping("/checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //生成验证码并返回
        String checkCodeGen = CheckCodeUtil.generateVerifyCode(4);

        //存入session
        HttpSession session = request.getSession();
        session.setAttribute("checkCodeGen",checkCodeGen);

        System.out.println(session.getId());
        //输出图片
        ServletOutputStream os = response.getOutputStream();
        CheckCodeUtil.outputImage(100,40,os,checkCodeGen);

    }

    //登录态获取
    @RequestMapping("/checkLogin")
    public String checkLogin(HttpSession session){
        return (String)session.getAttribute("userName");
    }

    //退出
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        //清除session信息
        session.setAttribute("userName",null);
        return "success";
    }

    //查询
    @RequestMapping("/selectByCondition")
    public String selectByCondition(@RequestBody  User user ,@RequestParam("currentPage") Integer currentPage,@RequestParam("pageSize") Integer pageSize){
        //调用服务查询所有
        String  pageInfo = userService.selectByCondition(user,currentPage,pageSize);
        return pageInfo;
    }

    //添加用户
    @RequestMapping("/add")
    public String add(@RequestBody User user){
        return userService.add(user);
    }

    //批量禁用
    @RequestMapping("/disableByIds")
    public String disableByIds(@RequestBody int[] ids){
        return userService.disableByIds(ids);
    }

    //更新
    @RequestMapping("update")
    public String update(@RequestBody User user,@RequestParam("flag") boolean flag){
        return userService.update(user,flag);
    }

    //删除
    @RequestMapping("deleteById")
    public String delete(@RequestBody User user){
        return userService.deleteById(user);
    }

}
