package com.producer.sender;

import com.ack.common.dao.BrokerMessageLogMapper;
import com.ack.common.entity.BrokerMessageLog;
import com.ack.common.entity.Order;
import com.producer.config.ProducerConfig;
import com.producer.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderSender {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;


    /**
     * 投递回调: setConfirmCallback
     * 触发条件: 消息投递 exchange 到达了 exchange, 注意:只是到达 exchange, 并不表示成功投递
     * 可能出现:
     * 1.消息到达 exchange, 并成功投递 ack=true
     * 2.消息到达 exchange, 并投递失败 ack=false
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * @param correlationData 唯一标识，有了这个唯一标识，我们就知道可以确认（失败）哪一条消息了
         * @param ack  是否投递成功
         * @param cause 失败原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            String messageId = correlationData.getId();
            BrokerMessageLog entity = new BrokerMessageLog();
            entity.setMessageId(messageId);

            BrokerMessageLog brokerMessageLog = null;
            try {
                brokerMessageLog = brokerMessageLogMapper.selectByPrimaryKey(messageId);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            if (null == brokerMessageLog) {
                logger.error("不存在messageId:{}的日志记录", messageId);
                return;
            }

            //返回成功，表示消息被正常投递
            if (ack) {
                brokerMessageLog.setStatus(Constants.ORDER_SEND_SUCCESS);
                brokerMessageLog.setUpdateTime(LocalDateTime.now());
                brokerMessageLogMapper.updateByPrimaryKeySelective(brokerMessageLog);
                logger.info("信息投递到exchange成功，messageId:{}", brokerMessageLog.getMessageId());
            } else {
                // TODO: 失败则进行具体的后续操作:重新发送 或者补偿等手段
                logger.error("信息投递到exchange失败，messageId:{} 原因:{}", brokerMessageLog.getMessageId(), cause);
            }
        }
    };


    /**
     * 失败回调:setReturnCallback
     * 触发条件:消息成功到达exchange,但是没有队列与之绑定的时候触发的ack回调
     * 举例:
     *     发生网络分区会出现这种情况。如果消息未能投递到目标 queue 里将触发回调 returnCallback ，
     *     一旦向 queue 投递消息未成功，这里一般会记录下当前消息的详细投递数据，方便后续做重发或者补偿等操作。
     */
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            logger.info("消息投递失败, exchange 没有对应的 queue,失败:message:{},id:{},reason:{},exchange:{},routingKey:{}", message, replyCode, replyText, exchange, routingKey);
        }
    };

    /**
     * 信息投递的方法
     */
    public void send(Order order, String routeKey) throws Exception {
        // 通过实现 ConfirmCallback 接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中
        // 打开回调开关: publisher-confirms="true" publisher-returns="true"
        // 投递回调:setConfirmCallback, 当消息成功到达exchange的时候触发的ack回调
        rabbitTemplate.setConfirmCallback(confirmCallback);
        // 失败回调:setReturnCallback, 当消息成功到达exchange，但是没有队列与之绑定的时候触发的ack回调
        rabbitTemplate.setReturnCallback(returnCallback);
        // 设置消息唯一ID
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getMessageId());
        rabbitTemplate.convertAndSend(ProducerConfig.TOPIC_EXCHANGE, routeKey, order,
                message -> {
                    // 设置消息的TTL超时时间
                    // 由于队列的先进先出特性，只有当过期的消息到了队首，才会进入死信队列，所以需要保证每条消息延迟一致
                    MessageProperties messageProperties = message.getMessageProperties();
                    messageProperties.setExpiration(30 * 1000 + "");
                    // 延迟发送时间
                    messageProperties.setDelay(0);
                    return message;
                },
                correlationData);
    }

}
