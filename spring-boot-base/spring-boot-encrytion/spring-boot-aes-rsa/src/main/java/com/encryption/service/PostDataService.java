package com.encryption.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * Created by
 *
 * @date :   2018-04-28
 */

public interface PostDataService {
    StringBuffer postHttpJsonByte(String URL, JSONObject jsonObject, JSONObject results) throws IOException;

    StringBuffer postHttpJsonData(String URL, JSONObject jsonObject, JSONObject results, int... timeoutArrays) throws Exception;

    StringBuffer postHttpStringData(String URL, String postData, JSONObject results, int... timeoutArrays) throws Exception;

}
