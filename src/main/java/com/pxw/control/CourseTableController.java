package com.pxw.control;

import com.pxw.pojo.CourseTable;
import com.pxw.pojo.Task;
import com.pxw.service.CourseTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pxw on 2022/4/22 16:02
 *
 * @author pxw
 */


@RestController
@RequestMapping("courseTable")
public class CourseTableController {

    @Autowired
    private CourseTableService courseTableService;

    @RequestMapping("selectAll")
    public String selectAll(@RequestParam Integer currentPage, @RequestParam Integer pageSize){
        return courseTableService.selectAll(currentPage,pageSize);
    }

    @RequestMapping("selectByCondition")
    public String selectByCondition(@RequestBody CourseTable courseTable, @RequestParam Integer currentPage, @RequestParam Integer pageSize){
        System.out.println(courseTable.getTask().getTeacher());
        return courseTableService.selectByCondition(courseTable,currentPage,pageSize);
    }

    @RequestMapping("selectEmptyRoom")
    public String selectEmptyRoom(@RequestBody CourseTable courseTable, @RequestParam Integer currentPage, @RequestParam Integer pageSize){
        return courseTableService.selectEmptyRoom(courseTable,currentPage,pageSize);
    }

    @RequestMapping("selectTask")
    public String selectTask(@RequestBody Task task,@RequestParam Integer currentPage, @RequestParam Integer pageSize){
        return courseTableService.selectTask(task,currentPage,pageSize);
    }

    @RequestMapping("selectTable")
    public String selectByTable(@RequestBody CourseTable courseTable){
        return courseTableService.selectTable(courseTable);
    }


    @RequestMapping("deleteByIds")
    public String delete(@RequestBody int[] ids) {
        courseTableService.deleteByIds(ids);
        return "success";
    }

    @RequestMapping("add")
    public String add(@RequestBody CourseTable courseTable){
        return courseTableService.add(courseTable);
    }

    @RequestMapping("update")
    public String update(@RequestBody CourseTable courseTable){
        return courseTableService.update(courseTable);
    }


    @RequestMapping("reArrangeCourse")
    public String reArrangeCourse(){
        return courseTableService.GA();
    };



}
