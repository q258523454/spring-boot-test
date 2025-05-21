
package com.example.springbootexcel.we;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Excel ReadListener
 */
@Data
@Log4j2
public class QuestionExcelReadListener implements ReadListener<ImportQuestionModel> {
    /**
     * Excel 标题行,即第几行是标题
     */
    private static final int HEAD_INDEX_NUM = 2;

    /**
     * Excel 最多20个选项
     */
    private static final int MAX_OPTION_NUM = 20;

    /**
     * Excel 非选项的列数, 目前就是 题型+题目 2列。 用于计算 选项order
     */
    private static final int NON_OPTION_COLUMN_NUM = 2;

    /**
     * Excel 支持最大行数 150行
     */
    private static final int MAX_ROW_COUNT = 150;

    /**
     * 表单标题字符最大值: 50
     */
    private static final int MAX_FORM_TITLE_LENGTH = 50;

    /**
     * 表单选项最大值: 1024
     */
    private static final int MAX_FORM_OPTION_LENGTH = 1024;

    /**
     * 单选题,也可当做判断题;简单题,就是填空题.
     */
    private static final Map<String, QuestionTypeEnum> TITLE_MAP = new HashMap<>();


    /**
     * 实体类的注解value, 用于校验Excel模板是否与实体类定义的注解列名一致
     */
    private static final List<String> FILED_ANNOTATION_NAME_LIST = new ArrayList<>();


    /**
     * key:字段名; value:注解值(index),即选项的 order
     */
    private static final Map<String, Integer> FILED_ANNOTATION_NAME_INDEX_MAP = new HashMap<>();

    /**
     * 各个属性字段对应的列,用于保存详细日志,定位列
     * key:字段名; value:注解值(value),即Excel表头名
     * 结合 columnHeadMap 使用,获取列号
     */
    private static final Map<String, String> FILED_ANNOTATION_NAME_VALUE_MAP = new HashMap<>();


    static {
        TITLE_MAP.put("单选题", QuestionTypeEnum.SINGLE_CHOICE);
        TITLE_MAP.put("多选题", QuestionTypeEnum.MULTIPLE_CHOICE);
        TITLE_MAP.put("简答题", QuestionTypeEnum.FILL);
        initFiledAnnotationInfo();
    }

    /**
     * 读取Excel的时候,当前表头指针,从第一行开始算.
     */
    private AtomicInteger headIndex = new AtomicInteger(1);

    /**
     * 除去表头,总共有多少行数据
     */
    private AtomicInteger rowCount = new AtomicInteger(0);

    /**
     * 记录上一次非空数据行,用于判断行数据是否连续. 模板第一行从标题的下一行开始,那么上一行默认从标题行开始.
     */
    private AtomicInteger lastRowIndex = new AtomicInteger(HEAD_INDEX_NUM);

    /**
     * 待新增的问题(成功解析Excel后的数据)
     */
    private List<AddQuestionsBO> addQuestionsList = new LinkedList<>();

    /**
     * 失败信息
     */
    private List<AddQuestionsLineResultBO> resultErrorList = new LinkedList<>();

    /**
     * 读取的原始数据
     */
    private List<ImportQuestionModel> importQuestionModelList = new ArrayList<>();

    /**
     * Excel表头 map
     */
    private Map<Integer, ReadCellData<?>> headMap = new HashMap<>();

    /**
     * Excel表头,列名(注解名)对应的列号
     */
    private Map<String, Integer> columnHeadMap = null;


    /**
     * 反射调用初始化 @ExcelProperty 各项注解值
     * {@link QuestionExcelReadListener#FILED_ANNOTATION_NAME_VALUE_MAP}
     */
    private static void initFiledAnnotationInfo() {
        Field[] fields = ImportQuestionModel.class.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof ExcelProperty) {
                    ExcelProperty excelProperty = (ExcelProperty) annotation;
                    String[] value = excelProperty.value();
                    FILED_ANNOTATION_NAME_LIST.add(value[0]);
                    FILED_ANNOTATION_NAME_VALUE_MAP.put(fieldName, value[0]);
                    // 获取选项字段的order, 即表单的选项顺序
                    int index = excelProperty.index();
                    FILED_ANNOTATION_NAME_INDEX_MAP.put(fieldName, index);
                }
            }
        }
    }

    /**
     * 解析 head 行
     *
     * @param headMap headMap
     * @param context context
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        String sheetName = context.readSheetHolder().getSheetName();
        if (!StringUtils.isEmpty(sheetName) && sheetName.length() > MAX_FORM_TITLE_LENGTH) {
            Integer errorCode = ExcelImportErrorCode.FORM_TITLE_TOO_LONG.getCode();
            String errorMsg = MessageFormat.format(ExcelImportErrorCode.FORM_TITLE_TOO_LONG.getMessage(),
                    MAX_FORM_TITLE_LENGTH);
            String errorMsgEn = MessageFormat.format(ExcelImportErrorCode.FORM_TITLE_TOO_LONG.getMessageEn(),
                    MAX_FORM_TITLE_LENGTH);
            throw new ExcelFormatException(errorCode, null, null, errorMsg, errorMsgEn);
        }
        // 只读取指定的表头行
        if (headIndex.intValue() == HEAD_INDEX_NUM) {
            // 检查Excel总行数
            checkExcelRowNum(context);
            this.headMap = headMap;
            // 检查Excel标题行
            checkHeadTitle(headMap);
            columnHeadMap = initColumnHeadMap(this.headMap);
        } else {
            headIndex.addAndGet(1);
        }
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return true;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value.
     * @param context analysis context
     */
    @Override
    public void invoke(ImportQuestionModel data, AnalysisContext context) {
        // 保存读取的原始数据
        importQuestionModelList.add(data);
        // 行号(context从0开始),这里+1
        int rowIndex = context.readRowHolder().getRowIndex() + 1;
        // Excel数据行必须连续
        if (rowIndex != lastRowIndex.intValue() + 1) {
            Integer errorCode = ExcelImportErrorCode.ROW_DATA_MUST_SEQUENCE.getCode();
            String errorMsg = ExcelImportErrorCode.ROW_DATA_MUST_SEQUENCE.getMessage();
            String errorMsgEn = ExcelImportErrorCode.ROW_DATA_MUST_SEQUENCE.getMessageEn();
            throw new ExcelFormatException(errorCode, lastRowIndex.intValue() + 1, null, errorMsg, errorMsgEn);
        }
        lastRowIndex.set(rowIndex);

        checkValidator(data, rowIndex);
        try {
            // 题型
            String type = data.getType();
            QuestionTypeEnum questionTypeEnum = TITLE_MAP.get(type);
            // 输入的题型不正确
            if (null == type || null == questionTypeEnum) {
                String columnName = FILED_ANNOTATION_NAME_VALUE_MAP.get(ImportQuestionModel.Fields.type);
                int columnIndex = columnHeadMap.get(columnName);
                String errorMsg = MessageFormat.format(ExcelImportErrorCode.TITLE_TYPE_ERROR.getMessage(), columnName,
                        data.getType());
                String errorMsgEn = MessageFormat.format(ExcelImportErrorCode.TITLE_TYPE_ERROR.getMessageEn(), columnName,
                        data.getType());
                Integer errorCode = ExcelImportErrorCode.TITLE_TYPE_ERROR.getCode();
                throw new ExcelImportException(errorCode, rowIndex, columnIndex, errorMsg, errorMsgEn);
            }
            String title = data.getTitle();
            // 问题标题不能为空
            if (StringUtils.isEmpty(title)) {
                String columnName = FILED_ANNOTATION_NAME_VALUE_MAP.get(ImportQuestionModel.Fields.title);
                int columnIndex = columnHeadMap.get(columnName);
                String errorMsg = MessageFormat.format(ExcelImportErrorCode.QUESTION_TITLE_MUST_NOT_EMPTY.getMessage(),
                        columnName, title);
                String errorMsgEn = MessageFormat.format(ExcelImportErrorCode.QUESTION_TITLE_MUST_NOT_EMPTY.getMessageEn(),
                        columnName, title);
                Integer errorCode = ExcelImportErrorCode.QUESTION_TITLE_MUST_NOT_EMPTY.getCode();
                throw new ExcelImportException(errorCode, rowIndex, columnIndex, errorMsg, errorMsgEn);
            }

            AddQuestionsBO addQuestionsBO = new AddQuestionsBO();
            addQuestionsBO.setQuestionTitle(title);
            List<PropertyVO> questionPropertiesList = null;
            // 构造 question properties
            if (isSingleChoice(questionTypeEnum)) {
                // 单选
                addQuestionsBO.setQuestionType(QuestionTypeEnum.SINGLE_CHOICE.getQuestionType());
                questionPropertiesList = initSingleChoicePropertiesList();
            } else if (isMultipleChoice(questionTypeEnum)) {
                // 多选
                addQuestionsBO.setQuestionType(QuestionTypeEnum.MULTIPLE_CHOICE.getQuestionType());
                questionPropertiesList = initMultipleChoicePropertiesList();
            } else if (isFill(questionTypeEnum)) {
                // 填空
                addQuestionsBO.setQuestionType(QuestionTypeEnum.FILL.getQuestionType());
                questionPropertiesList = initFillPropertiesList();
            }
            addQuestionsBO.setProperties(questionPropertiesList);

            // 构造 options properties
            List<OptionReq> optionList;
            // 只有选择题才有选项,填空题不处理
            optionList = getOptionList(data, questionTypeEnum, rowIndex);
            addQuestionsBO.setOptions(optionList);
            addQuestionsList.add(addQuestionsBO);
        } catch (ExcelImportException ex) {
            log.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ExcelImportErrorCode.INVOKE_DATA_INNER_ERROR.getMessage(), ex);
            Integer errorCode = ExcelImportErrorCode.INVOKE_DATA_INNER_ERROR.getCode();
            String errorMsg = ExcelImportErrorCode.INVOKE_DATA_INNER_ERROR.getMessage();
            String errorMsgEn = ExcelImportErrorCode.INVOKE_DATA_INNER_ERROR.getMessageEn();
            throw new ExcelImportException(errorCode, rowIndex, null, errorMsg, errorMsgEn);
        }
    }

    /**
     * 读取合并单元信息
     *
     * @param extra   extra information
     * @param context analysis context
     */
    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        if (extra.getType() == CellExtraTypeEnum.MERGE) {
            int rowIndex = extra.getFirstRowIndex() + 1;
            int columnIndex = extra.getFirstColumnIndex() + 1;
            if (rowIndex > HEAD_INDEX_NUM) {
                Integer errorCode = ExcelImportErrorCode.NO_MERGE_CELLS.getCode();
                String errorMsg = ExcelImportErrorCode.NO_MERGE_CELLS.getMessage();
                String errorMsgEn = ExcelImportErrorCode.NO_MERGE_CELLS.getMessageEn();
                throw new ExcelFormatException(errorCode, rowIndex, columnIndex, errorMsg, errorMsgEn);
            }
        }
    }

    /**
     * 每个sheet读取完毕后调用一次
     *
     * @param context context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (importQuestionModelList.size() <= 0) {
            Integer errorCode = ExcelImportErrorCode.ROWS_MUST_NOT_EMPTY.getCode();
            String errorMsg = ExcelImportErrorCode.ROWS_MUST_NOT_EMPTY.getMessage();
            String errorMsgEn = ExcelImportErrorCode.ROWS_MUST_NOT_EMPTY.getMessageEn();
            throw new ExcelFormatException(errorCode, null, null, errorMsg, errorMsgEn);
        }
        log.info(context.readSheetHolder().getSheetName() + "excel data finished!");
    }

    /**
     * 异常处理：抛出异常则停止读取, 否则继续读取下一行
     *
     * @param ex      exception
     * @param context context
     */
    @Override
    public void onException(Exception ex, AnalysisContext context) {
        String errorMsg = "";
        String errorMsgEn = "";
        AddQuestionsLineResultBO result = null;
        ExcelImportException excelImportException = null;
        if (ex instanceof ExcelDataConvertException) {
            ExcelDataConvertException exception = (ExcelDataConvertException) ex;
            Integer rowIndex = exception.getRowIndex();
            Integer columnIndex = exception.getColumnIndex();
            Integer errorCode = ExcelImportErrorCode.EASY_EXCEL_CONVERT_ERROR.getCode();
            errorMsg = ExcelImportErrorCode.EASY_EXCEL_CONVERT_ERROR.getMessage();
            errorMsgEn = ExcelImportErrorCode.EASY_EXCEL_CONVERT_ERROR.getMessageEn();
            excelImportException = new ExcelImportException(errorCode, rowIndex, columnIndex, errorMsg, errorMsgEn);
        } else if (ex instanceof ExcelImportException) {
            // 包括 ExcelFormatException
            excelImportException = (ExcelImportException) ex;
        } else {
            // 未知异常
            log.error(ExcelImportErrorCode.SYSTEM_UNKNOWN_ERROR.getMessage(), ex);
            Integer errorCode = ExcelImportErrorCode.SYSTEM_UNKNOWN_ERROR.getCode();
            errorMsg = ExcelImportErrorCode.SYSTEM_UNKNOWN_ERROR.getMessage();
            errorMsgEn = ExcelImportErrorCode.SYSTEM_UNKNOWN_ERROR.getMessageEn();
            excelImportException = new ExcelImportException(errorCode, null, null, errorMsg, errorMsgEn);
        }
        // 格式化 ExcelImportException 异常, 统一处理
        result = buildErrorMsg(excelImportException);
        log.error(result.getErrorMsg());
        resultErrorList.add(result);

        // Excel格式有问题,直接中断读取, 具体异常信息已经保存在了 resultErrorList
        if (ex instanceof ExcelFormatException) {
            log.error(ExcelImportErrorCode.EXCEL_FORMAT_ERROR.getMessage(), ex);
            throw new ExcelFormatException(ExcelImportErrorCode.EXCEL_FORMAT_ERROR);
        }
    }

    /**
     * 构造报错信息;
     * 有的报错无法定位列号,例如:多选项个数必须大于2, 所以构造的报错信息无法 固定格式.
     *
     * @param exception ExcelImportException
     * @return AddQuestionsLineResultBO
     */
    private AddQuestionsLineResultBO buildErrorMsg(ExcelImportException exception) {
        Integer rowIndex = exception.getRowIndex();
        Integer columnIndex = exception.getColumnIndex();
        if (null == rowIndex) {
            String errorMsg = MessageFormat.format(ExcelImportErrorCode.FORMAT_ERROR_MSG_ONLY.getMessage(),
                    exception.getMessage());
            String errorMsgEn = MessageFormat.format(ExcelImportErrorCode.FORMAT_ERROR_MSG_ONLY.getMessageEn(),
                    exception.getMessageEn());
            return buildAddQuestionsLineResultBO(errorMsg, errorMsgEn, false);
        }
        if (null == columnIndex) {
            String errorMsg = MessageFormat.format(ExcelImportErrorCode.FORMAT_ERROR_MSG_ROW.getMessage(), rowIndex,
                    exception.getMessage());
            String errorMsgEn = MessageFormat.format(ExcelImportErrorCode.FORMAT_ERROR_MSG_ROW.getMessageEn(), rowIndex,
                    exception.getMessageEn());
            return buildAddQuestionsLineResultBO(errorMsg, errorMsgEn, false);
        } else {
            // 行号+列号+报错信息
            String errorMsg = MessageFormat.format(ExcelImportErrorCode.FORMAT_ERROR_MSG_ALL.getMessage(), rowIndex,
                    columnIndex, exception.getMessage());
            String errorMsgEn = MessageFormat.format(ExcelImportErrorCode.FORMAT_ERROR_MSG_ALL.getMessageEn(), rowIndex,
                    columnIndex, exception.getMessageEn());
            return buildAddQuestionsLineResultBO(errorMsg, errorMsgEn, false);
        }
    }

    /**
     * 是否为 单选
     *
     * @param questionTypeEnum questionTypeEnum
     * @return boolean
     */
    private boolean isSingleChoice(QuestionTypeEnum questionTypeEnum) {
        return QuestionTypeEnum.SINGLE_CHOICE.getQuestionType().equals(questionTypeEnum.getQuestionType());
    }

    /**
     * 是否为 多选
     *
     * @param questionTypeEnum questionTypeEnum
     * @return boolean
     */
    private boolean isMultipleChoice(QuestionTypeEnum questionTypeEnum) {
        return QuestionTypeEnum.MULTIPLE_CHOICE.getQuestionType().equals(questionTypeEnum.getQuestionType());
    }

    /**
     * 是否为 填空
     *
     * @param questionTypeEnum questionTypeEnum
     * @return boolean
     */
    private boolean isFill(QuestionTypeEnum questionTypeEnum) {
        return QuestionTypeEnum.FILL.getQuestionType().equals(questionTypeEnum.getQuestionType());
    }

    /**
     * 单选/多选-构造options
     *
     * @param rowData  obj
     * @param type     type
     * @param rowIndex 行号
     * @return return
     * @throws ExcelImportException ExcelImportException
     */
    private List<OptionReq> getOptionList(ImportQuestionModel rowData, QuestionTypeEnum type, int rowIndex) throws ExcelImportException {
        List<OptionReq> optionReqList = new ArrayList<>();
        // 只处理单选题/多选题
        if (!isSingleChoice(type) && !isMultipleChoice(type)) {
            return optionReqList;
        }

        // 获取bean属性
        Map<String, Object> attributeMap = null;
        try {
            attributeMap = BeanToMapUtil.beanToMap(rowData);
        } catch (Exception ex) {
            log.error("Bean to map error:", ex);
            Integer errorCode = ExcelImportErrorCode.SYSTEM_INNER_ERROR.getCode();
            String errorMsg = ExcelImportErrorCode.SYSTEM_INNER_ERROR.getMessage();
            String errorMsgEn = ExcelImportErrorCode.SYSTEM_INNER_ERROR.getMessageEn();
            // 系统错误,直接抛出格式异常,onException会终止继续读取
            throw new ExcelFormatException(errorCode, rowIndex, null, errorMsg, errorMsgEn);
        }

        // 选项值arr
        String[] optionArr = new String[MAX_OPTION_NUM];
        // 选项对应的列名
        String[] optionColumnName = new String[MAX_OPTION_NUM];

        for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
            String attributeName = entry.getKey();
            String attributeValue = (String) entry.getValue();
            // 选项字段,并返回选项顺序,eg: o
            // option3 表示第3个选项,表单的 order=2,跟注解里面的 index 一样, 下标从0开始
            int order = FILED_ANNOTATION_NAME_INDEX_MAP.get(attributeName) - NON_OPTION_COLUMN_NUM;
            // 保存选项字段的order和值
            if (order >= 0) {
                optionColumnName[order] = attributeName;
                optionArr[order] = attributeValue;
            }
        }

        // 非空选项个数
        int optionCount = 0;
        for (int i = 0; i < optionArr.length; i++) {
            String optionValue = optionArr[i];
            // 处理空值
            if (StringUtils.isEmpty(optionValue)) {
                // 如果空选项后存在非空选项,说明选项不连续,格式问题.
                if (existNonEmpty(i + 1, optionArr)) {
                    String columnName = FILED_ANNOTATION_NAME_VALUE_MAP.get(optionColumnName[i]);
                    int columnIndex = columnHeadMap.get(columnName);
                    Integer errorCode = ExcelImportErrorCode.CHOICE_MUST_SEQUENTIAL.getCode();
                    String errorMsg = ExcelImportErrorCode.CHOICE_MUST_SEQUENTIAL.getMessage();
                    String errorMsgEn = ExcelImportErrorCode.CHOICE_MUST_SEQUENTIAL.getMessageEn();
                    throw new ExcelImportException(errorCode, rowIndex, columnIndex, errorMsg, errorMsgEn);
                }
                // 连续的的空值,无需再进行解析,跳过.
                break;
            } else {
                // 选项内容不能超过1024个字符长度
                if (optionValue.length() > MAX_FORM_OPTION_LENGTH) {
                    String columnName = FILED_ANNOTATION_NAME_VALUE_MAP.get(optionColumnName[i]);
                    int columnIndex = columnHeadMap.get(columnName);
                    Integer errorCode = ExcelImportErrorCode.OPTION_LENGTH_ERROR.getCode();
                    String errorMsg = MessageFormat.format(ExcelImportErrorCode.OPTION_LENGTH_ERROR.getMessage(),
                            MAX_FORM_OPTION_LENGTH);
                    String errorMsgEn = MessageFormat.format(ExcelImportErrorCode.OPTION_LENGTH_ERROR.getMessageEn(),
                            MAX_FORM_OPTION_LENGTH);
                    throw new ExcelImportException(errorCode, rowIndex, columnIndex, errorMsg, errorMsgEn);
                }
                OptionReq optionReq = buildOptionByOrderAndAttAndType(i, optionValue, type);
                optionReqList.add(optionReq);
                optionCount++;
            }
        }

        // 单选题和多选题的选项不能为空
        if (optionCount == 0) {
            Integer errorCode = ExcelImportErrorCode.CHOOSE_MUST_NOT_NULL.getCode();
            String errorMsg = ExcelImportErrorCode.CHOOSE_MUST_NOT_NULL.getMessage();
            String errorMsgEn = ExcelImportErrorCode.CHOOSE_MUST_NOT_NULL.getMessageEn();
            throw new ExcelImportException(errorCode, rowIndex, null, errorMsg, errorMsgEn);
        }

        // 多选题,选项必须大于1
        if (optionCount <= 1) {
            Integer errorCode = ExcelImportErrorCode.CHOICE_MORE_THAN_TWO.getCode();
            String errorMsg = MessageFormat.format(ExcelImportErrorCode.CHOICE_MORE_THAN_TWO.getMessage(), optionCount);
            String errorMsgEn = MessageFormat.format(ExcelImportErrorCode.CHOICE_MORE_THAN_TWO.getMessageEn(), optionCount);
            throw new ExcelImportException(errorCode, rowIndex, null, errorMsg, errorMsgEn);
        }
        return optionReqList;
    }

    /**
     * 数组是否存在非空
     *
     * @param start 开始index
     * @param arr   数组
     * @return boolean
     */
    private boolean existNonEmpty(int start, String[] arr) {
        for (int i = start; i < arr.length; i++) {
            if (!StringUtils.isEmpty(arr[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 构造 option
     *
     * @param order   order
     * @param content content
     * @param type    type
     * @return OptionReq
     */
    private OptionReq buildOptionByOrderAndAttAndType(int order, String content, QuestionTypeEnum type) {
        // 构造 option
        OptionReq optionReq = new OptionReq();
        if (isSingleChoice(type)) {
            optionReq = initSingleChoiceOptionReq(order, content);
        }
        if (isMultipleChoice(type)) {
            optionReq = initMultipleChoiceOptionReq(order, (content));
        }
        return optionReq;
    }

    /**
     * 单选-初始化 options
     *
     * @param order   order
     * @param content content
     * @return OptionReq
     */
    private OptionReq initSingleChoiceOptionReq(int order, String content) {
        OptionReq optionReq = new OptionReq();
        optionReq.setOptionId(null);
        optionReq.setOptionOrder(order);
        optionReq.setOptionContent(content);
        // 构造 option properties
        List<PropertyVO> optionPropertiesList = new ArrayList<>();
        PropertyVO optionPropertyVO = new PropertyVO();
        optionPropertyVO.setPropertyKey(OptionPropertyEnum.TEMP_ID.getPropertyKey());
        optionPropertyVO.setPropertyValue(UUID.randomUUID().toString());
        optionPropertiesList.add(optionPropertyVO);
        optionReq.setProperties(optionPropertiesList);
        return optionReq;
    }

    /**
     * 单选-初始化 question properties
     *
     * @return return
     */
    private List<PropertyVO> initSingleChoicePropertiesList() {
        // 构造 question properties
        List<PropertyVO> questionPropertiesList = new ArrayList<>();
        PropertyVO questionPropertyVO = new PropertyVO();
        questionPropertyVO.setPropertyKey(QuestionPropertyEnum.TEMP_ID.getPropertyKey());
        questionPropertyVO.setPropertyValue(UUID.randomUUID().toString());
        questionPropertiesList.add(questionPropertyVO);
        return questionPropertiesList;
    }

    /**
     * 多选-初始化 options
     *
     * @param order   order
     * @param content content
     * @return OptionReq
     */
    private OptionReq initMultipleChoiceOptionReq(int order, String content) {
        OptionReq optionReq = new OptionReq();
        optionReq.setOptionId(null);
        optionReq.setOptionOrder(order);
        optionReq.setOptionContent(content);
        optionReq.setIsOther(false);
        // 构造 option properties
        List<PropertyVO> optionPropertiesList = new ArrayList<>();
        PropertyVO optionPropertyVO = new PropertyVO();
        optionPropertyVO.setPropertyKey(OptionPropertyEnum.TEMP_ID.getPropertyKey());
        optionPropertyVO.setPropertyValue(UUID.randomUUID().toString());
        optionPropertiesList.add(optionPropertyVO);
        optionReq.setProperties(optionPropertiesList);
        return optionReq;
    }

    /**
     * 多选-初始化 question properties
     *
     * @return return
     */
    private List<PropertyVO> initMultipleChoicePropertiesList() {
        List<PropertyVO> questionPropertiesList = new ArrayList<>();
        PropertyVO questionPropertyVO1 = new PropertyVO();
        PropertyVO questionPropertyVO2 = new PropertyVO();
        PropertyVO questionPropertyVO3 = new PropertyVO();
        PropertyVO questionPropertyVO4 = new PropertyVO();

        questionPropertyVO1.setPropertyKey(QuestionPropertyEnum.SWITCH_MUM_CHANGE.getPropertyKey());
        questionPropertyVO1.setPropertyValue("false");
        questionPropertyVO2.setPropertyKey(QuestionPropertyEnum.MAXIMUM.getPropertyKey());
        questionPropertyVO2.setPropertyValue("-1");
        questionPropertyVO3.setPropertyKey(QuestionPropertyEnum.MINIMUM.getPropertyKey());
        questionPropertyVO3.setPropertyValue("-1");
        questionPropertyVO4.setPropertyKey(QuestionPropertyEnum.TEMP_ID.getPropertyKey());
        questionPropertyVO4.setPropertyValue(UUID.randomUUID().toString());

        questionPropertiesList.add(questionPropertyVO1);
        questionPropertiesList.add(questionPropertyVO2);
        questionPropertiesList.add(questionPropertyVO3);
        questionPropertiesList.add(questionPropertyVO4);
        return questionPropertiesList;
    }

    /**
     * 单选-初始化 question properties
     *
     * @return return
     */
    private List<PropertyVO> initFillPropertiesList() {
        // 构造 question properties
        List<PropertyVO> questionPropertiesList = new ArrayList<>();
        PropertyVO questionPropertyVO1 = new PropertyVO();
        PropertyVO questionPropertyVO2 = new PropertyVO();

        questionPropertyVO1.setPropertyKey(QuestionPropertyEnum.FILL_CONTENT_TYPE.getPropertyKey());
        questionPropertyVO1.setPropertyValue(FillContentTypeEnum.NORMAL_TEXT.getType());
        questionPropertyVO2.setPropertyKey(QuestionPropertyEnum.TEMP_ID.getPropertyKey());
        questionPropertyVO2.setPropertyValue(UUID.randomUUID().toString());

        questionPropertiesList.add(questionPropertyVO1);
        questionPropertiesList.add(questionPropertyVO2);
        return questionPropertiesList;
    }

    /**
     * 构造 AddQuestionsLineResultBO
     *
     * @param errorMsg   errorMsg
     * @param errorMsgEn errorMsgEn
     * @param isSuccess  成功或失败
     * @return AddQuestionsLineResultBO
     */
    private AddQuestionsLineResultBO buildAddQuestionsLineResultBO(String errorMsg, String errorMsgEn,
            boolean isSuccess) {
        AddQuestionsLineResultBO addQuestionsLineResultBO = new AddQuestionsLineResultBO();
        addQuestionsLineResultBO.setErrorMsg(errorMsg);
        addQuestionsLineResultBO.setErrorMsgEn(errorMsgEn);
        addQuestionsLineResultBO.setHasSuccess(isSuccess);
        return addQuestionsLineResultBO;
    }

    /**
     * 检查Excel文件行数
     *
     * @param context AnalysisContext
     */
    private void checkExcelRowNum(AnalysisContext context) {
        Integer approximateTotalRowNumber = context.readSheetHolder().getApproximateTotalRowNumber();
        // 备注和标题占了2行
        int totalRow = approximateTotalRowNumber - 2;
        if (totalRow > MAX_ROW_COUNT) {
            Integer errorCode = ExcelImportErrorCode.ROW_NUMBER_TOO_LARGE.getCode();
            String errorMsg = MessageFormat.format(ExcelImportErrorCode.ROW_NUMBER_TOO_LARGE.getMessage(), MAX_ROW_COUNT);
            String errorMsgEn = MessageFormat.format(ExcelImportErrorCode.ROW_NUMBER_TOO_LARGE.getMessageEn(), MAX_ROW_COUNT);
            throw new ExcelFormatException(errorCode, null, null, errorMsg, errorMsgEn);
        }
    }

    /**
     * 检查Excel标题
     *
     * @param headMap headMap
     */
    private void checkHeadTitle(Map<Integer, ReadCellData<?>> headMap) {
        List<String> titleNameList = new ArrayList<>();
        for (Map.Entry<Integer, ReadCellData<?>> entry : headMap.entrySet()) {
            ReadCellData<?> value = entry.getValue();
            Integer columnIndex = value.getColumnIndex() + 1;
            if (StringUtils.isEmpty(value.getStringValue())) {
                Integer errorCode = ExcelImportErrorCode.TITLE_MUST_NOT_EMPTY.getCode();
                String errorMsg = ExcelImportErrorCode.TITLE_MUST_NOT_EMPTY.getMessage();
                String errorMsgEn = ExcelImportErrorCode.TITLE_MUST_NOT_EMPTY.getMessageEn();
                throw new ExcelFormatException(errorCode, HEAD_INDEX_NUM, columnIndex, errorMsg, errorMsgEn);
            }
            String columnName = value.getStringValue().trim();
            titleNameList.add(columnName);
        }
        // Excel标题行 必须与 类实体属性 定义保持一致
        if (!CollectionUtils.isEqualCollection(titleNameList, FILED_ANNOTATION_NAME_LIST)) {
            Integer errorCode = ExcelImportErrorCode.TITLE_NOT_MATCH.getCode();
            String errorMsg = ExcelImportErrorCode.TITLE_NOT_MATCH.getMessage();
            String errorMsgEn = ExcelImportErrorCode.TITLE_NOT_MATCH.getMessageEn();
            throw new ExcelFormatException(errorCode, HEAD_INDEX_NUM, null, errorMsg, errorMsgEn);
        }
    }

    /**
     * 初始化 列名对应的列号
     *
     * @param headMap headMap
     * @return 列名, 列号
     */
    private Map<String, Integer> initColumnHeadMap(Map<Integer, ReadCellData<?>> headMap) {
        Map<String, Integer> tempColumnHeadMap = new HashMap<>();
        for (Map.Entry<Integer, ReadCellData<?>> entry : headMap.entrySet()) {
            ReadCellData<?> value = entry.getValue();
            Integer columnIndex = value.getColumnIndex() + 1;
            String columnName = value.getStringValue().trim();
            tempColumnHeadMap.put(columnName, columnIndex);
        }
        return tempColumnHeadMap;
    }

    /**
     * 检查填写内容长度
     *
     * @param data     data
     * @param rowIndex rowIndex
     */
    private void checkValidator(ImportQuestionModel data, int rowIndex) {
        if (null == data) {
            return;
        }
        String questionTile = data.getTitle();
        // 题目标题长度限制 250(字符长度)
        if (null != questionTile && questionTile.length() > DataLengthConstants.QUESTION_TITLE_LIMIT) {
            // 标题所在列名:'题目'
            String columnName = FILED_ANNOTATION_NAME_VALUE_MAP.get(ImportQuestionModel.Fields.title);
            int columnIndex = columnHeadMap.get(columnName);
            Integer errorCode = ExcelImportErrorCode.QUESTION_TITLE_TOO_LONG.getCode();
            String errorMsg = MessageFormat.format(ExcelImportErrorCode.QUESTION_TITLE_TOO_LONG.getMessage(),
                    DataLengthConstants.QUESTION_TITLE_LIMIT);
            String errorMsgEn = MessageFormat.format(ExcelImportErrorCode.QUESTION_TITLE_TOO_LONG.getMessageEn(),
                    DataLengthConstants.QUESTION_TITLE_LIMIT);
            throw new ExcelFormatException(errorCode, rowIndex, columnIndex, errorMsg, errorMsgEn);
        }
    }

    /**
     * 是否全部导入成功
     *
     * @return boolean
     */
    public boolean isAllSuccess() {
        return addQuestionsList.size() == importQuestionModelList.size();
    }

    /**
     * 是否全部导入失败
     *
     * @return boolean
     */
    public boolean isAllError() {
        return addQuestionsList.size() == 0;
    }

    /**
     * 导入 Excel 的总行数
     *
     * @return int
     */
    public int getRowCounts() {
        return importQuestionModelList.size();
    }

    /**
     * 导入 Excel 的 成功的行数
     *
     * @return int
     */
    public int getSuccessRowCounts() {
        return addQuestionsList.size();
    }

    /**
     * 导入 Excel 的 失败的行数
     *
     * @return int
     */
    public int getErrorRowCounts() {
        return resultErrorList.size();
    }
}