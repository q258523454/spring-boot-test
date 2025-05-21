
package com.example.springbootexcel.we;

import lombok.Getter;

/**
 * ExcelImportErrorCode
 */
@Getter
public enum ExcelImportErrorCode {
    /**
     * Excel导入表单报错
     */
    INNER_ERROR(59000, "Excel导入表单报错", "Error importing form via excel"),

    /**
     * Excel导入表单报错
     */
    COLUMN_TRANS_ERROR(59001, "第{0}行存在列值转换失败,请修改后重试.", "Column value conversion failed in line {0}. Please modify it and try again"),

    /**
     * Excel导入表单报错
     */
    READ_FILE_ERROR(59002, "从文件uuid:{0}读取文件失败", "file uuid:{0},Failed to read the file"),

    /**
     * 输入的题型不正确
     */
    TITLE_TYPE_ERROR(59003, "列名:{0},值:{1},题型不正确.", "column name:{0}, value:{1}, incorrect question type."),

    /**
     * 选项超过最大值
     */
    BEYOND_MAX_OPTION_COUNT(59004, "选项超过最大值:{0}", "The option exceeds maximum value number:{0}"),

    /**
     * 单选题和多选题的选项不能为空
     */
    CHOOSE_MUST_NOT_NULL(59005, "单选题和多选题的选项不能为空", "Single choice and multiple choice options cannot be left blank"),

    /**
     * 多选题的选项必须大于2
     */
    CHOICE_MORE_THAN_TWO(59006, "选择题的选项必须大于2,当前数{0}", "Choice options must be greater than 2,current value:{0}"),

    /**
     * 标题与模板不匹配.
     */
    TITLE_NOT_MATCH(59007, "标题与模板不匹配,请勿修改模板.", "The title does not match the template,do not modify the template"),

    /**
     * 标题与模板不匹配.
     */
    EASY_EXCEL_CONVERT_ERROR(59008, "内部数据转换报错", "Internal data conversion error"),

    /**
     * 标题与模板不匹配.
     */
    INVOKE_DATA_INNER_ERROR(59009, "读取行数据,系统异常", "read line data, system error"),

    /**
     * 选择题的选项必须连续
     */
    CHOICE_MUST_SEQUENTIAL(59010, "选择题的选项必须连续", "choice questions must be sequential"),

    /**
     * 模板标题不能为空
     */
    TITLE_MUST_NOT_EMPTY(59011, "模板标题不能为空", "title cannot be empty"),

    /**
     * 超过最大行数 150
     */
    ROW_NUMBER_TOO_LARGE(59012, "超过最大行数:{0}", "Exceeded the maximum number of rows:{0}"),

    /**
     * 问题标题超过最大长度 150
     */
    QUESTION_TITLE_TOO_LONG(59013, "问题标题超过最大长度:{0}", "The question title exceeds the maximum length:{0}"),

    /**
     * 表单标题超过最大长度 50
     */
    FORM_TITLE_TOO_LONG(59014, "表单标题超过最大长度:{0}", "The form title exceeds the maximum length:{0}"),

    /**
     * 导入内容不能为空
     */
    ROWS_MUST_NOT_EMPTY(59015, "导入内容不能为空", "The import content cannot be empty"),

    /**
     * 问题的题目不能为空
     */
    QUESTION_TITLE_MUST_NOT_EMPTY(59016, "列名:{0},值:{1},问题的题目不能为空", "column name:{0}, value:{1},The title of the question cannot be empty"),

    /**
     * 超过最大行数 150
     */
    OPTION_LENGTH_ERROR(59017, "选项内容超过最大长度:{0}", "The option content exceeds the maximum length:{0}"),

    /**
     * 表单标题超过最大长度 50
     */
    ROW_DATA_MUST_SEQUENCE(59018, "Excel数据行必须连续", "Excel data rows must be contiguous"),

    /**
     * 禁止使用合并单元格
     */
    NO_MERGE_CELLS(59019, "禁止使用合并单元格", "Do not use merge cells"),

    /**
     * 格式有问题
     */
    EXCEL_FORMAT_ERROR(59994, "Excel格式错误", "Excel format error"),

    // ------------------------------------------ 统一格式 BEGIN------------------------------------------
    /**
     * 格式化报错信息1:没有行号,没有列号
     */
    FORMAT_ERROR_MSG_ONLY(59995, "信息:'{'{0}'}'", "error message:'{'{0}'}'"),

    /**
     * 格式化报错信息2:只有行号,信息
     */
    FORMAT_ERROR_MSG_ROW(59996, "第{0}行,信息:'{'{1}'}'", "Row:{0},error message:'{'{1}'}'"),

    /**
     *
     * 格式化报错信息3:行号,列号,信息
     */
    FORMAT_ERROR_MSG_ALL(59997, "第{0}行,第{1}列,信息:'{'{2}'}'", "Row:{0},column:{1},error message:'{'{2}'}'"),
    // ------------------------------------------ 统一格式 END ------------------------------------------

    /**
     * 未知异常
     */
    SYSTEM_UNKNOWN_ERROR(59998, "未知异常", "system unknown error"),

    /**
     * 系统内部错误
     */
    SYSTEM_INNER_ERROR(59999, "系统内部错误", "system inner error"),
    ;

    private final Integer code;

    private final String message;

    private final String messageEn;

    ExcelImportErrorCode(Integer code, String message, String messageEn) {
        this.code = code;
        this.message = message;
        this.messageEn = messageEn;
    }
}
