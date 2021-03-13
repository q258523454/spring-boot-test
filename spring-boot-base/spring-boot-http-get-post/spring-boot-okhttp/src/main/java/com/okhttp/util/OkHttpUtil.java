package com.okhttp.util;

import com.alibaba.fastjson.JSON;
import com.okhttp.entity.HttpReceive;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class OkHttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    public static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";
    public static final String MEDIA_TYPE_XML = "application/xml; charset=utf-8";

    /**
     * do get
     * get 请求的参数，是在 URL 后面追加  http://xxxx:8080/user?name=xxxx&sex=1
     *
     * @param url
     * @param header
     * @param query
     * @return
     */
    public static HttpReceive doGet(String url, Map<String, Object> header, Map<String, Object> query) {
        HttpReceive httpReceive = new HttpReceive();
        Response response = null;
        OkHttpClient okHttpClient = null;
        try {
            okHttpClient = SpringContextHolder.getBean("okHttpClient");
            Request request = null;
            Request.Builder builder = new Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

            // 装载请求头参数
            if (null != header && !header.isEmpty()) {
                Iterator<Map.Entry<String, Object>> headerIterator = header.entrySet().iterator();
                headerIterator.forEachRemaining(e -> {
                    builder.addHeader(e.getKey(), (String) e.getValue());
                });
            }

            // 装载请求的参数 (?name=xxxx&sex=1)
            if (null != query && !query.isEmpty()) {
                Iterator<Map.Entry<String, Object>> queryIterator = query.entrySet().iterator();
                queryIterator.forEachRemaining(e -> {
                    urlBuilder.addQueryParameter(e.getKey(), (String) e.getValue());
                });
            }

            // 设置自定义的 builder
            request = builder.url(urlBuilder.build()).build();
            response = okHttpClient.newCall(request).execute();

            httpReceive.setBody(response.body());
            httpReceive.setHeaders(response.headers());
            httpReceive.setStatusCode(response.code());
            httpReceive.setErrMsg("success");
            return httpReceive;
        } catch (Exception e) {
            logger.error("error:{}", e.getMessage());
            httpReceive.setErrMsg(e.getMessage());
            httpReceive.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return httpReceive;
        } finally {
            if (null != response) {
                response.close();
            }
            if (null != okHttpClient) {
                okHttpClient.dispatcher().executorService().shutdown();
            }
        }
    }


    /**
     * Get 异步请求
     *
     * @param url
     * @param header
     * @param query
     * @return
     */
    public static HttpReceive doGetAsync(String url, Map<String, Object> header, Map<String, Object> query) {
        HttpReceive httpReceive = new HttpReceive();
        OkHttpClient okHttpClient = null;
        try {
            okHttpClient = SpringContextHolder.getBean("okHttpClient");
            Request request = null;
            Request.Builder builder = new Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

            // 装载请求头参数
            if (null != header && !header.isEmpty()) {
                Iterator<Map.Entry<String, Object>> headerIterator = header.entrySet().iterator();
                headerIterator.forEachRemaining(e -> {
                    builder.addHeader(e.getKey(), (String) e.getValue());
                });
            }

            // 装载请求的参数 ((?name=xxxx&sex=1))
            if (null != query && !query.isEmpty()) {
                Iterator<Map.Entry<String, Object>> queryIterator = query.entrySet().iterator();
                queryIterator.forEachRemaining(e -> {
                    urlBuilder.addQueryParameter(e.getKey(), (String) e.getValue());
                });
            }

            // 设置自定义的 builder
            request = builder.url(urlBuilder.build()).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    logger.error("error:{}", e.getMessage(), e);
                    httpReceive.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    httpReceive.setErrMsg(e.getMessage());
                    httpReceive.setHaveDone(true);
                    logger.info("doGetAsync call is done:{}", JSON.toJSONString(httpReceive));

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    httpReceive.setBody(response.body());
                    httpReceive.setHeaders(response.headers());
                    httpReceive.setStatusCode(response.code());
                    httpReceive.setHaveDone(true);
                    logger.info("doGetAsync call is done:{}", JSON.toJSONString(httpReceive));
                }
            });

            logger.info("doGetAsync is done:{}", JSON.toJSONString(httpReceive));
            return httpReceive;
        } catch (Exception e) {
            logger.error("error:{}", e.getMessage());
            httpReceive.setErrMsg(e.getMessage());
            httpReceive.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpReceive.setHaveDone(true);
            return httpReceive;
        }
    }

    /**
     * post 请求
     *
     * @param url
     * @param header
     * @param params
     * @return
     * @throws Exception
     */
    public static HttpReceive doPost(String url, Map<String, Object> header, Map<String, Object> params) throws Exception {
        HttpReceive httpReceive = new HttpReceive();
        Response response = null;
        OkHttpClient okHttpClient = null;
        try {
            Request request = null;
            Request.Builder builder = new Request.Builder();
            okHttpClient = SpringContextHolder.getBean("okHttpClient");

            // 装载请求头参数
            if (null != header && !header.isEmpty()) {
                Iterator<Map.Entry<String, Object>> headerIterator = header.entrySet().iterator();
                headerIterator.forEachRemaining(e -> {
                    builder.addHeader(e.getKey(), (String) e.getValue());
                });
            }

            FormBody.Builder post = new FormBody.Builder();

            // post请求参数
            Iterator<Map.Entry<String, Object>> queryIterator = params.entrySet().iterator();
            queryIterator.forEachRemaining(e -> {
                post.add(e.getKey(), (String) e.getValue());
            });

            // 设置自定义的 builder, 可以直接用 builder.post(),delete()..etc
            builder.method("POST", post.build());

            response = okHttpClient.newCall(request).execute();

            httpReceive.setBody(response.body());
            httpReceive.setHeaders(response.headers());
            httpReceive.setStatusCode(response.code());
            httpReceive.setErrMsg("成功");
            return httpReceive;
        } catch (Exception e) {
            logger.error("error:{}", e.getMessage());
            httpReceive.setErrMsg(e.getMessage());
            httpReceive.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return httpReceive;
        } finally {
            if (null != response) {
                response.close();
            }
            if (null != okHttpClient) {
                okHttpClient.dispatcher().executorService().shutdown();
            }
        }
    }


    /**
     * do post json String
     *
     * @param url
     * @param header
     * @param params
     * @param mediaType
     * @return
     * @throws Exception
     */
    public static HttpReceive doPost(String url, Map<String, Object> header, String params, String mediaType) throws Exception {
        HttpReceive httpReceive = new HttpReceive();
        Response response = null;
        OkHttpClient okHttpClient = null;
        try {
            okHttpClient = SpringContextHolder.getBean("okHttpClient");
            Request request = null;
            RequestBody requestBody = RequestBody.create(params, MediaType.parse(mediaType));
            Request.Builder builder = new Request.Builder();

            // 装载请求头参数
            if (null != header && !header.isEmpty()) {
                Iterator<Map.Entry<String, Object>> headerIterator = header.entrySet().iterator();
                headerIterator.forEachRemaining(e -> {
                    builder.addHeader(e.getKey(), (String) e.getValue());
                });
            }
            // 设置自定义的 builder
            request = builder.url(url).post(requestBody).build();
            response = okHttpClient.newCall(request).execute();

            httpReceive.setBody(response.body());
            httpReceive.setHeaders(response.headers());
            httpReceive.setStatusCode(response.code());
            httpReceive.setErrMsg("成功");
            return httpReceive;
        } catch (Exception e) {
            logger.error("error cause:{},msg:{}", e.getCause().getCause(), e.getMessage());
            httpReceive.setErrMsg(e.getMessage());
            httpReceive.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return httpReceive;
        } finally {
            if (null != response) {
                response.close();
            }
            if (null != okHttpClient) {
                okHttpClient.dispatcher().executorService().shutdown();
            }
        }
    }


}
