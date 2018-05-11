package com.megvii.player;

public class ApiConfig {

    private static final String BASE_URL = "http://110.16.71.162:10800/";

    /**获取播放地址接口，播放单个通道直播*/
    public static final String URL_GET_CHANNEL_STREAM = BASE_URL + "api/v1/getchannelstream";
    /**视频播放心跳*/
    public static final String URL_GET_HEARTBEAT = BASE_URL + "api/v1/touchchannelstream";

}
