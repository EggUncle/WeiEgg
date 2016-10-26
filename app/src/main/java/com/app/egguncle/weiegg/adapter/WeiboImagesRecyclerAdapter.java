package com.app.egguncle.weiegg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.entities.weibo.PicUrls;
import com.app.egguncle.weiegg.utils.LogUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by egguncle on 16.10.26.
 * 用于图片
 */
public class WeiboImagesRecyclerAdapter extends RecyclerView.Adapter<WeiboImagesRecyclerAdapter.ViewHolder> {

    private List<PicUrls> mImageList;
    private Context mContext;

    public WeiboImagesRecyclerAdapter(Context context, List<PicUrls> imageList) {
        mContext = context;
        mImageList = imageList;
        LogUtils.e("------------1------------");
        for (int i=0;i<imageList.size();i++){
            LogUtils.e(imageList.get(i).getThumbnail_pic());
        }
    }

    @Override
    public WeiboImagesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(WeiboImagesRecyclerAdapter.ViewHolder holder, int position) {
        LogUtils.e("------------2------------");
        Glide.with(mContext)
                .load(mImageList.get(position).getThumbnail_pic())
                .override(600, 300)
                .fitCenter()
                .thumbnail(0.1f) //加载缩略图  为原图的十分之一
                .into(holder.itemImage);
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
