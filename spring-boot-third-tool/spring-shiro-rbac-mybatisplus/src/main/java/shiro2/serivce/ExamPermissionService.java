package shiro2.serivce;

import com.baomidou.mybatisplus.extension.service.IService;

import shiro2.pojo.entity.ExamPermission;

import java.util.List;

public interface ExamPermissionService extends IService<ExamPermission> {

    List<ExamPermission> selectPermissionByRoleId(Integer roleId);

}
