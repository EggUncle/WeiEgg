package com.app.egguncle.weiegg.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.egguncle.weiegg.R;
import com.bumptech.glide.Glide;

import java.util.zip.Inflater;

/**
 * Created by egguncle on 17-4-23.
 * 微博图片显示
 */

public class ImageGroup extends HorizontalScrollView {

    public View groupView;
    public LinearLayout lineImages;
    public Context mContext;

    private final  static String TAG="ImageGroup";

    public ImageGroup(Context context) {
        this(context,null);

    }

    public ImageGroup(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);

    }

    public ImageGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        groupView = LayoutInflater.from(mContext).inflate(R.layout.image_group_view, this);
        initView();
    }

//    public ImageGroup(Context context) {
//        super(context);
//        mContext = context;
//
//        groupView = LayoutInflater.from(mContext).inflate(R.layout.image_group_view, null);
//        initView();
//    }



    public void initView() {
        lineImages = (LinearLayout) groupView.findViewById(R.id.view_image_group);
    }

    public void addImage(String imgUrl) {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);

        lineImages.addView(imageView);

        Glide.with(mContext)
                .load(imgUrl)
                .centerCrop()

               // .thumbnail(0.1f) //加载缩略图  为原图的十分之一
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);

        Log.i(TAG, "addImage: "+imgUrl);

    }

    public void clearImage(){
        lineImages.removeAllViews();
    }

}
