package com.megvii.player.play;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.megvii.player.ApiConfig;
import com.megvii.player.R;
import com.megvii.player.model.ChannelStream;
import com.megvii.player.model.SwitchVideoModel;
import com.megvii.player.video.SampleVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 单独的视频播放页面
 */
public class PlayActivity extends AppCompatActivity {

    public final static String INTENT_KEY_URL = "URL";

    private SampleVideo videoPlayer;
    private OrientationUtils orientationUtils;

    private final OkHttpClient okHttpClient = new OkHttpClient();
    /**心跳控制*/
    private Handler heartHandler = new Handler();
    /**心跳时间*/
    private long heartbeatTime = 1000 * 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);
        initView();

        String url = getIntent().getStringExtra(INTENT_KEY_URL);
        initPlayer(url);
        heartbeat();
    }

    private void initView(){
        videoPlayer = findViewById(R.id.video_player);
    }

    private void initPlayer(String url) {
//        String source1 = "https://res.exexm.com/cw_145225549855002";
//        String name = "普通";
//        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);

//        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
//        String name = "普通";
//        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);

        String name = "普通";
        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, url);

        List<SwitchVideoModel> list = new ArrayList<>();
        list.add(switchVideoModel);

        videoPlayer.setUp(list, true, "");

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
        heartHandler = null;
        if (orientationUtils != null)
            orientationUtils.releaseListener();
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
     * 延迟发送心跳请求
     */
    private void heartbeat(){
        if(heartHandler == null){
            return;
        }
        heartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestHeartbeat();
            }
        }, heartbeatTime);
    }

    /**
     * 视频播放心跳接口
     */
    private void requestHeartbeat() {
        if(heartHandler == null){
            return;
        }
        String url = ApiConfig.URL_GET_HEARTBEAT + "?channel=1&line=local&protocol=rtmp";
        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                heartbeat();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                heartbeat();
                if (!response.isSuccessful()){
                    return;
                }
                try {
                    String body = response.body().string();
                    Gson gson = new Gson();
                    ChannelStream channelStream = gson.fromJson(body, ChannelStream.class);
                    String url = channelStream.getEasyDarwin().getBody().getURL();
                    if(url.contains("{host}")){
                        url = url.replace("{host}", "110.16.71.162");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
