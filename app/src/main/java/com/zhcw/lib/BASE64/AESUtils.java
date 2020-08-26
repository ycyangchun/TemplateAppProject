package com.zhcw.lib.BASE64;

import android.text.TextUtils;


import org.springside.modules.utils.security.CryptoUtil;

import java.io.IOException;

/**
 * CopyRight (c)1999-2019 : zhcw.com
 * Project :  assistant
 * Comments : <对此类的描述，可以引用系统设计中的描述>
 * JDK version : JDK1.8
 * Create Date : 2019/05/21 15:52
 *
 * @author : gongchen
 * @version : 1.0
 * @since : 1.0
 */
public class AESUtils {

  private static final String AES_KEY = "X7M@dgH936Vrl!ib";
  private static final String IV_KEY = "pV@eixygV3jRsPY4";
  /**
   * aes加密
   * 加密基类方法步骤:CCCrypt函数加密，base64编码，替换某些字符，得到加密的数据
   * @param aesStr
   * @return
   */
  public static String aesEncrypt(String aesStr){
    if (TextUtils.isEmpty(aesStr)){
      return "";
    }

    byte[] bytes2 = CryptoUtil.aesEncrypt(aesStr.getBytes(),AES_KEY.getBytes(),IV_KEY.getBytes());

    String bytes = (new BASE64Encoder()).encode(bytes2);

    return encodeSpecialLetter(bytes);
  }

  /**
   * aes解密
   * 解密基类方法步骤:替换某些字符，base64编码，CCCrypt函数解密，得到原本的数据
   * @param aesStr
   * @return
   */
  public static String aesDecrypt(String aesStr){
    String str = "";
    try {
      if (TextUtils.isEmpty(aesStr)){
        return str;
      }
      String aes =  decodeSpecialLetter(aesStr);
      BASE64Decoder decoder = new BASE64Decoder();
      byte[] byteMi = decoder.decodeBuffer(aes);
      str = CryptoUtil.aesDecrypt(byteMi,AES_KEY.getBytes(),IV_KEY.getBytes());

    } catch (IOException e) {
      e.printStackTrace();
    }finally {
      return str;
    }
  }

  /**
   * 转换*号为 -x-，
   为了防止有违反协议的字符，-x 转换为-xx
   * @param str
   * @return
   */
  private static String encodeSpecialLetter(String str){
    //转成针对url的base64编码
    str = str.replaceAll("\\=", "!");
    str = str.replaceAll("\\+", "*");
    str = str.replaceAll("\\/", "-");
    //去除换行
    str = str.replaceAll("\\n", "");
    str = str.replaceAll("\\r", "");
    str = str.replaceAll("\\-x", "-xx");
    str = str.replaceAll("\\*", "-x-");
    return str;
  }

  /**
   * 转换 -x-号为*，-xx转换为-x
   * @param str
   * @return
   */
  private static String decodeSpecialLetter(String str){
    str = str.replaceAll("\\-x-", "*");
    str = str.replaceAll("\\-xx", "-x");
    str = str.replaceAll("\\!", "=");
    str = str.replaceAll("\\*", "+");
    str = str.replaceAll("\\-", "/");
    return str;
  }
  
}
