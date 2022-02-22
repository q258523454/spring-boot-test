package com.sec.pojo.dto;

import lombok.Data;

@Data
public class SeckillMessage {

    private Long userId;

    /**
     * 秒杀商品id
     */
    private long skGoodsId;
}
