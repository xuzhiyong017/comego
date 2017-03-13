package com.duowan.fw.util;


public class EncryptCipher {
	private RC4 mEncrypt;
	private RC4 mDecrypt;
	
	public EncryptCipher(byte[] rc4key) {
		mEncrypt = new RC4(rc4key);
		mDecrypt = new RC4(rc4key);
	}
	
	public byte[] encrypt(byte[] data) {
		mEncrypt.doFinal(data);
		return data;
	}
	
	public byte[] decrypt(byte[] data) {
		mDecrypt.doFinal(data);
		return data;
	}
	
	public void encrypt(byte[] data, int start, int count) {
		mEncrypt.doFinal(data, start, count);
	}
	
	public void decrypt(byte[] data, int start, int count) {
		mDecrypt.doFinal(data, start, count);
	}
}
