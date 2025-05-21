package shiro2.serivce;

import com.baomidou.mybatisplus.extension.service.IService;

import shiro2.pojo.entity.ExamRole;

import java.util.List;

public interface ExamRoleService extends IService<ExamRole> {

    List<ExamRole> selectRoleByUserId(Long userId);
}

