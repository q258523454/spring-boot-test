
package com.example.springbootexcel.we;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 从Excel导入表单 业务类
 */
@Data
public class AddQuestionsBO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 问题-标题
     */
    private String questionTitle;

    /**
     * 问题-提示
     */
    private String questionTip;

    /**
     * 问题-类型
     */
    private String questionType;

    /**
     * 问题-是否必填
     */
    private Boolean isRequired;

    /**
     * 问题-属性
     */
    private List<PropertyVO> properties = new ArrayList<>();


    /**
     * 选项
     */
    private List<OptionReq> options = new ArrayList<>();
}
