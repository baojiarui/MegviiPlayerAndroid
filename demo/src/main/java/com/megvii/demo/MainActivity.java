package com.megvii.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.megvii.demo.util.Base64Utils;
import com.megvii.demo.util.MD5Utils;
import com.megvii.player.model.ChannelStream;
import com.megvii.player.model.VideoPara;
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

        //requestVideoUrl();
        requestNonce();
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
            String url;
            switch (msg.what){
                case 0:
                    url = msg.getData().getString("url");
                    //url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
                    goToVideoPlayer(MainActivity.this, url);
                    finish();
                    break;
                case 1:
                    String nonce = msg.getData().getString("nonce");
                    String realm = msg.getData().getString("realm");
                    url = getRtmpStream(realm, nonce);
                    goToVideoPlayer(MainActivity.this, url);
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    private String getRtmpStream(String realm, String nonce){
        String auth = getAuth(realm, nonce);
//        return "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";
        return ApiConfig.BASE_RTSP + Constant.CAMERA_ID + "?auth=" + auth;// + "&resolution=240p" + "&stream=1";
    }

    private String getHLSStream(String realm, String nonce){
        String auth = getAuth(realm, nonce);
        return ApiConfig.BASE_URL+ "hls/" + Constant.CAMERA_ID + ".m3u?auth=" + auth;// + "&resolution=240p" + "&stream=1";
    }


    /**
     * Call GET /api/getNonce
     In response you'll get a JSON object with realm and nonce
     Translate user's username to the lower case
     Check the required method ("GET" for HTTP requests, "PLAY" for RTSP)
     digest = md5_hex(user_name + ":" + realm + ":" + password)
     partial_ha2 = md5_hex(method + ":")
     simplified_ha2 = md5_hex(digest + ":" + nonce + ":" + partial_ha2)
     auth_digest = base64(user_name + ":" + nonce + ":" + simplified_ha2)
     Here auth_digest is the required authentication hash
     */

    private String getAuth(String realm, String nonce){
        String authDigest;
        String digest = MD5Utils.getMD5(Constant.USER_NAME + ":" + realm + ":" + Constant.USER_PWD);
        String partial_ha2 = MD5Utils.getMD5("PLAY:");
        String simplified_ha2 = MD5Utils.getMD5(digest + ":" + nonce + ":" + partial_ha2);
        authDigest = Base64Utils.getBase64(Constant.USER_NAME+ ":" + nonce + ":" + simplified_ha2);
        return authDigest;
    }

    private void requestNonce() {
        String url = ApiConfig.URL_GET_NONCE;
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
                    VideoPara videoPara = gson.fromJson(body, VideoPara.class);

                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("nonce", videoPara.getReply().getNonce());
                    bundle.putString("realm", videoPara.getReply().getRealm());
                    msg.setData(bundle);
                    msg.what = 1;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
