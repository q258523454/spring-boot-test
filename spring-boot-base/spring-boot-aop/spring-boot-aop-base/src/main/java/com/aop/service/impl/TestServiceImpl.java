package com.aop.service.impl;


import com.aop.service.TestService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Override
    public void print() {
        log.info("TestService print.");
    }
}
