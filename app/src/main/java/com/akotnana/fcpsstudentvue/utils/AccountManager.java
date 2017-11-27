package com.akotnana.gradeview.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * Created by anees on 11/21/2017.
 */

public class AccountManager {
    public static final int SAVE_CREDENTIALS_REQUEST_CODE = 1;
    private static final int LOGIN_WITH_CREDENTIALS_REQUEST_CODE = 2;

    public static final int AUTHENTICATION_DURATION_SECONDS = 30;

    public static final String KEY_NAME = "key";

    public static final String TRANSFORMATION = KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
            + KeyProperties.ENCRYPTION_PADDING_PKCS7;
    public static final String CHARSET_NAME = "UTF-8";
    public static final String STORAGE_FILE_NAME = "credentials";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    Context context;

    public AccountManager(Context context) {
        this.context = context;
    }

    public void saveCredentials(String username, String password) {
        String key1 = "";
        if (new DataStorage(context).getData("createdKey") != "1") {
            key1 = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
            new DataStorage(context).storeData("key", key1, true);
            new DataStorage(context).storeData("createdKey", "1", true);
        } else {
            key1 = new DataStorage(context).getData("key");
        }
        Key key = null;
        try {
            key = generateKey(key1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cipher c = null;
        try {
            c = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            c.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] encVal = new byte[0];
        try {
            encVal = c.doFinal(password.getBytes());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String encryptedPassword = Base64.encodeToString(encVal, Base64.DEFAULT);
        new DataStorage(context).storeData("password", encryptedPassword, true);
        new DataStorage(context).storeData("username", username, true);
    }

    private Key generateKey(String key1) throws Exception {
        return new SecretKeySpec(key1.getBytes(), "AES");
    }

    public String[] retrieveCredentials() {
        String key1 = new DataStorage(context).getData("key");
        String encryptedPassword = new DataStorage(context).getData("password");
        String username = new DataStorage(context).getData("username");
        Key key = null;
        try {
            key = generateKey(key1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cipher c1 = null;
        try {
            c1 = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            c1.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] decordedValue = Base64.decode(encryptedPassword.getBytes(), Base64.DEFAULT);
        byte[] decValue = new byte[0];
        try {
            decValue = c1.doFinal(decordedValue);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String password = new String(decValue);
        return new String[]{username,password};
    }
}
