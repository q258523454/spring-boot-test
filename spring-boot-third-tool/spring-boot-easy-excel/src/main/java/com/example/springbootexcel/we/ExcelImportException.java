
package com.example.springbootexcel.we;

import lombok.Getter;

@Getter
public class ExcelImportException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 行号
     */
    private final Integer rowIndex;
    /**
     * 列号
     */
    private final Integer columnIndex;

    /**
     * code
     */
    private final Integer code;

    /**
     * 报错信息 英文
     */
    private final String messageEn;

    /**
     * 构造函数
     * @param code code
     * @param message message
     * @param messageEn messageEn
     */
    public ExcelImportException(Integer code, String message, String messageEn) {
        super(message);
        this.messageEn = messageEn;
        this.code = code;
        this.columnIndex = null;
        this.rowIndex = null;
    }

    /**
     * 构造函数
     * @param errorCode errorCode
     */
    public ExcelImportException(ExcelImportErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), errorCode.getMessageEn());
    }

    /**
     * Instantiates a new Form exception.
     * @param errorCode errorCode
     * @param message message
     * @param messageEn messageEn
     */
    public ExcelImportException(ExcelImportErrorCode errorCode, String message, String messageEn) {
        this(errorCode.getCode(), message, messageEn);
    }


    /**
     * 构造函数
     * @param code code
     * @param rowIndex rowIndex
     * @param columnIndex columnIndex
     * @param message message
     * @param messageEn messageEn
     */
    public ExcelImportException(Integer code, Integer rowIndex, Integer columnIndex, String message, String messageEn) {
        super(message);
        this.messageEn = messageEn;
        this.code = code;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

}
