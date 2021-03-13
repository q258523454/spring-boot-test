package com.producer.service.impl;

import com.ack.common.dao.BrokerMessageLogMapper;
import com.ack.common.dao.OrderMapper;
import com.ack.common.entity.BrokerMessageLog;
import com.ack.common.entity.Order;
import com.alibaba.fastjson.JSONObject;
import com.producer.constant.Constants;
import com.producer.sender.OrderSender;
import com.producer.service.RabbitService;
import com.producer.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Slf4j
public class RabbitServiceImpl implements RabbitService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    // 下一次投递间隔时间
    @Value("${base.config.rabbitmq.overtime}")
    private int minutes;

    @Autowired
    private OrderSender orderSender;

    @Override
    public void createOrder(Order order, String routeKey) {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));

        try {
            // 插入订单表
            orderMapper.insertSelective(order);
            // 插入rabbitmq投递信息日志表
            BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
            brokerMessageLog.setMessageId(order.getMessageId());
            brokerMessageLog.setMessage(JSONObject.toJSONString(order));
            // 手动修改数据库,可重发
            brokerMessageLog.setStatus(Constants.ORDER_SENDING);
            brokerMessageLog.setCreateTime(LocalDateTime.now());
            // 下一次投递时间
            brokerMessageLog.setNextRetry(longToLocalDateTime(System.currentTimeMillis() + minutes * 60000));
            brokerMessageLog.setTryCount(0);
            brokerMessageLogMapper.insertSelective(brokerMessageLog);
            wPlatformTransactionManager.commit(wTransactionStatus);
            log.info("提交完成");
        } catch (Exception ex) {
            log.info("开始回滚");
            wPlatformTransactionManager.rollback(wTransactionStatus);
            log.info("回滚完成");
            throw ex;
        }

        try {
            //信息投递
            orderSender.send(order, routeKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * long(Date) 转 LocalDateTime
     * @param mills 例如: Calendar.getInstance().getTimeInMillis()
     * @param mills 例如: Date().getTime().getTime
     * @return
     */
    public static LocalDateTime longToLocalDateTime(long mills) {
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(mills).atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime();
    }
}
