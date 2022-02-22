package com.test.intercept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;


@Slf4j
public class AllIntercept implements HandlerInterceptor {


    private static final String UNKNOWN = "unknown";


    /***
     * 进入controller方法之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("--------------- 拦截器: IpAddressIntercept preHandle() ---------------");
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("获取所有的请求parameters");
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            log.info(entry.getKey() + ":" + Arrays.toString(entry.getValue()));
        }
        String token = request.getHeader("token");
        String host = request.getHeader("Host");
        log.info("header token:" + token);
        log.info("header host:" + host);
        String ipAddress = getIpAddress(request);
        log.info("ip address:" + ipAddress);
        if ("127.0.0.1".equals(ipAddress)) {
            log.info("ip address is correct.");
        } else {
            log.info("ip address is error.");
            // return false;
        }
        return true;
    }

    /***
     * 进入controller方法之后, 如果Controller出现异常, 不调用该方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.info("--------------- 拦截器: IpAddressIntercept postHandle() ---------------");
    }

    /***
     * controller结束, 无论Controller是不是有异常, 都执行该方法, 一般用于清理资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("--------------- 拦截器: IpAddressIntercept afterCompletion() ---------------");
    }


    /**
     * X-Forwarded-For
     * 这是一个 Squid 开发的字段，只有在通过了 HTTP 代理或者负载均衡服务器时才会添加该项。
     * 格式为 X-Forwarded-For:client1,proxy1,proxy2，一般情况下，第一个 ip 为客户端真实 ip，后面的为经过的代理服务器 ip。现在大部分的代理都会加上这个请求头。
     *
     * Proxy-Client-IP/WL- Proxy-Client-IP
     * 这个一般是经过 apache http 服务器的请求才会有，用 apache http 做代理时一般会加上 Proxy-Client-IP 请求头，而 WL-Proxy-Client-IP 是他的 weblogic 插件加上的请求头。
     *
     * 需要注意几点：
     * 这些请求头都不是 http 协议里的标准请求头，也就是说这是各个代理服务器自己规定的表示客户端地址的请求头。
     * 如果哪天有一个代理服务器软件用 xxx-client-ip 这个请求头代表客户端请求，那上面的代码就不行了。
     * 这些请求头不是代理服务器一定会带上的，网络上的很多匿名代理就没有这些请求头，所以获取到的客户端 ip
     * 不一定是真实的客户端 ip。代理服务器一般都可以自定义请求头设置。即使请求经过的代理都会按自己的规范
     * 附上代理请求头，上面的代码也不能确保获得的一定是客户端 ip。不同的网络架构，判断请求头的顺序是不一样的。
     * 最重要的一点，请求头都是可以伪造的。如果一些对客户端校验较严格的应用（比如投票）要获取客户端 ip，
     * 应该直接使用 request.getRemoteAddr()，虽然获取到的可能是代理的 ip 而不是客户端的 ip，但这个获取
     * 到的 ip 基本上是不可能伪造的，也就杜绝了刷票的可能。
     */
    public String getIpAddress(HttpServletRequest request) {
        String ip = "";
        ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
