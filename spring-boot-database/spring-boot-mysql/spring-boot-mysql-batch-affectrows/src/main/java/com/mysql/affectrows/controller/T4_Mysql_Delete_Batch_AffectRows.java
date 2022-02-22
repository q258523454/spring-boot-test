package com.mysql.affectrows.controller;

import com.mysql.affectrows.serivce.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @date 2020-04-26 11:22
 * @modify
 */
@RestController
public class T4_Mysql_Delete_Batch_AffectRows {
    private static final Logger logger = LoggerFactory.getLogger(T4_Mysql_Delete_Batch_AffectRows.class);

    @Autowired
    private IStudentService studentService;

    /**
     * 删除:delete .. in(..)
     * @param num 从最大值开始，往前删除多少个, 默认一次删除2个
     */
    @GetMapping(value = "/affect/rows/delete/batch")
    public String batch(Integer num) {
        List<String> list = new ArrayList<>();
        if (null == studentService.getMaxId()) {
            return "no data to delete.";
        }
        long maxId = studentService.getMaxId();
        if (num == null) {
            num = 2;
        }
        for (long i = maxId - num + 1; i <= maxId; i++) {
            list.add(String.valueOf(i));
        }

        int i = studentService.deleteByIdList(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }
}
