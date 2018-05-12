package com.megvii.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.megvii.player.model.ChannelStream;
import com.megvii.player.play.PlayActivity;
import com.megvii.player.ApiConfig;
import com.megvii.player.BaseActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private final OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestVideoUrl();
    }

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

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String url = msg.getData().getString("url");
            //url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
            goToVideoPlayer(MainActivity.this, url);
            finish();
            return false;
        }
    });

    /**
     * 获取视频地址接口
     */
    private void requestVideoUrl() {
        String url = ApiConfig.URL_GET_CHANNEL_STREAM + "?channel=1&protocol=RTMP";
        Request request = new Request.Builder()
                .url(url)
                .build();

        showLoading();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideLoading();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                hideLoading();
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

                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
