package com.megvii.player.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by baojiarui on 2018/5/18.
 */

public class Base64Utils {

    /**
     * 加密
      * @param str
     * @return
     */
    public static String getBase64(String str) {
        String result = "";
        if (str != null) {
            try {
                result = new String(Base64.encode(str.getBytes("utf-8"), Base64.DEFAULT), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 解密
      * @param str
     * @return
     */
    public static String getFromBase64(String str) {
        String result = "";
        if (str != null) {
            try {
                result = new String(Base64.decode(str, Base64.NO_WRAP), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
