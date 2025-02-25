package com.pxw.control;

import com.alibaba.fastjson.JSON;
import com.pxw.pojo.Room;
import com.pxw.pojo.Teacher;
import com.pxw.pojo.User;
import com.pxw.service.RoomService;
import com.pxw.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;


    //登录
    @RequestMapping("/login")
    public String login(@RequestBody Teacher teacher, @RequestParam String checkCode, HttpServletRequest request) throws IOException {

        // 获取输入的验证码
        HttpSession session = request.getSession();
        String checkCodeGen = (String) session.getAttribute("checkCodeGen");
        //防止空指针
        if (checkCodeGen.equalsIgnoreCase(checkCode)){
            Teacher result = teacherService.login(teacher);
            if (result != null){
                session.setAttribute("teacher",JSON.toJSONString(result));
                return "success" ;
            }
        }else{
            return "验证码错误";
        }
        return "用户名或密码错误";

    }


    //登录态获取
    @RequestMapping("/checkLogin")
    public String checkLogin(HttpSession session){
        return (String)session.getAttribute("teacher");
    }


    @RequestMapping("/selectByCondition")
    public String selectByCondition(@RequestBody Teacher teacher, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        String jsonStr = teacherService.selectByCondition(teacher, currentPage, pageSize);
        return jsonStr;
    }

    @RequestMapping("/update")
    public String update(@RequestBody Teacher teacher,@RequestParam("flag") String oldId) {
        System.out.println(oldId);
        return teacherService.update(teacher,oldId);
    }


    @RequestMapping("deleteByIds")
    public String delete(@RequestBody int[] ids) {
        teacherService.deleteByIds(ids);
        return "success";
    }



    @RequestMapping("add")
    public String add(@RequestBody Teacher teacher){
          return teacherService.add(teacher);
    }

}
