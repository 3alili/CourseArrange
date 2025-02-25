package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.ClassesMapper;
import com.pxw.pojo.Classes;
import com.pxw.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ClassesServiceImpl implements ClassesService {

    @Autowired
    private ClassesMapper classesMapper;

    @Override
    public String selectByCondition(Classes classes, Integer currentPage, Integer pageSize) {
        //分页 按条件查询
        PageHelper.startPage(currentPage, pageSize);
        List<Classes> Classess = classesMapper.selectByCondition(classes);
        //分页信息
        PageInfo<Classes> ClassesPageInfo = new PageInfo<>(Classess);

        return JSON.toJSONString(ClassesPageInfo);
    }


    /**
     *
     * @param Classes
     * @param flag 名称是否修改
     * @return
     */
    @Override
    public String update(Classes Classes, boolean flag) {
        //名称修改了 判断是否重名
        if (flag){
            if (classesMapper.selectByName(Classes.getClassesName()) != null) {
                return "fail";
            } else {
                classesMapper.update(Classes);
                return "success";
            }
        }else {
            classesMapper.update(Classes);
            return "success";
        }



    }

    @Override
    public void deleteByIds(int[] ids) {
        classesMapper.deleteByIds(ids);
    }

    @Override
    public String add(Classes NewClasses) {

        if (classesMapper.selectByName(NewClasses.getClassesName()) != null) {
            return "false";
        } else {
            classesMapper.add(NewClasses);
            return "success";
        }

    }

}
