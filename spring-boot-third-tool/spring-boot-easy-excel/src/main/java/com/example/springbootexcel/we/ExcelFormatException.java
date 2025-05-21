
package com.example.springbootexcel.we;

import lombok.Getter;

/**
 * ExcelFormatException
 */
@Getter
public class ExcelFormatException extends ExcelImportException {

    public ExcelFormatException(Integer code, String message, String messageEn) {
        super(code, message, messageEn);
    }

    public ExcelFormatException(ExcelImportErrorCode errorCode) {
        super(errorCode);
    }

    public ExcelFormatException(ExcelImportErrorCode errorCode, String message, String messageEn) {
        super(errorCode, message, messageEn);
    }

    public ExcelFormatException(Integer code, Integer rowIndex, Integer columnIndex, String message, String messageEn) {
        super(code, rowIndex, columnIndex, message, messageEn);
    }
}
