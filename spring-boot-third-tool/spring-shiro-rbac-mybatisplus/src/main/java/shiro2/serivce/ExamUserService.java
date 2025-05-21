package shiro2.serivce;

import com.baomidou.mybatisplus.extension.service.IService;

import shiro2.pojo.entity.ExamUser;

import java.util.List;

public interface ExamUserService extends IService<ExamUser> {

    List<ExamUser> selectUserByName(String username);
}



