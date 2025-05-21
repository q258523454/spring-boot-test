package shiro;


import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class ShiroTest {

    private static Object encrypt(String userName, String enablePassword) {
        String hashAlgorithmName = "md5";//加密算法
        String passwordSalt = "5371f568a45e5ab1f442c38e0932aef24447139b";//密钥
        String salt = passwordSalt + userName + passwordSalt; //盐值
        int hashIterations = 1024; //散列次数
        ByteSource credentialsSalt = ByteSource.Util.bytes(salt);//盐
        return new SimpleHash(hashAlgorithmName, enablePassword, credentialsSalt, hashIterations);
    }


    // 加密
    public static void main(String[] args) {
        Object zhangsanPassword = encrypt("zhangsan", "12345");
        System.out.println("加密 : " + zhangsanPassword);
    }
}

