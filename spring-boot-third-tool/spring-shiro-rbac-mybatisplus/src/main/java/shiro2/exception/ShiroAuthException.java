package shiro2.exception;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 自定义异常
 */
@ControllerAdvice
@Slf4j
public class ShiroAuthException {
    /**
     * 处理Shiro权限拦截异常
     * 如果返回JSON数据格式请加上 @ResponseBody注解
     *
     * @Return Map<Object> 返回结果集
     */
    @ResponseBody
    @ExceptionHandler(value = AuthorizationException.class)
    public Map<String, Object> defaultErrorHandler(AuthorizationException exception) {
        log.error("error:" + exception);
        Map<String, Object> map = new HashMap<>();
        map.put("403", "权限不足");
        return map;
    }
}
