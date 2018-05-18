package com.megvii.player.model;

import java.io.Serializable;

/**
 * Created by baojiarui on 2018/5/18.
 */

public class VideoPara implements Serializable {

    /**
     * error : 0
     * errorString :
     * reply : {"nonce":"56c7910f35778","realm":"VMS"}
     */

    private String error;
    private String errorString;
    private ReplyBean reply;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public ReplyBean getReply() {
        return reply;
    }

    public void setReply(ReplyBean reply) {
        this.reply = reply;
    }

    public static class ReplyBean {
        /**
         * nonce : 56c7910f35778
         * realm : VMS
         */

        private String nonce;
        private String realm;

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getRealm() {
            return realm;
        }

        public void setRealm(String realm) {
            this.realm = realm;
        }
    }
}
