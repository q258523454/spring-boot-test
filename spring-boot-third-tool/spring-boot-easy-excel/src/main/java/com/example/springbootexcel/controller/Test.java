package com.example.springbootexcel.controller;

import java.text.MessageFormat;

public class Test {
    public static void main(String[] args) {
        String s = "/web/free/down/{0}/{1}";
        String format = MessageFormat.format(s, 11, "2");
        System.out.println(format);
    }
}

