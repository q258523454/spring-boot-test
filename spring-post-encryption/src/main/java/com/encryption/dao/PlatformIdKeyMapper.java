package com.encryption.dao;

import com.encryption.entity.PlatformIdkey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-31
 */

@Repository
@Mapper
public interface PlatformIdKeyMapper {

    List<PlatformIdkey> selectAllPlatformIdkey();
}
