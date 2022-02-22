package com.sec.rabbitmq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.sec.pojo.dto.SeckillMessage;
import com.sec.pojo.entity.GoodsSeckill;
import com.sec.rabbitmq.config.DirectConfig;
import com.sec.redis.SeckillOrderKey;
import com.sec.service.impl.GoodsSeckillService;
import com.sec.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class DirectConsumer {

    @Autowired
    private GoodsSeckillService goodsSeckillService;

    @RabbitListener(queues = DirectConfig.QUEUE_DIRECT)
    public void consumer(String str, Message message, Channel channel) throws Exception {
        // Json反序列化
        ObjectMapper mapper = new ObjectMapper();
        SeckillMessage seckillMessage = mapper.readValue(str, SeckillMessage.class);

        long userId = seckillMessage.getUserId();
        long skGoodsId = seckillMessage.getSkGoodsId();

        // 直接查数据库的库存是否充足
        GoodsSeckill goodsSeckill = goodsSeckillService.selectById(skGoodsId);
        if (goodsSeckill.getStockCount() < 0) {
            log.info("No Stock.");
            return;
        }

        // 判断重复秒杀(根据用户id+商品id, 一个用户对同一个商品至多秒杀一次）
        String res = RedisUtil.get(SeckillOrderKey.USER_ORDER, userId + ":" + skGoodsId);
        if (!StringUtils.isEmpty(res)) {
            log.info("MQ-repeat seckill.");
            return;
        }

        // 减库存,写订单
        boolean result = goodsSeckillService.doSeckill(userId, goodsSeckill);
        if (result) {
            log.info("userId:" + userId + ",goods:" + goodsSeckill.getId() + ", success");
        } else {
            log.info("userId:" + userId + ",goods:" + goodsSeckill.getId() + ", no stock ");
        }

        // 消息已经成功处理,直接确认删除
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
