package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.CteacherMapper;
import com.pxw.pojo.Course;
import com.pxw.pojo.Cteacher;
import com.pxw.pojo.Teacher;
import com.pxw.service.CteacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CteacherServiceImpl implements CteacherService {

    @Autowired
    private CteacherMapper cteacherMapper;

    @Override
    public String selectByCondition(Cteacher cteacher, Integer currentPage, Integer pageSize) {
        //分页 按条件查询
        PageHelper.startPage(currentPage, pageSize);
        List<Cteacher> cteachers = cteacherMapper.selectByCondition(cteacher);
        //分页信息
        PageInfo<Cteacher> cteacherPageInfo = new PageInfo<>(cteachers);

        return JSON.toJSONString(cteacherPageInfo);
    }


    @Override
    public String update(Cteacher newCteacher) {

        //判断是否修改
        Cteacher oldCteacher = cteacherMapper.selectById(newCteacher.getId());
        System.out.println(oldCteacher);
        System.out.println(newCteacher);
        if (! oldCteacher.equals(newCteacher)){
            //已修改 判断是否重复
            if (cteacherMapper.selectByCondition(newCteacher).size()!=0){
                return "fail";
            }
        }
        cteacherMapper.update(newCteacher);
        return "success";
    }

    @Override
    public void deleteByIds(int[] ids) {
        cteacherMapper.deleteByIds(ids);
    }

    @Override
    public String add(Cteacher[] cteachers) {
        for(Cteacher cteacher:cteachers){
            //判断课程是否已存在
            if (cteacherMapper.selectByCondition(cteacher).size() != 0 ){
                return "fail";
            }
        }
        //若不存在则添加
        for(Cteacher cteacher:cteachers){
            cteacherMapper.add(cteacher);
        }
        return "success";
    }

    @Override
    public String selectTeachreByCourse(Course course) {
        List<Teacher> teachers = cteacherMapper.selectTeachreByCourse(course);
        String jsonString = JSON.toJSONString(teachers);

        return jsonString ;
    }

}
