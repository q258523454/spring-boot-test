package readbody.aspects;

import com.alibaba.ttl.TransmittableThreadLocal;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@EnableAspectJAutoProxy(exposeProxy = true)
public class LogAspect {

    public static final TransmittableThreadLocal<String> TRACE_ID_CACHE = new TransmittableThreadLocal<>();


    public void setCurrentTraceId(String traceId) {
        TRACE_ID_CACHE.set(traceId);
    }

    public void clearCurrentTraceId(String traceId) {
        TRACE_ID_CACHE.remove();
    }

    public String getCurrentTraceId() {
        return TRACE_ID_CACHE.get();
    }


    @Pointcut("@annotation(readbody.aspects.Flow)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String treadName = Thread.currentThread().getName();
        String traceId = TRACE_ID_CACHE.get();
        log.info("----------pointCut()---------:TreadName:{}, currentTraceId:{}", treadName, traceId);
        return joinPoint.proceed();
    }
}
