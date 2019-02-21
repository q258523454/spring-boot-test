package com.encryption.utils;

public class AESTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String src = "rongshukeji";
		String encrypted = AESUtil.encrypt(src,"a");
		String decrypted = AESUtil.decrypt(encrypted,"a");
		System.out.println("src: " + src);
		System.out.println("encrypted: " + encrypted);
		System.out.println("decrypted: " + decrypted);
	}

}
