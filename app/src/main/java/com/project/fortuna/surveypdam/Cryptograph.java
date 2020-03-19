package com.project.fortuna.surveypdam;

import org.apache.commons.net.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Akautsar Efendi on 1/10/2018.
 */

public class Cryptograph {
    private static Cryptograph instance;
    private static byte[] keyBytes = null;
    private static final String UNICODE_FORMAT = "UTF-8";
    private static final String ALGORITHM = "DESede";
    private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";
    private SecretKey key;
    private Cipher cipher;
    private IvParameterSpec iv;

    public static Cryptograph getInstance() {
        if(instance == null) {
            instance = new Cryptograph();
        }
        return instance;
    }

    public Cryptograph() {
        try {
            keyBytes = "HG58YZ3CR9UI6PYUIO1K&HJ&".getBytes(UNICODE_FORMAT);
            key = new SecretKeySpec(keyBytes, ALGORITHM);
            cipher = Cipher.getInstance(TRANSFORMATION);
            iv = new IvParameterSpec(new byte[8]);
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        } catch (UnsupportedEncodingException e) {
        }
    }

    public String encrypt(String plainText) {
        String chiperText = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] plainTextBytes = plainText.getBytes(UNICODE_FORMAT);
            byte[] chiperTextBytes = cipher.doFinal(plainTextBytes);
            chiperText = new Base64().encodeToString(chiperTextBytes).trim();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return chiperText;
    }

    public String decrypt(String chiperText) {
        String plainText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] chiperTextBytes = new Base64().decode(chiperText.getBytes(UNICODE_FORMAT));
            byte[] plainTextBytes = cipher.doFinal(chiperTextBytes);
            plainText = new String(plainTextBytes, UNICODE_FORMAT);
        }  catch (InvalidKeyException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (BadPaddingException e) {
        } catch (InvalidAlgorithmParameterException e) {
        }
        return plainText;
    }
}
