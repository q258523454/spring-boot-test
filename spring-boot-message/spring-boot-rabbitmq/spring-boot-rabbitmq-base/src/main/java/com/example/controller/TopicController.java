package com.example.controller;

import com.example.producer.TopicSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicController {
    @Autowired
    private TopicSender topicSender;

    @GetMapping("/sendTopicQueue")
    public Object sendTopicQueue() {
        topicSender.sendTopic();
        return "ok";
    }
}