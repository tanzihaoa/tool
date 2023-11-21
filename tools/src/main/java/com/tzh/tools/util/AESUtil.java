package com.tzh.tools.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


public class AESUtil {

    public static String filteNull(String str) {
        return str == null ? "" : str;
    }

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static String encrypt(String content, String password) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }

        if (password.length() > 16) {
            return "";
        } else if (password.length() < 16) {

            int gapLength = 16 - password.length();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < gapLength; i++) {
                stringBuffer.append("0");
            }
            password = stringBuffer.toString() + password;

        }

        try {
            IvParameterSpec zeroIv = new IvParameterSpec(password.getBytes());
            SecretKeySpec key1 = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key1, zeroIv);
            byte[] encryptedData = cipher.doFinal(content.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            String encryptResultStr = encoder.encode(encryptedData);
            encryptResultStr = replaceBlank(encryptResultStr);
            return encryptResultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content  AES加密过过的内容
     * @param password 加密时的密码
     * @return 明文
     */
    public static String decrypt(String content, String password) {

        if (password.length() > 16) {
            return "";
        }

        if (password.length() < 16) {

            int gapLength = 16 - password.length();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < gapLength; i++) {
                stringBuffer.append("0");
            }

            password = stringBuffer.toString() + password;

        }

        try {
            IvParameterSpec zeroIv = new IvParameterSpec(password.getBytes());
            byte[] raw = password.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, zeroIv);
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] original = cipher.doFinal(decoder.decodeBuffer(content));
            return new String(original);
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
            return content;
        }
        catch (Exception e) {
            e.printStackTrace();
            return content;
        }

    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


}