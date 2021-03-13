package com.apache.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public enum HttpUtilServiceImpl implements HttpUtilService {
    /**
     * 实例
     */
    INSTANCE,
    ;

    private Logger logger = LoggerFactory.getLogger(HttpUtilServiceImpl.class);


    @Override
    public StringBuffer postHttpJsonByte(String URL, JSONObject jsonObject, JSONObject res) throws IOException {
        HttpURLConnection connection = null;
        try {

            // 创建连接
            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();

            // 设置http连接属性
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST"); // 可以根据需要 提交 GET、POST、DELETE、INPUT等http提供的功能
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(5000); // 连接五秒超时

            // 设置http头 消息
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");  // UTF-8 解决中文乱码
            // connection.setRequestProperty("Content-Type", "text/xml");   //设定 请求格式 xml，
            connection.setRequestProperty("Accept", "application/json");//设定响应的信息的格式为 json，也可以设定xml格式的

            // connection.setRequestProperty("X-Auth-Token","xx");  //特定http服务器需要的信息，根据服务器所需要求添加
            connection.connect();
            // 传递 fastjson数据
            OutputStream out = connection.getOutputStream();    // 获取(写)数据流
            out.write(jsonObject.toJSONString().getBytes());    // 向数据流写数据 (JsonObject需要先转换成字符串)
            out.flush();
            out.close();

            // 请求返回的状态(也就是执行服务器程序) connection.getResponseCode() ---- 服务器执行服务的关键代码, 没有该代码服务器不执行服务.
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new ConnectException("Can not connect URL. status = " + connection.getResponseCode());
            }

            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            res.put("results", JSONObject.parse(sb.toString()));
            reader.close();
            connection.disconnect();     // 断开连接
            return new StringBuffer("ok");

        } catch (ConnectException e) {
            // TODO catch block
            e.printStackTrace();
            return new StringBuffer("URL无法连接,连接异常.");
        } catch (MalformedURLException e) {
            // TODO catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public StringBuffer postHttpJsonData(String URL, JSONObject jsonObject, JSONObject responseJsonMap, int... timeoutArrays) throws Exception {

        // setConnectTimeout: 从客户端到url建立连接的超时时间
        // setSocketTimeout: 连接上一个url后，获取response的返回等待时间
        RequestConfig requestConfig = null;
        if (timeoutArrays.length == 0) {
            requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000).setSocketTimeout(3600 * 1000).build();
        } else if (timeoutArrays.length == 2) {
            requestConfig = RequestConfig.custom().setConnectTimeout(timeoutArrays[0]).setSocketTimeout(timeoutArrays[1]).build();
        } else {
            throw new Exception("post 超时参数SocketTimeout和ConnectTimeout不符合要求");
        }
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        try {
            HttpPost post = new HttpPost(URL);
            // UTF-8 解决中文乱码
            StringEntity params = new StringEntity(jsonObject.toJSONString(), "UTF-8");
            post.addHeader("content-type", "application/json; charset=UTF-8");
            post.addHeader("Accept", "application/json");
            post.addHeader("Accept-Encoding", "UTF-8");
            post.setEntity(params);

            // 发送 post 请求
            HttpResponse response = httpClient.execute(post);

            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                logger.info("postHttpJsonData:After Post Response is Ok :HttpStatus.SC_OK[200]");
                InputStream in = response.getEntity().getContent(); //Get the data in the com.multi.entity
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb.append(lines);
                }
                reader.close();
                in.close();
                logger.info(sb.toString());

                responseJsonMap.put("status", "success");
                responseJsonMap.put("message", sb.toString());
            } else if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the com.multi.entity
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb.append(lines);
                }
                reader.close();
                in.close();
                logger.info(sb.toString());

                responseJsonMap.put("status", "error");
                responseJsonMap.put("message", "调起异常, 请联系管理员." + "error:" + sb.toString());
            } else {
                responseJsonMap.put("status", "error");
                responseJsonMap.put("message", "调起异常, 请联系管理员.");
            }

        } catch (Exception ex) {
            responseJsonMap.put("status", "error");
            responseJsonMap.put("message", "调起异常, 请联系管理员.");
            ex.printStackTrace();
        } finally {
            httpClient.close();
        }
        return new StringBuffer("finish post");
    }

    @Override
    public StringBuffer postHttpStringData(String URL, String platformId, String postData, JSONObject responseJsonMap, int... timeoutArrays) throws Exception {

        RequestConfig requestConfig = null;
        if (timeoutArrays.length == 0) {
            requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000).setSocketTimeout(3600 * 1000).build();
        } else if (timeoutArrays.length == 2) {
            requestConfig = RequestConfig.custom().setConnectTimeout(timeoutArrays[0]).setSocketTimeout(timeoutArrays[1]).build();
        } else {
            throw new Exception("post 超时参数SocketTimeout和ConnectTimeout不符合要求");
        }
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        try {
            HttpPost post = new HttpPost(URL);
            // UTF-8 解决中文乱码
            StringEntity params = new StringEntity(postData, "UTF-8");
            post.addHeader("content-type", "application/json; charset=UTF-8");
            post.addHeader("Accept", "application/json");
            post.addHeader("Accept-Encoding", "UTF-8");
            post.addHeader("platform_id", platformId);
            post.setEntity(params);

            // 发送 post 请求
            HttpResponse response = httpClient.execute(post);

            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                logger.info("postHttpJsonData:After Post Response is Ok :HttpStatus.SC_OK[200]");
                InputStream in = response.getEntity().getContent(); //Get the data in the com.multi.entity
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb.append(lines);
                }
                reader.close();
                in.close();
                logger.info(sb.toString());

                responseJsonMap.put("status", "success");
                responseJsonMap.put("message", sb.toString());
            } else if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the com.multi.entity
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb.append(lines);
                }
                reader.close();
                in.close();
                logger.info(sb.toString());

                responseJsonMap.put("status", "error");
                responseJsonMap.put("message", "调起异常, 请联系管理员." + "error:" + sb.toString());
            } else {
                responseJsonMap.put("status", "error");
                responseJsonMap.put("message", "调起异常, 请联系管理员.");
            }

        } catch (Exception ex) {
            responseJsonMap.put("status", "error");
            responseJsonMap.put("message", "调起异常, 请联系管理员.");
            ex.printStackTrace();
        } finally {
            httpClient.close();
        }
        return new StringBuffer("finish post");
    }

}
