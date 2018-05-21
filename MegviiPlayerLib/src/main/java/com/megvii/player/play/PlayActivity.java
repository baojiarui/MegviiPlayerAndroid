package com.megvii.player.play;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.megvii.player.Constants;
import com.megvii.player.R;
import com.megvii.player.cache.DownloadUtil;
import com.megvii.player.model.SwitchVideoModel;
import com.megvii.player.util.FileUtils;
import com.megvii.player.video.SampleVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 单独的视频播放页面
 */
public class PlayActivity extends AppCompatActivity {

    public final static String INTENT_KEY_URL = "URL";
    public final static String INTENT_KEY_CACHE = "CACHE";

    private SampleVideo videoPlayer;
    private OrientationUtils orientationUtils;

    /**视频链接*/
    private String mUrl;
    /**是否缓存到本地*/
    private boolean isCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);
        initView();

        mUrl = getIntent().getStringExtra(INTENT_KEY_URL).trim();
        isCache = getIntent().getBooleanExtra(INTENT_KEY_CACHE, false);

        play();
    }

    private void initView(){
        videoPlayer = findViewById(R.id.video_player);
    }

    private void initPlayer(String url) {
        String name = "普通";
        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, url);

        List<SwitchVideoModel> list = new ArrayList<>();
        list.add(switchVideoModel);

        videoPlayer.setUp(list, false, "");

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);

        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        videoPlayer.startPlayLogic();
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orientationUtils != null){
            orientationUtils.releaseListener();
        }
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        }, 200);
    }

    /**
     * 播放
     */
    private void play(){
        String cacheFile = checkCacheFile();
        if(cacheFile != null){
            initPlayer(cacheFile);
        }else{
            initPlayer(mUrl);
            cacheFile();
        }
    }

    /**
     * 开始缓存
     */
    private void cacheFile() {
        if(!isCache){
            return;
        }
        if(checkCacheFile() != null){
            return;
        }

        DownloadUtil.getInstance().download(mUrl, Constants.PATH_CACHE, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String path) {
                Log.i("download", "download success");
            }

            @Override
            public void onDownloading(int progress) {
                Log.i("download", "download progress=" + progress);
            }

            @Override
            public void onDownloadFailed() {
                Log.i("download", "download failed");
            }
        });
    }

    /**
     * 获取本地缓存文件
     * @return
     */
    private String checkCacheFile(){
        File file = new File(Constants.PATH_CACHE  + File.separator + DownloadUtil.getNameFromUrl(mUrl));
        if(file.exists()){
            return "file://" + file.getAbsolutePath();
        }else{
            return null;
        }
    }

}
