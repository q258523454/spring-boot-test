package com.producer.schedule;

import com.ack.common.dao.BrokerMessageLogMapper;
import com.ack.common.entity.BrokerMessageLog;
import com.ack.common.entity.Order;
import com.alibaba.fastjson.JSONObject;
import com.producer.config.ProducerConfig;
import com.producer.constant.Constants;
import com.producer.sender.OrderSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TrySendTask {

    @Autowired
    private OrderSender orderSender;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    /**
     * 系统启动后5秒开启定时任务 10秒执行一次
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void rabbitmqReSend() {

        /**
         * 查询出下一次执行时间小于当前时间的日志记录并且状态为投递中，
         * 遍历结果集，判断重试次数是或大于3次，如果大于，将日志设置为投递失败，
         * 如果小于 则尝试重新投递，并改变数据库中日志的尝试次数
         */

        BrokerMessageLog entity = new BrokerMessageLog();
        entity.setStatus("0");
        List<BrokerMessageLog> brokerMessageLogs = brokerMessageLogMapper.selectByStatusAndNextRetry(entity);

        brokerMessageLogs.forEach(brokerMessageLog -> {
            if (brokerMessageLog.getTryCount() >= 3) {
                brokerMessageLog.setStatus(Constants.ORDER_SEND_FAILURE);
                brokerMessageLog.setUpdateTime(LocalDateTime.now());
                brokerMessageLogMapper.updateByPrimaryKeySelective(brokerMessageLog);
            } else {
                brokerMessageLog.setTryCount(brokerMessageLog.getTryCount() + 1);
                brokerMessageLog.setUpdateTime(LocalDateTime.now());
                brokerMessageLogMapper.updateByPrimaryKeySelective(brokerMessageLog);
                try {
                    //重新投递信息
                    orderSender.send(JSONObject.parseObject(brokerMessageLog.getMessage(), Order.class), ProducerConfig.TOPIC_QUEUE_ROUTE_KEY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
