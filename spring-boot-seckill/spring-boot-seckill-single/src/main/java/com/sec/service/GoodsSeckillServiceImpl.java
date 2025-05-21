package com.sec.service;

import com.sec.dao.GoodsMapper;
import com.sec.dao.GoodsSeckillMapper;
import com.sec.dao.OrderInfoMapper;
import com.sec.dao.OrderMapper;
import com.sec.pojo.dto.GoodsSeckillDetail;
import com.sec.pojo.entity.Goods;
import com.sec.pojo.entity.GoodsSeckill;
import com.sec.pojo.entity.Order;
import com.sec.pojo.entity.OrderInfo;
import com.sec.pojo.entity.User;
import com.sec.redis.SeckillKey;
import com.sec.redis.SeckillOrderKey;
import com.sec.service.impl.GoodsSeckillService;
import com.sec.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class GoodsSeckillServiceImpl implements GoodsSeckillService {

    @Resource
    private GoodsSeckillMapper goodsSeckillMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public int insert(GoodsSeckill record) {
        return goodsSeckillMapper.insert(record);
    }

    @Override
    public int insertSelective(GoodsSeckill record) {
        return goodsSeckillMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKey(GoodsSeckill record) {
        return goodsSeckillMapper.updateByPrimaryKey(record);
    }

    @Override
    public GoodsSeckill selectById(Long id) {
        return goodsSeckillMapper.selectById(id);
    }

    @Override
    public List<GoodsSeckill> selectAll() {
        return goodsSeckillMapper.selectAll();
    }

    @Override
    public List<GoodsSeckillDetail> selectAllGoodsSeckillDetail() {
        return goodsSeckillMapper.selectAllGoodsSeckillDetail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean doSeckill(long userId, GoodsSeckill goodsSeckill) {
        boolean success = false;
        try {
            // 减库存
            success = reduceStock(goodsSeckill);
        } catch (Exception ex) {
            log.error("reduce stock occur error exception.");
            throw ex;
        }

        if (success) {
            // 创建订单
            createOrder(userId, goodsSeckill);
            return true;
        } else {
            // 库存不足,暂不考虑到取消支付,退单情况
            // GoodsSeckill record = goodsSeckillMapper.selectById(goodsSeckill.getId());
            //if (record.getStockCount() <= 0) {
            //    log.warn("Goods stock is over");
            //    RedisUtil.set(SeckillKey.GOODS_OVER, "" + goodsSeckill.getId(), "true");
            //}
            RedisUtil.set(SeckillKey.GOODS_OVER, "" + goodsSeckill.getId(), "true");
            return false;
        }
    }

    /**
     * 减库存
     */
    private boolean reduceStock(GoodsSeckill goodsSeckill) {
        // 尝试次数
        int attempts = 3;
        // 影响行数(是否更新成功)
        int affectRows = 0;
        GoodsSeckill record = new GoodsSeckill();
        record.setId(goodsSeckill.getId());
        do {
            // 无需用version作乐观锁, innodb的数据库update本身是X锁, 只需判断 stock>0.
            affectRows = goodsSeckillMapper.reduceStockById(record);

            // 更新成功
            if (affectRows > 0) {
                return true;
            }
            attempts--;
        } while (attempts > 0);

        return false;
    }


    /**
     * 创建订单
     */
    private void createOrder(long userId, GoodsSeckill goodsSeckill) {
        // 查询商品信息
        Goods goods = goodsMapper.selectByPrimaryKey(goodsSeckill.getGoodsId());

        // sk_order_info 订单详情表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setGoodsId(goodsSeckill.getGoodsId());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsPrice(goods.getGoodsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        // 自增id返回
        orderInfoMapper.insert(orderInfo);

        // sk_order 订单表
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderId(orderInfo.getId());
        order.setGoodsId(goods.getId());
        order.setSkGoodsId(goodsSeckill.getId());
        orderMapper.insert(order);

        RedisUtil.set(SeckillOrderKey.USER_ORDER, userId + ":" + goodsSeckill.getId(), "" + order.getId());
    }

    /**
     * 查询结果
     */
    @Override
    public String getSeckillResult(User user, long skGoodsId) {
        String orderId = RedisUtil.get(SeckillOrderKey.USER_ORDER, user.getId() + ":" + skGoodsId);
        if (!StringUtils.isEmpty(orderId)) {
            return "success, order id is:" + orderId;
        } else {
            // 是否已售罄
            boolean over = RedisUtil.hasKey(SeckillKey.GOODS_OVER, "" + skGoodsId);
            return over ? "goods over" : "seckill is doing";
        }
    }

}

