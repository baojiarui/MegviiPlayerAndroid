package com.megvii.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megvii.demo.adapter.CameraListAdapter;
import com.megvii.demo.model.CameraData;
import com.megvii.demo.util.Base64Utils;
import com.megvii.demo.util.MD5Utils;
import com.megvii.player.model.NonceModel;
import com.megvii.player.play.PlayActivity;
import com.megvii.player.ApiConfig;
import com.megvii.player.BaseActivity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private final int MSG_WHAT_CAMERA_LIST = 0;
    private final int MSG_WHAT_NONCE = 1;

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private RecyclerView mRecyclerView;
    private CameraListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<CameraData> mcameraList;

    private NonceModel mNonceModel;
    private String mPlayAuth;
    private String mGetAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListView();

        requestNonce();
    }

    /**
     * 摄像头列表
     */
    private void initListView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CameraListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(mNonceModel == null){
                    return;
                }
                CameraData item = (CameraData) adapter.getItem(position);
                String url = getHLSStream(item.getId());
                goToVideoPlayer(MainActivity.this, url);
            }
        });
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
                case MSG_WHAT_CAMERA_LIST:
                    mAdapter.setNewData(mcameraList);
                    break;
                case MSG_WHAT_NONCE:
                    String nonce = mNonceModel.getReply().getNonce();
                    String realm = mNonceModel.getReply().getRealm();
                    mGetAuth = getAuth(realm, nonce, "GET");
                    mPlayAuth = getAuth(realm, nonce, "PLAY");
                    requestCameraList();
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    private String getRtmpStream(String cameraId){
        return ApiConfig.BASE_RTSP + cameraId + "?auth=" + mPlayAuth;
    }

    private String getHLSStream(String cameraId){
        return ApiConfig.BASE_URL+ "hls/" + cameraId + ".m3u8?" + "auth=" + mGetAuth;
    }

    private String getHttpStream(String cameraId){
        return ApiConfig.BASE_URL+ "media/" + cameraId + ".webm?auth=" + mGetAuth;
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

    private String getAuth(String realm, String nonce, String method){
        String authDigest;
        String digest = MD5Utils.getMD5(Constant.USER_NAME + ":" + realm + ":" + Constant.USER_PWD);
        String partial_ha2 = MD5Utils.getMD5(method + ":");
        String simplified_ha2 = MD5Utils.getMD5(digest + ":" + nonce + ":" + partial_ha2);
        authDigest = Base64Utils.getBase64(Constant.USER_NAME+ ":" + nonce + ":" + simplified_ha2);
        return authDigest.trim();
    }

    /**
     * 请求auth需要的信息
     */
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
                    mNonceModel = gson.fromJson(body, NonceModel.class);

                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    msg.setData(bundle);
                    msg.what = MSG_WHAT_NONCE;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 请求摄像头列表
     */
    private void requestCameraList() {
        String url = ApiConfig.URL_GET_CAMERAS + "?login=" + Constant.USER_NAME + "&password=" + Constant.USER_PWD + "&auth=" + mGetAuth;

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
                    Type listType = new TypeToken<List<CameraData>>(){}.getType();
                    mcameraList = gson.fromJson(body, listType);

                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    msg.setData(bundle);
                    msg.what = MSG_WHAT_CAMERA_LIST;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
