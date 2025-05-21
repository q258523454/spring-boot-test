package readbody.filter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Description 解决 ServletRequest 读取body只能读取一次的问题(Dehghani 认为效率问题值得考虑)
 *              如果不需要使用拦截器获取body,可以使用 RequestBodyAdvice/ResponseBodyAdvice 统一处理
 *              注意：这里只是将Body做缓存, 然后使用 getBody()获取副本. getInputStream() 仍然只能读取一次
 *              其他：
 *              ServletRequest 的 getInputStream(),getReader()默认只能使用一次
 *              该方法与 spring自带的 ContentCachingRequestWrapper 方法区别:
 *              1.ContentCachingRequestWrapper 后面每次使用需要显示的指定:contentCachingRequestWrapper.getContentAsByteArray()
 *              2.ContentCachingRequestWrapper 无法在filterChain.doFilter之前获取Body,否则后面报错
 * @date 2020-05-14 10:23
 * @modify
 */
public class BodyCachingRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    private BufferedReader reader;

    private ServletInputStream inputStream;

    public BodyCachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 读一次 然后缓存起来
        body = IOUtils.toByteArray(request.getInputStream());
        inputStream = new RequestCachingInputStream(body);
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (inputStream != null) {
            return inputStream;
        }
        return super.getInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(inputStream, getCharacterEncoding()));
        }
        return reader;
    }

    /**
     * 代理 ServletInputStream, 内容是当前缓存的byte
     * 因为 Request.getInputStream() spring默认只能读取一次, 这里将其缓存, 实现重复去读取
     */
    private static class RequestCachingInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public RequestCachingInputStream(byte[] bytes) {
            inputStream = new ByteArrayInputStream(bytes);
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readlistener) {
        }

    }

}