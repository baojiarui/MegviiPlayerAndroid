package com.megvii.player;

public class ApiConfig {

    public static final String BASE_URL = "http://110.16.71.162:7001/";
    public static final String BASE_RTSP = "rtsp://110.16.71.162:7001/";

    /***/
    public static final String URL_GET_NONCE = BASE_URL + "api/getNonce";

    /**获取摄像头列表*/
    public static final String URL_GET_CAMERAS = BASE_URL + "ec2/getCamerasEx";

}
