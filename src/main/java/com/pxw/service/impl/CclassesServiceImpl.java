package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.CclassesMapper;
import com.pxw.mapper.ClassesMapper;
import com.pxw.pojo.Cclasses;
import com.pxw.pojo.Classes;
import com.pxw.service.CclassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CclassesServiceImpl implements CclassesService {

    @Autowired
    private CclassesMapper cclassesMapper;

    @Override
    public String selectByCondition(Cclasses cclasses, Integer currentPage, Integer pageSize) {
        //分页 按条件查询
        PageHelper.startPage(currentPage, pageSize);
        List<Cclasses> Cclassess = cclassesMapper.selectByCondition(cclasses);
        //分页信息
        PageInfo<Cclasses> ClassesPageInfo = new PageInfo<>(Cclassess);

        return JSON.toJSONString(ClassesPageInfo);
    }


    /**
     * @param oldCclasses
     * @return
     */
    @Override
    public String update(Cclasses oldCclasses) {
        //判断是否已经修改
        Cclasses cclasses1 = cclassesMapper.selectById(oldCclasses.getId());
        //已经修改
        if (! cclasses1.equals(oldCclasses)){
            //判断是否已存在
            List<Cclasses> cclasses = cclassesMapper.selectByName(oldCclasses.getClassesName());
            for(Cclasses cclassesSon : cclasses){
                if (oldCclasses.equals(cclassesSon)){
                    return "fail";
                }
            }
        }
        cclassesMapper.update(oldCclasses);
        return "success";
    }

    @Override
    public void deleteByIds(int[] ids) {
        cclassesMapper.deleteByIds(ids);
    }

    @Override
    public String add(Cclasses newClasses) {
        //去掉最后的逗号
        String classesName = newClasses.getClassesName();
        String substring = classesName.substring(0, classesName.length() - 1);
        newClasses.setClassesName(substring);
        //判断是否已存在
        List<Cclasses> cclasses = cclassesMapper.selectByCondition(newClasses);
        for(Cclasses cclassesSon : cclasses) {
            if (newClasses.equals(cclassesSon)) {
                return "fail";
            }
        }
        cclassesMapper.add(newClasses);
        return "success";
    }

}
