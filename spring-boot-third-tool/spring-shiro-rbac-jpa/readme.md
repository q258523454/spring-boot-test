代码结构参考 :   
[https://github.com/EalenXie/springcloud-microservice-ddd](https://github.com/EalenXie/springcloud-microservice-ddd)

博客链接 :   
[https://www.cnblogs.com/ealenxie/p/10610741.html](https://www.cnblogs.com/ealenxie/p/10610741.html)




测试步骤:  
http://localhost:8080/login  
zhangsan  
12345  
登录  

有权限  
http://localhost:8080/add  
{  
新增页面  
}  


用户未授权
http://localhost:8080/unauthorized
{  
用户 未授权  
}  

http://localhost:8080/requiresAuthentication  
{  
"msg": "Require Authentication : 需要认证 测试, 能够访问此接口"  
}  


http://localhost:8080/requiresPermissionsAdd  
{  
"msg": "require Permissions : 该用户具有 add 权限 , 能够访问此接口"  
}  


http://localhost:8080/requiresRoleAdministrator  
{  
"localizedMessage": "Subject does not have role [administrator]",  
"message": "用户无角色(权限)访问此接口",  
"throwableTime": "2022-09-08 15:31:57",  
"throwable": "org.apache.shiro.authz.UnauthorizedException: Subject does not have role [administrator]"  
}  