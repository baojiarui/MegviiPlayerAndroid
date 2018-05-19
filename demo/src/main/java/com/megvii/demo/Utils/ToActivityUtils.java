package com.megvii.demo.Utils;

import android.app.Activity;
import android.content.Intent;

import com.megvii.demo.R;
import com.megvii.player.play.PlayActivity;

public class ToActivityUtils {

    /**
     * 跳转到视频播放
     *
     * @param activity
     */
    public static void goToVideoPlayer(Activity activity, String url) {
        Intent intent = new Intent(activity, PlayActivity.class);
        intent.putExtra(PlayActivity.INTENT_KEY_URL, url);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

}
