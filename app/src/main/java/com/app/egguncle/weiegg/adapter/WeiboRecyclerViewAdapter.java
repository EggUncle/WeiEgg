package com.app.egguncle.weiegg.adapter;

import android.content.Context;
import android.content.Intent;
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
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by egguncle on 16.10.16.
 */
public class WeiboRecyclerViewAdapter extends RecyclerView.Adapter<WeiboRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Statuses> mListStatuses;
    private RequestManager glideRequest;
 //   private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public WeiboRecyclerViewAdapter(Context context, List<Statuses> statusesList) {
        mContext = context;
        mListStatuses = statusesList;
        glideRequest = Glide.with(context);
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
        //   holder.tvWeiboTime.setText(mListStatuses.get(position).getCreated_at());
        //   holder.tvWeiboFrom.setText(mListStatuses.get(position).getSource());
        holder.tvLikeCount.setText(mListStatuses.get(position).getAttitudes_count() + "");
        holder.tvRetweetCount.setText(mListStatuses.get(position).getReposts_count() + "");
        holder.tvCommentCount.setText(mListStatuses.get(position).getComments_count() + "");

        holder.lineUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,FriendActivity.class);
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
            LogUtils.e("来源为:" + source);
        } else {
            LogUtils.e("解析失败");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        try {
            Date date = dateFormat.parse(mListStatuses.get(position).getCreated_at());
            SimpleDateFormat needFormat = new SimpleDateFormat("HH:mm");
            String s = needFormat.format(date);
            holder.tvWeiboTime.setText(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtils.e("头像的原图地址链接为： " + mListStatuses.get(position).getUser().getAvatar_large());
        //      Glide.with(mContext).load(mListStatuses.get(position).getUser().getProfile_image_url()).into(holder.ivUserIcon);
        //如果有转发，显示被转发的微博的部分信息
        if (mListStatuses.get(position).getRetweeted_status() != null) {
            holder.lineRetweet.setVisibility(View.VISIBLE);
            holder.tvRetweetName.setText(mListStatuses.get(position).getRetweeted_status().getUser().getScreen_name());
            holder.tvRetweetContent.setText(mListStatuses.get(position).getRetweeted_status().getText());
        }
    }

    @Override
    public int getItemCount() {
        return mListStatuses == null ? 0 : mListStatuses.size();
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

        public ViewHolder(View itemView) {
            super(itemView);
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
            lineUser = (LinearLayout) itemView.findViewById(R.id.line_user);

        }
    }


}
