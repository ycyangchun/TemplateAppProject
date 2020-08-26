package com.zhcw.lib.BASE64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DesUtil {

    public static final String password = "12345678";

    public static String encryptDES(String encryptString, String encryptKey) {
        String enStr = encryptString;
        try {
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));

            String encodeForUrl = (new BASE64Encoder()).encode(encryptedData);
            //转成针对url的base64编码
            encodeForUrl = encodeForUrl.replaceAll("\\=", "!");
            encodeForUrl = encodeForUrl.replaceAll("\\+", "*");
            encodeForUrl = encodeForUrl.replaceAll("\\/", "-");
            //去除换行
            encodeForUrl = encodeForUrl.replaceAll("\\n", "");
            encodeForUrl = encodeForUrl.replaceAll("\\r", "");
            //转换*号为 -x-
            //防止有违反协议的字符
            enStr = encodeSpecialLetter(encodeForUrl);
        } catch (Exception e) {

        } finally {
            return enStr;
        }

    }

    public static String decryptDES(String decryptString, String decryptKey) {
        String deStr = decryptString;
        try {
            decryptString = decodeSpecialLetter(decryptString);
            decryptString = decryptString.replaceAll("\\!", "=");
            decryptString = decryptString.replaceAll("\\*", "+");
            decryptString = decryptString.replaceAll("\\-", "/");


            BASE64Decoder decoder = new BASE64Decoder();
            byte[] byteMi = decoder.decodeBuffer(decryptString);
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedData = cipher.doFinal(byteMi);
            deStr = new String(decryptedData, "UTF-8");
        } catch (Exception e) {

        } finally {
            return deStr;
        }

    }


    /**
     * 转换*号为 -x-，
     * 为了防止有违反协议的字符，-x 转换为-xx
     *
     * @param str
     * @return
     */
    private static String encodeSpecialLetter(String str) {
        str = str.replaceAll("\\-x", "-xx");
        str = str.replaceAll("\\*", "-x-");
        return str;
    }

    /**
     * 转换 -x-号为*，-xx转换为-x
     *
     * @param str
     * @return
     */
    private static String decodeSpecialLetter(String str) {
        str = str.replaceAll("\\-x-", "*");
        str = str.replaceAll("\\-xx", "-x");
        return str;
    }

}