package com.share.filter;

import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DubboFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(DubboFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        logger.warn("---------------------- DubboFilter -------------------");

        return invoker.invoke(invocation);
    }
}
