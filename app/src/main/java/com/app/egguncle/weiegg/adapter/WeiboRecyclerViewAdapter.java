package com.app.egguncle.weiegg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.activity.FriendActivity;
import com.app.egguncle.weiegg.entities.weibo.Statuses;
import com.app.egguncle.weiegg.utils.LogUtils;
import com.app.egguncle.weiegg.views.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by egguncle on 16.10.16.
 */
public class WeiboRecyclerViewAdapter extends RecyclerView.Adapter<WeiboRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Statuses> mListStatuses;
    private RequestManager glideRequest;
    private LinearLayoutManager mLinearLayoutManager;
    private List<String> contentStringList;// 用来将微博内容分成一小段一小段，再将用户昵称部分高亮，再拼接成原来的微博内容

 //   private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public WeiboRecyclerViewAdapter(Context context, List<Statuses> statusesList) {
        mContext = context;
        mListStatuses = statusesList;
        glideRequest = Glide.with(context);
        contentStringList = new ArrayList<>();

    }

//    @Override
//    public void onClick(View view) {
//        if (mOnItemClickListener != null) {
//            mOnItemClickListener.onItemClick(view, (Statuses) view.getTag(1));
//        }
//    }
//
//    public static interface OnRecyclerViewItemClickListener {
//        void onItemClick(View view, Statuses statuses);
//    }
//
//    public void setOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
//        this.mOnItemClickListener = mOnItemClickListener;
//    }

    @Override
    public WeiboRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_weibo, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
//        view.setOnClickListener(this);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(WeiboRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.lineRetweet.setVisibility(View.GONE);
        holder.tvName.setText(mListStatuses.get(position).getUser().getScreen_name());
        holder.tvWeiboContent.setText(mListStatuses.get(position).getText());
        holder.tvLikeCount.setText(mListStatuses.get(position).getAttitudes_count() + "");
        holder.tvRetweetCount.setText(mListStatuses.get(position).getReposts_count() + "");
        holder.tvCommentCount.setText(mListStatuses.get(position).getComments_count() + "");

        holder.lineUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,FriendActivity.class);
                intent.putExtra("type", "normal");
                intent.putExtra("data",mListStatuses.get(position));
                mContext.startActivity(intent);
            }
        });


        glideRequest.load(mListStatuses.get(position).getUser().getProfile_image_url()).transform(new GlideCircleTransform(mContext)).into(holder.ivUserIcon);
        // 使用正则表达式获取到来源字符串中的来源
        String line = mListStatuses.get(position).getSource();
        String pattern = ">.*<";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            String source = m.group().substring(1, m.group(0).length() - 1);
            holder.tvWeiboFrom.setText(source);
          //  LogUtils.e("来源为:" + source);
        } else {
         //   LogUtils.e("解析失败");
        }

        String line2 = mListStatuses.get(position).getText();
        String pattern2 = "@[\\u4e00-\\u9fa5a-zA-Z0-9_-]{4,30}";
        Pattern r2 = Pattern.compile(pattern2);
        Matcher m2 = r2.matcher(line2);
    //    if (m2.find()) {
            while (m2.find()){
            LogUtils.e("正在解析的微博内容为："+line2);
            LogUtils.e("名字是： " + m2.group().substring(0, m2.group().length()));}

//        } else {
//               LogUtils.e("解析失败");
//        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM d HH:mm:ss Z yyyy", Locale.ENGLISH);
        try {
         //   LogUtils.e(mListStatuses.get(position).getCreated_at());
            Date date = dateFormat.parse(mListStatuses.get(position).getCreated_at());
            SimpleDateFormat needFormat = new SimpleDateFormat("HH:mm");
            String s = needFormat.format(date);
            holder.tvWeiboTime.setText(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mListStatuses.get(position).getRetweeted_status() != null) {
            holder.tvRetweetName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, FriendActivity.class);
                    intent.putExtra("type", "retweet");
                    intent.putExtra("data", mListStatuses.get(position).getRetweeted_status());
                    mContext.startActivity(intent);
                }
            });
        }

        List<String> contentStringList = new ArrayList<>();




        //如果有转发，显示被转发的微博的部分信息
        if (mListStatuses.get(position).getRetweeted_status() != null) {
            holder.lineRetweet.setVisibility(View.VISIBLE);
            holder.tvRetweetName.setText(mListStatuses.get(position).getRetweeted_status().getUser().getScreen_name());
            holder.tvRetweetContent.setText(mListStatuses.get(position).getRetweeted_status().getText());
            if(mListStatuses.get(position).getRetweeted_status().getPic_urls()!=null){

                mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                holder.rcvImages.setLayoutManager(mLinearLayoutManager);
                holder.rcvImages.setHasFixedSize(true);
//                for (int i = 0; i < mListStatuses.get(position).getRetweeted_status().getPic_urls().size(); i++) {
//                    LogUtils.e("被转发的微博中的图片为：" + mListStatuses.get(position).getRetweeted_status().getPic_urls().get(i).getThumbnail_pic());
//                }
                WeiboImagesRecyclerAdapter imageAdapter = new WeiboImagesRecyclerAdapter(mContext, mListStatuses.get(position).getRetweeted_status().getPic_urls());
                holder.rcvImages.setAdapter(imageAdapter);
                holder.rcvImages.setVisibility(View.VISIBLE);
            }
        }
        if (mListStatuses.get(position).getPic_urls() != null) {
            //用于item中图片的显示
            mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            holder.rcvImages.setLayoutManager(mLinearLayoutManager);
            holder.rcvImages.setHasFixedSize(true);
            holder.rcvImages.setAdapter(new WeiboImagesRecyclerAdapter(mContext, mListStatuses.get(position).getPic_urls()));
            holder.rcvImages.setVisibility(View.VISIBLE);
        }
    }

    private List<String> lightContent(String content) {
        String strTemp = content;

        //  while(strTemp.indexOf("@"))
        content.indexOf("@");
        content.indexOf("@", content.indexOf("@"));


        return contentStringList;
    }

    @Override
    public int getItemCount() {
        return mListStatuses == null ? 0 : mListStatuses.size();
    }


    /**
     * 使用正则表达式匹配微博内容中的用户名字
     */
    private String highLightName(String content, int position) {
        String line = mListStatuses.get(position).getSource();
        String pattern = "@.*/";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            //    String source = m.group().substring(0, m.group(0).length() - 1);
            return m.group();
            //  LogUtils.e("来源为:" + source);
        } else {
            return null;
            //   LogUtils.e("解析失败");
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout lineUser;
        private ImageView ivUserIcon;
        private TextView tvName;
        private TextView tvWeiboContent;
        private TextView tvWeiboTime;
        private TextView tvWeiboFrom;
        private TextView tvLikeCount;
        private TextView tvRetweetCount;
        private TextView tvCommentCount;
        private LinearLayout lineRetweet;
        private TextView tvRetweetName;
        private TextView tvRetweetContent;
      //  private LinearLayout lineImages;
        private RecyclerView rcvImages;

        public ViewHolder(View itemView) {
            super(itemView);
            lineUser = (LinearLayout) itemView.findViewById(R.id.line_user);
            ivUserIcon = (ImageView) itemView.findViewById(R.id.iv_user_icon);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvWeiboContent = (TextView) itemView.findViewById(R.id.tv_weibo_content);
            tvWeiboTime = (TextView) itemView.findViewById(R.id.tv_weibo_time);
            tvWeiboFrom = (TextView) itemView.findViewById(R.id.tv_weibo_from);
            tvLikeCount = (TextView) itemView.findViewById(R.id.tv_like_count);
            tvRetweetCount = (TextView) itemView.findViewById(R.id.tv_retweet_count);
            tvCommentCount = (TextView) itemView.findViewById(R.id.tv_comment_count);
            lineRetweet = (LinearLayout) itemView.findViewById(R.id.line_retweet);
            tvRetweetName = (TextView) itemView.findViewById(R.id.tv_retweet_name);
            tvRetweetContent = (TextView) itemView.findViewById(R.id.tv_retweet_content);
   //         lineImages = (LinearLayout) itemView.findViewById(R.id.line_images);
            rcvImages = (RecyclerView) itemView.findViewById(R.id.rcv_images);

        }
    }


}
