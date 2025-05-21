package com.inter.entity.excpetion;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @Date: 2019-05-27
 * @Version 1.0
 */
public class ApiResultMsgUtil {

    private static final Logger log = LoggerFactory.getLogger(ApiResultMsgUtil.class);

    private ApiResultMsgUtil() {
        // Do nothing
    }

    /*
            201   （已创建）     请求成功并且服务器创建了新的资源。
            202   （已接受）     服务器已接受请求，但尚未处理。
            203   （非授权信息）  服务器已成功处理了请求，但返回的信息可能来自另一来源。
            204   （无内容）     服务器成功处理了请求，但没有返回任何内容。
            205   （重置内容）    服务器成功处理了请求，但没有返回任何内容。
            206   （部分内容）    服务器成功处理了部分 GET 请求。
         */
    // 返回success信息, 不写code, 默认为200, status=1
    public static JSONObject returnSuccess(String msg) {
        ApiResultMsg apiResultMsg = new ApiResultMsg(200, msg);
        apiResultMsg.setStatus("1");
        String resJsonStr = JSONObject.toJSONString(apiResultMsg);
        log.info(resJsonStr);
        return JSONObject.parseObject(resJsonStr, JSONObject.class, Feature.DisableSpecialKeyDetect);
    }

    /**
     * 构造success信息
     */
    public static JSONObject returnSuccess(Integer code, String msg) {
        ApiResultMsg apiResultMsg = new ApiResultMsg(code, msg);
        apiResultMsg.setStatus("1");
        String resJsonStr = JSONObject.toJSONString(apiResultMsg);
        log.info(resJsonStr);
        return JSONObject.parseObject(resJsonStr, JSONObject.class, Feature.DisableSpecialKeyDetect);
    }


    /*
        500   （服务器内部错误）  服务器遇到错误，无法完成请求。
        501   （尚未实施） 服务器不具备完成请求的功能。 例如，服务器无法识别请求方法时可能会返回此代码。
        502   （错误网关） 服务器作为网关或代理，从上游服务器收到无效响应。
        503   （服务不可用） 服务器目前无法使用（由于超载或停机维护）。 通常，这只是暂时状态。
        504   （网关超时）  服务器作为网关或代理，但是没有及时从上游服务器收到请求。
        505   （HTTP 版本不受支持） 服务器不支持请求中所用的 HTTP 协议版本。
    */
    // 构造错误信息, 不写code, 默认为500, status=0
    public static JSONObject returnError(String msg) {
        ApiResultMsg apiResultMsg = new ApiResultMsg(500, msg);
        apiResultMsg.setStatus("0");
        log.error("occur error: {}", msg);
        String resJsonStr = JSONObject.toJSONString(apiResultMsg);
        log.error(resJsonStr);
        return JSONObject.parseObject(resJsonStr, JSONObject.class, Feature.DisableSpecialKeyDetect);
    }

    /**
     * 构造错误信息
     */
    public static JSONObject returnError(Integer code, String msg, HttpServletRequest req, Exception e) {

        // 返回错误信息
        ApiResultMsg apiResultMsg = new ApiResultMsg(msg, req, e);
        apiResultMsg.setStatus("0");
        apiResultMsg.setCode(code);

        // 打印错误信息
        ApiResultMsg logErrorMsgObejct = new ApiResultMsg(msg, req, e);
        logErrorMsgObejct.setStatus("0");
        logErrorMsgObejct.setCode(code);
        logErrorMsgObejct.setUrl(req.getRequestURL().toString());
        logErrorMsgObejct.setField((Serializable) req.getParameterMap());
        logErrorMsgObejct.setErrorMsg(e.getMessage());
        String logErrorJson = JSONObject.toJSONString(logErrorMsgObejct);
        log.error(logErrorJson);

        log.error("occur error: {}", msg);
        String jsonStr = JSONObject.toJSONString(e);
        log.error(jsonStr, e); // 因Jsonfile=false,需显示的输出错误信息

        // Feature.DisableSpecialKeyDetect 是为了解决 fastJson 反序列化时候出现 "@type" 内容报错: autoType is not support. com.alibaba.fastjson.JSONException
        return JSONObject.parseObject(JSONObject.toJSONString(apiResultMsg), JSONObject.class, Feature.DisableSpecialKeyDetect);
    }


}
