package readbody.service;

import lombok.extern.slf4j.Slf4j;
import readbody.aspects.Flow;
import readbody.aspects.LogAspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class MySender {

    @Autowired
    private LogAspect logAspect;

    @Flow
    public void doRequest() {
        printStepCacheMap();
    }

    private void printStepCacheMap() {
        String treadName = Thread.currentThread().getName();
        String currentTraceId = logAspect.getCurrentTraceId();
        log.info("子线程执行:Sub TreadName:{}, currentTraceId:{}", treadName, currentTraceId);
    }
}


