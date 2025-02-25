package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.TaskMapper;
import com.pxw.pojo.Task;
import com.pxw.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pxw on 2022/4/22 16:04
 *
 * @author pxw
 */

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public String selectAll(Integer currentPage, Integer pageSize) {
        //分页 按条件查询
        PageHelper.startPage(currentPage, pageSize);
        List<Task> tasks = taskMapper.selectAll();

        //分页信息
        PageInfo<Task> taskPageInfo = new PageInfo<>(tasks);
        System.out.println(taskPageInfo);
        //避免重复引用 $ref 前端无数据显示
        return JSON.toJSONString(taskPageInfo, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public String selectByCondition(Task task, Integer currentPage, Integer pageSize) {
        //分页 按条件查询
        PageHelper.startPage(currentPage, pageSize);
        List<Task> tasks = taskMapper.selectByCondition(task);

        //分页信息
        PageInfo<Task> taskPageInfo = new PageInfo<>(tasks);
        System.out.println(taskPageInfo);
        //避免重复引用 $ref 前端无数据显示
        return JSON.toJSONString(taskPageInfo, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public void deleteByIds(int[] ids) {
        taskMapper.deleteByIds(ids);
    }

    @Override
    public String update(Task newTask) {
        Task oldTask = taskMapper.selectById(newTask.getId());
        System.out.println(oldTask);
        //判断是否修改
        if (!newTask.equals(oldTask)) {
            //判断是否重复
            if (taskMapper.selectByCondition2(newTask) != null) {
                return "fail";
            }
        }
        taskMapper.update(newTask);
        return "success";
    }

    @Override
    public String add(Task task) {
        //判断是否重复
        if (taskMapper.selectByCondition2(task) != null) {
            return "fail";
        }
        taskMapper.add(task);
        return "success";
    }

}
