package com.zhang.encryptbody.advice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>解密信息输入流</p>
 * @author zj
 * @date 2020/5/11 14:15
 */
@NoArgsConstructor
@AllArgsConstructor
public class HttpInputMessageImpl implements HttpInputMessage {

    private InputStream body;

    private HttpHeaders headers;

    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
