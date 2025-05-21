package com.controller.mybatisplus;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.dao.StudentPlusMapper;
import com.entity.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller_Wrapper_Select_ChainWrapper {
    @Autowired
    private StudentPlusMapper studentPlusMapper;

    /**
     * Lambda and 查询
     */
    @GetMapping(value = "/mybatis/plus/select/chain/wrapper1")
    public String wrapper() {
        LambdaQueryChainWrapper<Student> chainWrapper = new LambdaQueryChainWrapper<>(studentPlusMapper);
        List<Student> allList = chainWrapper.list();
        return JSON.toJSONString(allList);
    }
}
