package com.megvii.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megvii.demo.Utils.AuthUtil;
import com.megvii.demo.Utils.ToActivityUtils;
import com.megvii.demo.adapter.CameraListAdapter;
import com.megvii.demo.model.CameraData;
import com.megvii.player.model.NonceModel;
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

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_WHAT_CAMERA_LIST:
                    mAdapter.setNewData(mcameraList);
                    break;
                case MSG_WHAT_NONCE:
                    String nonce = mNonceModel.getReply().getNonce();
                    String realm = mNonceModel.getReply().getRealm();
                    mGetAuth = AuthUtil.getAuth(realm, nonce, "GET");
                    mPlayAuth = AuthUtil.getAuth(realm, nonce, "PLAY");
                    requestCameraList();
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListView();
        requestNonce();
    }

    /**初始化摄像头列表UI*/
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
                    requestNonce();
                    return;
                }
                CameraData item = (CameraData) adapter.getItem(position);
                showChoosePlay(item.getId());
            }
        });
    }

    private void showChoosePlay(final String cameraId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("选择播放方式");
        final String[] cities = {"RTSP", "HLS", "HTTP"};
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = null;
                switch (which){
                    case 0:
                        url = getRtspStream(cameraId);
                        break;
                    case 1:
                        url = getHLSStream(cameraId);
                        break;
                    case 2:
                        url = getHttpStream(cameraId);
                        break;
                    default:
                        break;
                }
                if(url != null){
                    ToActivityUtils.goToVideoPlayer(MainActivity.this, url);
                }
            }
        });
        builder.show();
    }

    /**获取RTMP播放地址*/
    private String getRtspStream(String cameraId){
        return ApiConfig.BASE_RTSP + cameraId + "?auth=" + mPlayAuth;
    }

    /**获取HLS播放地址*/
    private String getHLSStream(String cameraId){
        return ApiConfig.BASE_URL+ "hls/" + cameraId + ".m3u8?" + "auth=" + mGetAuth;
    }

    /**获取HTTP播放地址*/
    private String getHttpStream(String cameraId){
        return ApiConfig.BASE_URL+ "media/" + cameraId + ".webm?auth=" + mGetAuth;
    }

    /**请求auth需要的信息*/
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

    /**请求摄像头列表*/
    private void requestCameraList() {
        String url = ApiConfig.URL_GET_CAMERAS + "?login=" + ApiConfig.USER_NAME + "&password=" + ApiConfig.USER_PWD + "&auth=" + mGetAuth;
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
