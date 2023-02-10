package me.electronicsboy.yapca.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
//import java.security.spec.KeySpec;
//import java.util.Base64;
//
//import javax.crypto.Cipher;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.DESedeKeySpec;

public class Crypto {
    public static String getSHA256(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return StringUtil.toHex64String(md.digest(input.getBytes(StandardCharsets.UTF_8)));
    }

//    public static String encrypt(String unencryptedString, String keys) throws Exception {
//        KeySpec ks;
//        SecretKeyFactory skf;
//        Cipher cipher;
//        byte[] arrayBytes;
//        SecretKey key;
//        arrayBytes = keys.getBytes("UTF-8");
//        ks = new DESedeKeySpec(arrayBytes);
//        skf = SecretKeyFactory.getInstance("DESede");
//        cipher = Cipher.getInstance("DESede");
//        key = skf.generateSecret(ks);
//        String encryptedString = null;
//        try {
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            byte[] plainText = unencryptedString.getBytes("UTF-8");
//            byte[] encryptedText = cipher.doFinal(plainText);
//            encryptedString = new String(Base64.encode(encryptedText));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return encryptedString;
//    }


//    public static String decrypt(String encryptedString) {
//        String decryptedText=null;
//        try {
//            cipher.init(Cipher.DECRYPT_MODE, key);
//            byte[] encryptedText = Base64.decodeBase64(encryptedString);
//            byte[] plainText = cipher.doFinal(encryptedText);
//            decryptedText= new String(plainText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return decryptedText;
//    }

//    public static byte[] encrypt(String plaintext, String encrptionKey) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
//        Cipher cipher = null;
//        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encrptionKey.getBytes(), "AES"));
//        byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));
//        return cipherText;
//    }
//
//    public static String decrypt(byte[] ciphertext, String decryptionKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException {
//        Cipher cipher = null;
//        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptionKey.getBytes(), "AES"));
//        String decryptString = new String(cipher.doFinal(ciphertext), "UTF-8");
//        return decryptString;
//    }
    private static String INIT_VECTOR = "encryptionIntVec";

    public static String encrypt(String value, String SECRET_KEY) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String value, String SECRET_KEY) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
