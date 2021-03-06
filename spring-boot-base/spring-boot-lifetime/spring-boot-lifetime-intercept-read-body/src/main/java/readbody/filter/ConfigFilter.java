package readbody.filter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 拦截所有请求
@WebFilter(urlPatterns = "/*", filterName = "configFilter")
public class ConfigFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
        logger.info("≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡ 过滤器: doFilter 初始化 ≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡ 过滤器: doFilter 进入 ≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        BodyCachingRequestWrapper wrapperRequest = new BodyCachingRequestWrapper(request);
        logger.info("过滤器 key=" + wrapperRequest.getParameter("key"));
        logger.info("过滤器 token=" + wrapperRequest.getHeader("token"));
        // 注意: wrapperRequest.getInputStream() 仍然只能读取一次
        logger.info("过滤器 requestBody before filterChain:" + IOUtils.toString(wrapperRequest.getBody(), wrapperRequest.getCharacterEncoding()));
        filterChain.doFilter(wrapperRequest, servletResponse);
        logger.info("过滤器 requestBody after filterChain:" + IOUtils.toString(wrapperRequest.getBody(), wrapperRequest.getCharacterEncoding()));
        logger.info("≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡ 过滤器: doFilter 完成 ≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡");
    }

    @Override
    public void destroy() {
        logger.info("≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡ 过滤器: Login filter destroy ≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡");
    }
}
