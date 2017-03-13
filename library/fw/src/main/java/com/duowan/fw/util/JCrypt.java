package com.duowan.fw.util;

import android.os.Build;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Design by JerryZhou@outlook.com, v3.0.0
 * NB! NO SUPPORT Crypt in android 4.4
 * */
public class JCrypt {
	
    
	public static String encrypt(String seed, String cleartext) throws Exception {

        byte[] rawKey;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
             rawKey = deriveKeyInsecurely(seed,32).getEncoded();
        }else{
             rawKey = getRawKey(seed.getBytes());
        }


        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);    
    }    
        
    public static String decrypt(String seed, String encrypted) throws Exception {

        byte[] rawKey;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            rawKey = deriveKeyInsecurely(seed,32).getEncoded();
        }else{
            rawKey = getRawKey(seed.getBytes());
        }

        byte[] enc = toByte(encrypted);    
        byte[] result = decrypt(rawKey, enc);    
        return new String(result);
    }
//


    private static SecretKey deriveKeyInsecurely(String password, int
            keySizeInBytes) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.US_ASCII);
        return new SecretKeySpec(
                InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(
                        passwordBytes, keySizeInBytes),
                "AES");
    }

    
    /**
     * 获得Key
     * @param password
     * @return
     */
    public static SecretKeySpec setSecretKey(String seed, String ALGORITHM) throws Exception {
        SecretKeySpec key = null;
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed.getBytes());
        kgen.init(128, sr);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        key = new SecretKeySpec(enCodeFormat, ALGORITHM);

        return key;
    }
   
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }    
   
        
    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {    
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;    
    }    
   
    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {    
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");    
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);    
        return decrypted;    
    }    
   
    public static String toHex(String txt) {    
        return toHex(txt.getBytes());    
    }    
    public static String fromHex(String hex) {    
        return new String(toByte(hex));    
    }    
        
    public static byte[] toByte(String hexString) {    
        int len = hexString.length()/2;    
        byte[] result = new byte[len];    
        for (int i = 0; i < len; i++)    
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();    
        return result;    
    }    
   
    public static String toHex(byte[] buf) {    
        if (buf == null)    
            return "";    
        StringBuffer result = new StringBuffer(2*buf.length);    
        for (int i = 0; i < buf.length; i++) {    
            appendHex(result, buf[i]);    
        }    
        return result.toString();    
    }    
    private final static String HEX = "0123456789ABCDEF";    
    private static void appendHex(StringBuffer sb, byte b) {    
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));    
    } 
}

