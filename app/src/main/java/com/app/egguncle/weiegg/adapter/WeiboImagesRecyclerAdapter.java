package com.app.egguncle.weiegg.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.entities.weibo.PicUrls;
import com.app.egguncle.weiegg.utils.LogUtils;
import com.app.egguncle.weiegg.views.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by egguncle on 16.10.26.
 * 用于图片
 */
public class WeiboImagesRecyclerAdapter extends RecyclerView.Adapter<WeiboImagesRecyclerAdapter.ViewHolder> {

    private List<PicUrls> mImageList;
    private Context mContext;

    private final static String TAG = "WeiboImagesAdapter";

    public WeiboImagesRecyclerAdapter(Context context, List<PicUrls> imageList) {
        mContext = context;
        mImageList = imageList;
    }

    @Override
    public WeiboImagesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(final WeiboImagesRecyclerAdapter.ViewHolder holder, final int position) {

//        Glide.with(mContext)     .load(mImageList.get(position).getThumbnail_pic())
//                .override(600, 300)
//                .fitCenter()
//                .thumbnail(0.1f) //加载缩略图  为原图的十分之一
//                .into(holder.itemImage);

        String tag = (String) holder.itemImage.getTag();
        if (tag!=null&&!TextUtils.equals(tag, mImageList.get(position).getThumbnail_pic())) {
            Glide.with(mContext).load(R.mipmap.ic_launcher)
                    .asBitmap()
                    .override(600, 300)
                    .fitCenter()
                    .into(holder.itemImage);
        } else {
            Glide.with(mContext).load(mImageList.get(position).getThumbnail_pic())
                    .asBitmap()
                    .override(600, 300)
                    .fitCenter()
                    .thumbnail(0.1f) //加载缩略图  为原图的十分之一
                    .error(R.mipmap.ic_launcher)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                                glideAnimation) {
                            Log.i(TAG, "onResourceReady: " + mImageList.get(position).getThumbnail_pic());
                            //设置tag
                            holder.itemImage.setTag(mImageList.get(position).getThumbnail_pic());
                            //加载图片
                            holder.itemImage.setImageBitmap(resource);
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return mImageList == null ? 0 : mImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);

        }
    }
}
