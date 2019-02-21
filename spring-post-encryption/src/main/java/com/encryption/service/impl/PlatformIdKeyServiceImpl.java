package com.encryption.service.impl;

import com.encryption.dao.PlatformIdKeyMapper;
import com.encryption.entity.PlatformIdkey;
import com.encryption.service.PlatformIdKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-14
 */

@Service
public class PlatformIdKeyServiceImpl implements PlatformIdKeyService {

    @Autowired
    private PlatformIdKeyMapper platformIdKeyMapper;

    @Override
    public List<PlatformIdkey> selectAllPlatformIdkey() {
        return platformIdKeyMapper.selectAllPlatformIdkey();
    }
}
