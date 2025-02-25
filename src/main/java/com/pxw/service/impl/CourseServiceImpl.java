package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.CourseMapper;
import com.pxw.pojo.Course;
import com.pxw.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public String selectByCondition(Course course, Integer currentPage, Integer pageSize) {
        //分页 按条件查询
        PageHelper.startPage(currentPage, pageSize);
        List<Course> courses = courseMapper.selectByCondition(course);
        //分页信息
        PageInfo<Course> coursePageInfo = new PageInfo<>(courses);

        return JSON.toJSONString(coursePageInfo);
    }

    /**
     *
     * @param course
     * @param flag code是否修改
     * @return
     */
    @Override
    public String update(Course course, boolean flag) {
        //编码修改了 判断是否重名
        if (flag){
            if (courseMapper.selectByCode(course.getCode()) != null) {
                return "fail";
            } else {
                courseMapper.update(course);
                return "success";
            }
        }else {
            courseMapper.update(course);
            return "success";
        }



    }

    @Override
    public void deleteByIds(int[] ids) {
        courseMapper.deleteByIds(ids);
    }

    @Override
    public String add(Course NewCourse) {

        if (courseMapper.selectByCode(NewCourse.getCode()) != null) {
            return "false";
        } else {
            courseMapper.add(NewCourse);
            return "success";
        }

    }

}
