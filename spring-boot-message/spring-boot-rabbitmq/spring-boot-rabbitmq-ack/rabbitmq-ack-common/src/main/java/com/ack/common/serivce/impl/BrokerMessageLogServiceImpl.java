package com.ack.common.serivce.impl;

import com.ack.common.dao.BrokerMessageLogMapper;
import com.ack.common.entity.BrokerMessageLog;
import com.ack.common.serivce.BrokerMessageLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BrokerMessageLogServiceImpl implements BrokerMessageLogService {

    @Resource
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Override
    public int deleteByPrimaryKey(String messageId) {
        return brokerMessageLogMapper.deleteByPrimaryKey(messageId);
    }

    @Override
    public int insert(BrokerMessageLog record) {
        return brokerMessageLogMapper.insert(record);
    }

    @Override
    public int insertSelective(BrokerMessageLog record) {
        return brokerMessageLogMapper.insertSelective(record);
    }

    @Override
    public BrokerMessageLog selectByPrimaryKey(String messageId) {
        return brokerMessageLogMapper.selectByPrimaryKey(messageId);
    }

    @Override
    public int updateByPrimaryKeySelective(BrokerMessageLog record) {
        return brokerMessageLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(BrokerMessageLog record) {
        return brokerMessageLogMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateBatch(List<BrokerMessageLog> list) {
        return brokerMessageLogMapper.updateBatch(list);
    }

    @Override
    public List<BrokerMessageLog> selectByStatusAndNextRetry(BrokerMessageLog record) {
        return brokerMessageLogMapper.selectByStatusAndNextRetry(record);
    }

    @Override
    public List<BrokerMessageLog> selectAll() {
        return brokerMessageLogMapper.selectAll();
    }

}
