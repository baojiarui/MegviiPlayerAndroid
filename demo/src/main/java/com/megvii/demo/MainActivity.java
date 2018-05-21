package com.megvii.demo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megvii.demo.Utils.AuthUtils;
import com.megvii.demo.Utils.DateUtils;
import com.megvii.demo.Utils.ToActivityUtils;
import com.megvii.demo.adapter.CameraListAdapter;
import com.megvii.demo.model.CameraData;
import com.megvii.player.model.NonceModel;
import com.megvii.player.ApiConfig;
import com.megvii.player.BaseActivity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * player sample
 */
public class MainActivity extends BaseActivity {

    private static final String[] REQUIRED_PERMISSION_LIST = new String[] {
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    /**unrequest permissions*/
    private List<String> missingPermission = new ArrayList<>();

    private final int MSG_WHAT_CAMERA_LIST = 0;
    private final int MSG_WHAT_NONCE = 1;

    private final OkHttpClient okHttpClient = new OkHttpClient();

    /**摄像头列表*/
    private RecyclerView mRecyclerView;
    private CameraListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<CameraData> mcameraList;

    /**认证数据*/
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
                    mGetAuth = AuthUtils.getAuth(realm, nonce, "GET");
                    mPlayAuth = AuthUtils.getAuth(realm, nonce, "PLAY");
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

        checkAndRequestPermissions();
    }

    /**
     * Checks if there is any missing permissions, and
     * requests runtime permission if needed.
     */
    private void checkAndRequestPermissions() {
        if(missingPermission == null){
            return;
        }
        // Check for permissions
        for (String eachPermission : REQUIRED_PERMISSION_LIST) {
            if (ContextCompat.checkSelfPermission(this, eachPermission) != PackageManager.PERMISSION_GRANTED) {
                missingPermission.add(eachPermission);
            }
        }
        if(missingPermission.size() > 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this,
                        missingPermission.toArray(new String[missingPermission.size()]),
                        12345);
            }
        }
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
                String cameraId = item.getId().replace("{", "").replace("}", "");

                showChoosePlay(cameraId, 0, 0);
            }
        });
    }

    /**
     * 选择播放方式
     * @param cameraId 摄像头ID
     * @param pos 点播开始时间戳 直播默认0
     * @param endPos 点播结束时间戳 直播默认0
     */
    private void showChoosePlay(final String cameraId, final long pos, final long endPos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("选择播放方式");
        final String[] items = {PlayMode.RTSP.toString(), PlayMode.HLS.toString(), PlayMode.HTTP.toString()};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = null;
                switch (which){
                    case 0:
                        url = getPlayStream(PlayMode.RTSP, cameraId, 0, 0);
                        ToActivityUtils.goToVideoPlayer(MainActivity.this, url);
                        break;
                    case 1:
                        url = getPlayStream(PlayMode.HLS, cameraId, 0, 0);
                        ToActivityUtils.goToVideoPlayer(MainActivity.this, url);
                        break;
                    case 2:
                        //String cameraId = "681068eb-8026-9153-488a-75ebf913c673";
                        String cameraId = "64a288b7-4ef2-f213-2375-8e6f1823dfff";
                        long pos1 = DateUtils.getTimestamp("2018-05-21 11:10:00");
                        long pos2 = DateUtils.getTimestamp("2018-05-21 11:10:10");
                        url = getPlayStream(PlayMode.HTTP, cameraId, pos1, pos2);
                        ToActivityUtils.goToVideoPlayer(MainActivity.this, url, true);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * 获取播放地址
     * @param mode 播放方式，参考PlayMode
     * @param cameraId
     * @param pos If present and not equal to <now>, specifies archive stream start position (as a string containing time in milliseconds since epoch, or a local time formatted like "YYYY-MM-DDTHH:mm:ss.zzz" - the format is auto-detected). Otherwise, LIVE stream is provided
     * @param endPos If present, specifies archive stream end position (as a string containing time in milliseconds since epoch, or a local time formatted like "YYYY-MM-DDTHH:mm:ss.zzz" - the format is auto-detected). It is used only if "pos" parameter is present.
     * @return
     */
    private String getPlayStream(PlayMode mode, String cameraId, long pos, long endPos){
        String url = null;
        switch (mode){
            case RTSP:
                url = ApiConfig.BASE_RTSP + cameraId + "?auth=" + mPlayAuth;
                break;
            case HLS:
                url = ApiConfig.BASE_URL+ "hls/" + cameraId + ".m3u8?" + "auth=" + mGetAuth;
                break;
            case HTTP:
                url = ApiConfig.BASE_URL+ "media/" + cameraId + ".webm?";
                if(pos > 0){
                    url = url + "pos=" + pos;
                }
                if(endPos > 0){
                    url = url + "&endPos=" + endPos;
                }
                url = url + "&auth=" + mGetAuth;
                break;
            default:
                break;
        }
        if(url == null){
            return null;
        }
        return url;
    }

    /**请求auth需要的Nonce信息*/
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

    /**请求摄像头列表数据*/
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
