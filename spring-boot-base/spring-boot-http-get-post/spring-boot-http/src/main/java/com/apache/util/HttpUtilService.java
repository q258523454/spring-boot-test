package com.apache.util;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface HttpUtilService {
    StringBuffer postHttpJsonByte(String URL, JSONObject jsonObject, JSONObject results) throws IOException;

    StringBuffer postHttpJsonData(String URL, JSONObject jsonObject, JSONObject results, int... timeoutArrays) throws Exception;

    StringBuffer postHttpStringData(String URL, String platformId, String postData, JSONObject results, int... timeoutArrays) throws Exception;

}
