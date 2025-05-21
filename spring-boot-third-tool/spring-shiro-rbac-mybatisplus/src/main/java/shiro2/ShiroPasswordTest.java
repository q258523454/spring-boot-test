package shiro2;


import shiro2.util.SHA256Util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.UUID;

public class ShiroPasswordTest {


    // 加密
    public static void main(String[] args) {
        String salt = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
        System.out.println("salt:" + salt);
        String password = SHA256Util.sha256("123456", salt);
        System.out.println("加密 : " + password);
    }
}

