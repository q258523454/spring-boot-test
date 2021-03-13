package com.ack.common.serivce;

import com.ack.common.entity.BrokerMessageLog;

import java.util.List;

public interface BrokerMessageLogService {


    int deleteByPrimaryKey(String messageId);

    int insert(BrokerMessageLog record);

    int insertSelective(BrokerMessageLog record);

    BrokerMessageLog selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokerMessageLog record);

    int updateByPrimaryKey(BrokerMessageLog record);

    int updateBatch(List<BrokerMessageLog> list);

    List<BrokerMessageLog> selectByStatusAndNextRetry(BrokerMessageLog record);

    List<BrokerMessageLog> selectAll();
}
