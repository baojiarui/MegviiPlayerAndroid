package com.megvii.demo.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.megvii.demo.R;
import com.megvii.demo.model.CameraData;

public class CameraListAdapter extends BaseQuickAdapter<CameraData, BaseViewHolder> {

    public CameraListAdapter(Context context) {
        super(R.layout.layout_camera_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CameraData item) {
        try {
            helper.setText(R.id.textview, item.getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
