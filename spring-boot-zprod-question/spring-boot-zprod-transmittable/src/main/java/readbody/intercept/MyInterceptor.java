package readbody.intercept;

import lombok.extern.slf4j.Slf4j;
import readbody.aspects.LogAspect;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {

    private LogAspect logAspect;

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    @Resource
    public void setLRUCache(LogAspect logAspect) {
        this.logAspect = logAspect;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("--------------- 拦截器: preHandle() ---------------");
        int traceId = COUNTER.incrementAndGet();
        logAspect.setCurrentTraceId(traceId + "");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    /**
     * 执行controller方法之后, 如果Controller出现异常, 不调用该方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("--------------- 拦截器: postHandle() ---------------");
    }


    /**
     * controller结束, 无论Controller是不是有异常, 都执行该方法, 一般用于清理资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("--------------- 拦截器: afterCompletion() begin ---------------");
        String treadName = Thread.currentThread().getName();

        // 清理当前请求的traceId
        String traceId = COUNTER.get() + "";
        log.info("--------------- 拦截器: afterCompletion(). TreadName:{}, clear all traceId :{}", treadName, traceId);
        logAspect.clearCurrentTraceId(traceId);
    }
}
