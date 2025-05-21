package shiro.service.impl;

import shiro.pojo.vo.Resp;
import shiro.service.RBACService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserLoginApiService implements RBACService {


    @Override
    public ResponseEntity<Resp> login(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 执行认证登陆
        subject.login(token);
        return ResponseEntity.ok(new Resp("登录成功"));
    }

    @Override
    public ResponseEntity<Resp> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResponseEntity.ok(new Resp("登出成功"));
    }

}
