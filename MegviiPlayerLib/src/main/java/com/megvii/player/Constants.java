package com.megvii.player;

import android.os.Environment;

import java.io.File;

public class Constants {

    /**缓存目录*/
    public static final String PATH_CACHE = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MegviiPlayerCache";
}
