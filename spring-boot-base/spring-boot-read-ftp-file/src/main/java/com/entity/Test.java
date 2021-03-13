package com.entity;

/**
 * @Date: 2019-06-19
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
        String a = "/ndlj02_local/20190619/aaa.dat";
        String s1 = a.substring(0, a.lastIndexOf("/"));
        String s2 = a.substring(a.lastIndexOf("/")+1, a.length());

        System.out.println(s1);
        System.out.println(s2);
    }
}
