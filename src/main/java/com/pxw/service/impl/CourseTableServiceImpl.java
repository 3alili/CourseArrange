package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.CourseTableMapper;
import com.pxw.mapper.RoomMapper;
import com.pxw.mapper.TaskMapper;
import com.pxw.pojo.CourseTable;
import com.pxw.pojo.GA.GA;
import com.pxw.pojo.GA.Population;
import com.pxw.pojo.GA.Bootstrap;
import com.pxw.pojo.Room;
import com.pxw.pojo.Task;
import com.pxw.pojo.Timeslot;
import com.pxw.service.CourseTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pxw on 2022/4/24 14:48
 *
 * @author pxw
 */

@Service
public class CourseTableServiceImpl implements CourseTableService {

    @Autowired
    private CourseTableMapper courseTableMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private TaskMapper taskMapper;


    @Override
    public String selectAll(Integer currentPage, Integer pageSize) {
        return null;
    }

    @Override
    public String selectByCondition(CourseTable courseTable, Integer currentPage, Integer pageSize) {
        //分页 按条件查询
        PageHelper.startPage(currentPage, pageSize);
        List<CourseTable> courseTables = courseTableMapper.selectByCondition(courseTable);
        System.out.println(courseTables.toString());

        //分页信息
        PageInfo<CourseTable> courseTablePageInfo = new PageInfo<>(courseTables);
        System.out.println(courseTablePageInfo);

        //避免重复引用 $ref 前端无数据显示
        return JSON.toJSONString(courseTablePageInfo, SerializerFeature.DisableCircularReferenceDetect);

    }

    @Override
    public String selectEmptyRoom(CourseTable courseTable, Integer currentPage, Integer pageSize) {

        //分页 按条件查询
        PageHelper.startPage(currentPage, pageSize);
        List<CourseTable> courseTables = courseTableMapper.selectEmptyRoom(courseTable);

        //分页信息
        PageInfo<CourseTable> courseTablePageInfo = new PageInfo<>(courseTables);
        System.out.println(courseTablePageInfo);
        //避免重复引用 $ref 前端无数据显示
        return JSON.toJSONString(courseTablePageInfo, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public String selectTable(CourseTable courseTable) {
        //查询条件为空，不调用查询。
        if ( (null == courseTable.getTask().getCclasses().getClassesName() ||
                courseTable.getTask().getCclasses().getClassesName().equals("")) &&
                (courseTable.getTask().getTeacher().getTeacherName()==null ||
                        courseTable.getTask().getTeacher().getTeacherName().equals(""))&&
                (courseTable.getRoom().getRoomName()==null||
                        courseTable.getRoom().getRoomName().equals("")))
            return "";
        //条件查询
        List<CourseTable> courseTables = courseTableMapper.selectTable(courseTable);
        String jsonString = JSON.toJSONString(courseTables, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(jsonString);
        return jsonString;

    }

    @Override
    public String selectTask(Task task,Integer currentPage,Integer pageSize) {
        //分页 按条件查询
        PageHelper.startPage(currentPage, pageSize);
        List<Task> tasks = courseTableMapper.selectTask(task);

        //分页信息
        PageInfo<Task> taskPageInfo = new PageInfo<>(tasks);
        System.out.println(taskPageInfo);
        //避免重复引用 $ref 前端无数据显示
        return JSON.toJSONString(taskPageInfo, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public void deleteByIds(int[] ids) {
        courseTableMapper.deleteByIds(ids);
    }

    @Override
    public String update(CourseTable courseTable) {

        String result = isClash(courseTable);
         if (result.equals("success")){
             courseTableMapper.update(courseTable);
             return "success";
         }else {
             return result;
         }


    }

    @Override
    public String add(CourseTable courseTable) {
        String result = isClash(courseTable);
        if (result.equals("success")){
            courseTableMapper.addCourseTable(courseTable);
            return "success";
        }else {
            return result;
        }
    }

    public String isClash(CourseTable courseTable){
        //要判断是否冲突
        //班级时间冲突
        String[] split = courseTable.getTask().getCclasses().getClassesName().split(",");
        Integer timeId = courseTable.getTimeslot().getId();
        for (String classesName : split){
            //查询的结果为空 但是类已经创建不为null 应当判断size
            if (courseTableMapper.selectByCondition2(classesName, timeId, null).size() != 0){
                return "操作失败，选中结果与现有排课冲突！(班级时间)";
            }
        }
        Integer teacherId = courseTable.getTask().getTeacher().getId();
        //教师时间冲突
        if (courseTableMapper.selectByCondition2(null,timeId,teacherId).size() != 0 ){
            return "操作失败，选中结果与现有排课冲突！(教师时间)";
        }

        return "success";
    }

    //排课算法
    @Override
    public String GA(){

        //初始化课表管理类
        List<Room> rooms = roomMapper.selectAll();
        List<Task> tasks = taskMapper.selectAll();
        List<Timeslot> timeslots = courseTableMapper.selectAllTimeslot();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.setRooms(rooms);
        bootstrap.setTimeslots(timeslots);
        bootstrap.setTasks(tasks);

        //初始化GA(种群数，变异率，交叉率，精英个体数，锦标赛个体数)
        GA ga = new GA(100, 0.01, 0.9, 2, 5);
        //初始化种群
        Population population = ga.initPopulation(bootstrap);
        //计算种群适应度
        ga.evalPopulationFitness(population, bootstrap);
        //追踪代数
        int generation = 1;
        //排课结果标志
        boolean flag = false;

        //开始进化循环
        //添加终止条件  达到最佳适应度 或达到最大代数
        while (!ga.isEnd(generation, 1000) &&
                !ga.isEnd(population)){

            //打印适应度
            System.out.println("G" +generation +" Best fitness: "+population.getFittest(0).getFitness());

            //交叉  锦标赛 均匀交叉
            population = ga.crossover(population, bootstrap);

            //变异
            population = ga.mutate(population, bootstrap);
            //计算种群适应度
            ga.evalPopulationFitness(population, bootstrap);

            generation++;

        }
        //判断排课结果
        if (population.getFittest(0).getFitness()==1.0) {
            flag=true;
        }
        //打印最终适应度
        //创建课表
        bootstrap.createTable(population.getFittest(0));
        System.out.println();
        System.out.println("Solution found in "+generation +" generations");
        System.out.println("Final solution fitness: "+population.getFittest(0).getFitness());
        System.out.println("Clashes: "+ bootstrap.calcClashes());
        System.out.println();

        //排课成功
        if (flag){
            //插入数据库
            //删除之前的排课结果
            courseTableMapper.deleteAll();
            CourseTable[] tables = bootstrap.getTables();
            courseTableMapper.addCourseTables(tables);

            return "success";
        }

        return "fail";


    }


}
