package com.mytest.service;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class GreetingServiceUnitTest {

    private GreetingService service;


    /**
     * 每个测试方法执行前
     */
    @BeforeEach
    public void setUp() {
        service = new GreetingService();
    }

    /**
     * 不启动 Spring 上下文
     * 纯单元测试
     */
    @Test
    void greet_shouldReturnFormattedMessage() {
        // given
        String name = "JUnit5";

        // when
        String result = service.greet(name);
        log.info(result);

        // then
        assertThat(result).isEqualTo("Hello, JUnit5!");
    }
}