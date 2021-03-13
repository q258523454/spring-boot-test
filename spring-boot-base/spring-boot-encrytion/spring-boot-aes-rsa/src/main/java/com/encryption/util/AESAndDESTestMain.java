package com.encryption.util;

public class AESAndDESTestMain {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String data = "zhangxiaofan";
//        String encryptionKey = "0123456789123456"; // 128
//        String encryptionKey = "012345678901234567891234"; // 192
//        String encryptionKey = "01234567890123456789012345678912"; // 256
//        String encryptionKey = "de674dbf26004abd692ee15dd11ccb94";
        String encryptionKey = AESUtil.genKey(); //
        System.out.println(encryptionKey);

        String encrypted = AESUtil.encrypt(data, encryptionKey);
        String decrypted = AESUtil.decrypt(encrypted, encryptionKey);
        System.out.println("src: " + data);
        System.out.println("encryptionKey: " + encryptionKey);
        System.out.println("encrypted: " + encrypted);
        System.out.println("decrypted: " + decrypted);

        String data2 = "zhangxiaofan";
//        String encryptionKey2 = "12345678";
        String encryptionKey2 = "12345678";
        String encrypted2 = DESUtil.encrypt(data2, encryptionKey2);
        String decrypted2 = DESUtil.decrypt(encrypted2, encryptionKey2);
        System.out.println("src2: " + data);
        System.out.println("encryptionKey2: " + encryptionKey2);
        System.out.println("encrypted2: " + encrypted2);
        System.out.println("decrypted2: " + decrypted2);


        String test = "cMtmU8zLGQJfQDTvXPPlPjXCJYiFJF5oWAmhhjgFlwBK68W7pvnFwWwG0X9FK9dQFqqVINHoWvy1\n" +
                "kfeF7GY7cg==";
        System.out.println(AESUtil.decrypt(test, "bfcf1ba4d7059352613588effbfe8f0b"));
        System.exit(0);
    }

}
