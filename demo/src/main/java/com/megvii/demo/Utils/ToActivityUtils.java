package com.megvii.demo.Utils;

import android.app.Activity;
import android.content.Intent;

import com.megvii.demo.R;
import com.megvii.player.play.PlayActivity;

public class ToActivityUtils {

    public static void goToVideoPlayer(Activity activity, String url){
        goToVideoPlayer(activity, url, false);
    }

    /**
     * 跳转到视频播放
     *
     * @param activity
     * @param url 播放链接
     * @param isCache 是否缓存到本地
     */
    public static void goToVideoPlayer(Activity activity, String url, boolean isCache) {
        Intent intent = new Intent(activity, PlayActivity.class);
        intent.putExtra(PlayActivity.INTENT_KEY_URL, url);
        intent.putExtra(PlayActivity.INTENT_KEY_CACHE, isCache);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

}
