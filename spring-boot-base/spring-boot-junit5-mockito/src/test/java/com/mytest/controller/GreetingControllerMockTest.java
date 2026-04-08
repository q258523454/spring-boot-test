package com.mytest.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mytest.service.GreetingService;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@Slf4j
@WebMvcTest(GreetingController.class)
class GreetingControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    // 模拟 GreetingService，不加载真实实现
    @MockitoBean
    private GreetingService service;

    @Test
    void greet_shouldUseMockedService() throws Exception {
        when(service.greet(anyString())).thenReturn("Mocked Hello");
        MockHttpServletRequestBuilder param = get("/greet").param("name", "Test");
        MvcResult mvcResult = mockMvc.perform(param)
                .andExpect(status().isOk())
                .andExpect(content().string("Mocked Hello"))
                .andReturn();
        log.info("response is :" + mvcResult.getResponse().getContentAsString());
    }
}