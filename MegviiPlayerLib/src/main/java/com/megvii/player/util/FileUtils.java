package com.megvii.player.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.megvii.player.BuildConfig;

import java.io.File;

/**
 * Created by baojiarui on 2018/5/21.
 */

public class FileUtils {

    /***
     * 检查文件夹是否存在，不存在则创建
     * @param path
     */
    public static void checkFileExists(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }
}
