package com.megvii.demo.Utils;

import com.megvii.player.ApiConfig;
import com.megvii.player.util.Base64Utils;
import com.megvii.player.util.MD5Utils;

public class AuthUtils {

    /**
     * Call GET /api/getNonce
     In response you'll get a JSON object with realm and nonce
     Translate user's username to the lower case
     Check the required method ("GET" for HTTP requests, "PLAY" for RTSP)
     digest = md5_hex(user_name + ":" + realm + ":" + password)
     partial_ha2 = md5_hex(method + ":")
     simplified_ha2 = md5_hex(digest + ":" + nonce + ":" + partial_ha2)
     auth_digest = base64(user_name + ":" + nonce + ":" + simplified_ha2)
     Here auth_digest is the required authentication hash
     */
    public static String getAuth(String realm, String nonce, String method){
        String authDigest;
        String digest = MD5Utils.getMD5(ApiConfig.USER_NAME + ":" + realm + ":" + ApiConfig.USER_PWD);
        String partial_ha2 = MD5Utils.getMD5(method + ":");
        String simplified_ha2 = MD5Utils.getMD5(digest + ":" + nonce + ":" + partial_ha2);
        authDigest = Base64Utils.getBase64(ApiConfig.USER_NAME+ ":" + nonce + ":" + simplified_ha2);
        return authDigest.trim();
    }
}
