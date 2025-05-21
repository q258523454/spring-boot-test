
package com.example.springbootexcel.we;

import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * The enum Question property enum.
 *
 * @author chenlunwei
 * @date 2021 /1/23
 */
@Getter
public enum QuestionPropertyEnum {

    /**
     * 参数key
     */
    OTHER_CHOICE("otherChoice"),
    /**
     * Multi choice max count question property enum.
     */
    MULTI_CHOICE_MAX_COUNT("multiChoiceMaxCount"),
    /**
     * Multi choice min count question property enum.
     */
    MULTI_CHOICE_MIN_COUNT("multiChoiceMinCount"),
    /**
     * Score question property enum.
     */
    SCORE("score"),
    /**
     * Example file question property enum.
     */
    EXAMPLE_FILE("exampleFile"),
    /**
     * Area format question property enum.
     */
    AREA_FORMAT("areaFormat"),
    /**
     * Time format question property enum.
     */
    TIME_FORMAT("timeFormat"),
    /**
     * Input type question property enum.
     */
    INPUT_TYPE("inputType"),
    /**
     * Temp id question property enum.
     */
    TEMP_ID("tempId"),
    /**
     * Maximum question property enum.
     */
    MAXIMUM("maximum"),
    /**
     * Minimum question property enum.
     */
    MINIMUM("minimum"),
    /**
     * NumberType question property enum.
     */
    NUMBER_TYPE("numberType"),
    /**
     * Verification question property enum.
     */
    VERIFICATION("verification"),
    /**
     * PictureNumber question property enum.
     */
    PICTURE_NUMBER("pictureNumber"),
    /**
     * ScoreNumber question property enum.
     */
    SCORE_NUMBER("scoreNumber"),
    /**
     * ConditionSet question property enum.
     */
    CONDITION_SET("conditionSet"),
    /**
     * RankType question property enum.
     */
    RANK_TYPE("rankType"),
    /**
     * OnlyPhoto question property enum.
     */
    ONLY_PHOTO("onlyPhoto"),
    /**
     * CheckNumber question property enum.
     */
    CHECK_NUMBER("checkNumber"),
    /**
     * RandomChange question property enum.
     */
    RANDOM_CHANGE("randomChange"),
    /**
     * UserInfoSwitch question property enum.
     */
    USER_INFO_SWITCH("userInfoSwitch"),
    /**
     * CountLimit question property enum.
     */
    COUNT_LIMIT("countlimit"),
    /**
     * HighScore question property enum.
     */
    HIGH_SCORE("highScore"),
    /**
     * LowScore question property enum.
     */
    LOW_SCORE("lowScore"),
    /**
     * SwitchMumChange question property enum.
     */
    SWITCH_MUM_CHANGE("switchMumChange"),
    /**
     * checkMaxNumber
     */
    CHECK_MAX_NUMBER("checkMaxNumber"),

    /**
     * 填空题填写内容类型,类型在FillContentTypeEnum中定义
     */
    FILL_CONTENT_TYPE("fillContentType"),

    /**
     * 行程码是否校验手机号
     */
    PHONE_NUMB_CHECK("phoneNumbCheck"),

    /**
     * 行程码是否校验更新时间
     */
    UPDATE_TIME_LIMIT("updateTimeLimit"),

    /**
     * 行程码更新时间
     */
    UPDATE_TIME("updateTime"),

    /**
     * 行程码颜色
     */
    COLOR("color"),

    /**
     * 行程码途径城市是否带星
     */
    IS_STAR("isStar"),

    /**
     * 行程码途径的全部城市
     */
    PATHWAY("pathway"),

    /**
     * 行程码途径带星城市
     */
    PATHWAY_STAR("pathwayStar"),

    /**
     * 校验上传的是否是本人的行程码
     */
    IS_SELF("isSelf"),

    /**
     * 行程码的更新时间距离当前时间的日期范围
     */
    DATE_RANGE("dateRange"),

    /**
     * 健康码时间
     */
    DATE("date"),

    /**
     * 健康码姓名
     */
    USERNAME("userName"),
    ;

    private final String propertyKey;

    QuestionPropertyEnum(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    /**
     * 校验问题参数propertyType
     *
     * @param propertyType 参数类型
     * @return boolean boolean
     */
    public static boolean validatePropertyType(String propertyType) {
        if (StringUtils.isEmpty(propertyType)) {
            return false;
        }
        for (QuestionPropertyEnum type : QuestionPropertyEnum.values()) {
            if (propertyType.equals(type.getPropertyKey())) {
                return true;
            }
        }
        return false;
    }
}
