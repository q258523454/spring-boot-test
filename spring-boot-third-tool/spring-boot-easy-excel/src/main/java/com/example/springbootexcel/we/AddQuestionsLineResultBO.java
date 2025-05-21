
package com.example.springbootexcel.we;

import lombok.Data;

import java.io.Serializable;

/**
 * 从Excel导入表单 业务类
 */
@Data
public class AddQuestionsLineResultBO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    private boolean hasSuccess;

    /**
     * 不成功的话,错误信息
     */
    private String errorMsg;

    /**
     * 不成功的话,错误信息
     */
    private String errorMsgEn;

}
