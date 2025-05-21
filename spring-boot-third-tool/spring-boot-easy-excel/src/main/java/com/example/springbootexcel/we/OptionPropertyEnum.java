
package com.example.springbootexcel.we;

import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * The enum Option property enum.
 *
 * @author chenlunwei
 * @date 2021 /1/23
 */
@Getter
public enum OptionPropertyEnum {

    /**
     * 参数key
     */
    TEMP_ID("tempId"),
    UUID("uuid"),
    IMAGE_URL("imageUrl")
    ;

    private final String propertyKey;

    OptionPropertyEnum(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    /**
     * 校验option property
     *
     * @param propertyKey property key
     * @return boolean
     */
    public static boolean validatePropertyType(String propertyKey) {
        if (StringUtils.isEmpty(propertyKey)) {
            return false;
        }
        for (OptionPropertyEnum type : OptionPropertyEnum.values()) {
            if (propertyKey.equals(type.getPropertyKey())) {
                return true;
            }
        }
        return false;
    }
}
