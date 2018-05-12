package com.megvii.player.video;

import android.content.Context;
import android.util.AttributeSet;

import com.megvii.player.view.CustomRenderView;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;

/**
 * 自定义渲染控件
 */

public class CustomRenderVideoPlayer extends NormalGSYVideoPlayer {
    public CustomRenderVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public CustomRenderVideoPlayer(Context context) {
        super(context);
    }

    public CustomRenderVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void addTextureView() {
        mTextureView = new CustomRenderView();
        mTextureView.addView(getContext(), mTextureViewContainer, mRotate, this, this, mEffectFilter, mMatrixGL, mRenderer, mMode);
    }
}
