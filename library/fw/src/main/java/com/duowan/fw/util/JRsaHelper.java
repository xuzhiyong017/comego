package com.duowan.fw.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class JRsaHelper {
	private boolean mKeyIsValid = false;
	private RSAPublicKey mRSAPublickey = null;
	private RSAPrivateKey mRSAPrivatekey = null;
	private Cipher mDecryptCipher = null;
	private static JRsaHelper theKey = null;

	public JRsaHelper() {
	}

	public static JRsaHelper generate() {
		if(theKey == null)
		{
			synchronized(JRsaHelper.class) {
				if(theKey == null)
				{
					JRsaHelper key = new JRsaHelper();
					key.mKeyIsValid = false;
					try {
						KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
						RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(512, RSAKeyGenParameterSpec.F4);
						keyGen.initialize(spec);
						KeyPair pair = keyGen.generateKeyPair();
						key.mRSAPublickey = (RSAPublicKey) pair.getPublic();
						key.mRSAPrivatekey = (RSAPrivateKey) pair.getPrivate();
						key.mDecryptCipher = Cipher.getInstance("RSA/None/PKCS1Padding");
						key.mDecryptCipher.init(Cipher.DECRYPT_MODE, key.mRSAPrivatekey);
						key.mKeyIsValid = true;
						theKey = key;
					} catch (NoSuchAlgorithmException e) {
						JLog.error(JRsaHelper.class, e);
					} catch (InvalidAlgorithmParameterException e) {
						JLog.error(JRsaHelper.class, e);
					} catch (NoSuchPaddingException e) {
						JLog.error(JRsaHelper.class, e);
					} catch (InvalidKeyException e) {
						JLog.error(JRsaHelper.class, e);
					}
					//return key;
				}
			}
		}
		return theKey;
	}

	public boolean keyIsValid() {
		return this.mKeyIsValid;
	}

	public RSAPublicKey getPublicKey() {
		if (mKeyIsValid)
			return mRSAPublickey;
		else
			return null;
	}

	public synchronized byte[] decryptData(byte[] data) {
		if (!mKeyIsValid)
			return null;
		try {
			return mDecryptCipher.doFinal(data);
		} catch (IllegalBlockSizeException e) {
			JLog.error(this, e);
		} catch (BadPaddingException e) {
			JLog.error(this, e);
		}
		return null;
	}
}