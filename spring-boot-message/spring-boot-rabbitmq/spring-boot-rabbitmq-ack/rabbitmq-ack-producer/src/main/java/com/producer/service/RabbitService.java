package com.producer.service;


import com.ack.common.entity.Order;

public interface RabbitService {

    void createOrder(Order order, String routeKey);

}
