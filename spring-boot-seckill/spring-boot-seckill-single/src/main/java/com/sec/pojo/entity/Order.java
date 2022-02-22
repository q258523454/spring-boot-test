package com.sec.pojo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    private Long id;

    private Long userId;

    private Long orderId;

    private Long goodsId;

    private Long skGoodsId;

    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}