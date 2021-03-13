package com.ack.common.dao;

import com.ack.common.entity.BrokerMessageLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
public interface BrokerMessageLogMapper {
    int deleteByPrimaryKey(String messageId);

    int insert(BrokerMessageLog record);

    int insertSelective(BrokerMessageLog record);

    BrokerMessageLog selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokerMessageLog record);

    int updateByPrimaryKey(BrokerMessageLog record);

    int updateBatch(List<BrokerMessageLog> list);

    List<BrokerMessageLog> selectByStatusAndNextRetry(BrokerMessageLog record);

    List<BrokerMessageLog> selectAll();

    BrokerMessageLog selectByEntity(BrokerMessageLog record);

}