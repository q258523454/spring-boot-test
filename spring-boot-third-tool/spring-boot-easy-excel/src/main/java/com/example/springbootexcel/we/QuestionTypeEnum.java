
package com.example.springbootexcel.we;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The enum Question type enum.
 *
 * @author chenlunwei
 * @date 2021 /1/26
 */
@Getter
public enum QuestionTypeEnum {

    /**
     * 单选题
     */
    SINGLE_CHOICE("single-choice"),

    /**
     * 多选
     */
    MULTIPLE_CHOICE("multiple-choice"),

    /**
     * 填空
     */
    FILL("fill"),

    /**
     * 表格单选
     */
    MATRIX_SINGLE_CHOICE("matrix-single-choice"),

    /**
     * 表格多选
     */
    MATRIX_MULTIPLE_CHOICE("matrix-multiple-choice"),

    /**
     * 评分
     */
    SCORE("score"),

    /**
     * 图片
     */
    PICTURE("picture"),

    /**
     * 附件
     */
    ATTACHMENT("attachment"),

    /**
     * 省市区
     */
    AREA("area"),

    /**
     * 日期
     */
    DATE("date"),

    /**
     * 地址
     */
    LOCATION("location"),

    /**
     * 数字
     */
    NUMBER("number"),

    /**
     * 手机号码
     */
    PHONE("phone"),

    /**
     * 邮箱
     */
    EMAIL("email"),

    /**
     * 排序
     */
    SORT("sort"),

    /**
     * 性别
     */
    GENDER("gender"),

    /**
     * 表格填空
     */
    MATRIX_FILL("matrix-fill"),

    /**
     * 链接
     */
    URL("url"),

    /**
     * 文字描述
     */
    DESCRIPTION("description"),

    /**
     * 部门
     */
    DEPARTMENT("department"),

    /**
     * 人员
     */
    PERSON("person"),

    /**
     * 手写签名
     */
    SIGNATURE("signature"),

    /**
     * 影藏题目类型
     */
    HIDE("hide"),

    /**
     * 行程码题目类型
     */
    TRIP_CODE("trip-code"),

    /**
     * 行程码2.0
     */
    OCR_IDENTIFY("ocrIdentify-tripcode"),

    /**
     * 健康码
     */
    HEALTH_CODE("ocrIdentify-healthcode")
    ;

    private final String questionType;

    QuestionTypeEnum(String questionType) {
        this.questionType = questionType;
    }

    /**
     * Fill type list.
     *
     * @return the list
     */
    public static List<String> fillType() {
        List<String> list = new ArrayList<>();
        list.add(FILL.questionType);
        list.add(PICTURE.questionType);
        list.add(SIGNATURE.questionType);
        list.add(ATTACHMENT.questionType);
        list.add(AREA.questionType);
        list.add(DATE.questionType);
        list.add(LOCATION.questionType);
        list.add(NUMBER.questionType);
        list.add(PHONE.questionType);
        list.add(EMAIL.questionType);
        list.add(SORT.questionType);
        list.add(URL.questionType);
        list.add(MATRIX_FILL.questionType);
        list.add(DESCRIPTION.questionType);
        list.add(DEPARTMENT.questionType);
        list.add(PERSON.questionType);
        list.add(HIDE.questionType);
        list.add(TRIP_CODE.questionType);
        list.add(OCR_IDENTIFY.questionType);
        list.add(HEALTH_CODE.questionType);
        return list;
    }

    /**
     * Choice type list.
     *
     * @return the list
     */
    public static List<String> choiceType() {
        List<String> list = new ArrayList<>();
        list.add(SINGLE_CHOICE.questionType);
        list.add(MULTIPLE_CHOICE.questionType);
        list.add(GENDER.questionType);
        list.add(SCORE.questionType);
        return list;
    }

    /**
     * Matrix choice type list.
     *
     * @return the list
     */
    public static List<String> matrixChoiceType() {
        List<String> list = new ArrayList<>();
        list.add(MATRIX_SINGLE_CHOICE.questionType);
        list.add(MATRIX_MULTIPLE_CHOICE.questionType);
        return list;
    }

    /**
     * Matrix type list.
     *
     * @return the list
     */
    public static List<String> matrixType() {
        List<String> list = new ArrayList<>();
        list.add(MATRIX_SINGLE_CHOICE.questionType);
        list.add(MATRIX_MULTIPLE_CHOICE.questionType);
        list.add(MATRIX_FILL.questionType);
        return list;
    }

    /**
     * Matrix type list.
     *
     * @return the list
     */
    public static List<String> multipleLine() {
        List<String> list = new ArrayList<>();
        list.add(MATRIX_SINGLE_CHOICE.questionType);
        list.add(MATRIX_MULTIPLE_CHOICE.questionType);
        list.add(MATRIX_FILL.questionType);
        list.add(TRIP_CODE.questionType);
        list.add(OCR_IDENTIFY.questionType);
        list.add(HEALTH_CODE.questionType);
        return list;
    }

    /**
     * number type list.
     *
     * @return the list
     */
    public static List<String> numberType() {
        List<String> list = new ArrayList<>();
        list.add(NUMBER.questionType);
        list.add(SCORE.questionType);
        return list;
    }

    /**
     * File url type list.
     *
     * @return the list
     */
    public static List<String> fileType() {
        List<String> list = new ArrayList<>();
        list.add(PICTURE.questionType);
        list.add(SIGNATURE.questionType);
        list.add(ATTACHMENT.questionType);
        list.add(TRIP_CODE.questionType);
        list.add(OCR_IDENTIFY.questionType);
        list.add(HEALTH_CODE.questionType);
        return list;
    }

    /**
     * 需要导出到onebox的文件类型
     *
     * @return the list
     */
    public static List<String> exportOneBoxFileType() {
        List<String> list = new ArrayList<>();
        list.add(PICTURE.questionType);
        list.add(ATTACHMENT.questionType);
        list.add(TRIP_CODE.questionType);
        list.add(OCR_IDENTIFY.questionType);
        list.add(HEALTH_CODE.questionType);
        return list;
    }
}
