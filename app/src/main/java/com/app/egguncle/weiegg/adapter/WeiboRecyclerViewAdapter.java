package com.app.egguncle.weiegg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
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
import com.app.egguncle.weiegg.utils.MyClickableSpan;
import com.app.egguncle.weiegg.views.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

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

    private final static String TAG = "WeiboRecyclerAdapter";

    private Context mContext;
    private List<Statuses> mListStatuses;
    private RequestManager glideRequest;
    private LinearLayoutManager mLinearLayoutManager;
    private List<String> contentStringList;// 用来将微博内容分成一小段一小段，再将用户昵称部分高亮，再拼接成原来的微博内容

    private  static boolean loadImg=true;

    public void loadImg(){
        loadImg=true;
    }
    public void stopLoadImg(){
        loadImg=false;
    }



    //   private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public WeiboRecyclerViewAdapter(Context context, List<Statuses> statusesList) {
        mContext = context;
        mListStatuses = statusesList;
        glideRequest = Glide.with(context);
        contentStringList = new ArrayList<>();

    }

    @Override
    public WeiboRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_weibo, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
//        view.setOnClickListener(this);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final WeiboRecyclerViewAdapter.ViewHolder holder, final int position) {
        String weiboId = mListStatuses.get(position).getIdstr();


        holder.lineRetweet.setVisibility(View.GONE);
        holder.tvName.setText(mListStatuses.get(position).getUser().getScreen_name());
        //   holder.tvWeiboContent.setText(mListStatuses.get(position).getText());
        holder.tvLikeCount.setText(mListStatuses.get(position).getAttitudes_count() + "");
        holder.tvRetweetCount.setText(mListStatuses.get(position).getReposts_count() + "");
        holder.tvCommentCount.setText(mListStatuses.get(position).getComments_count() + "");

        holder.lineUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FriendActivity.class);
                intent.putExtra("type", "normal");
                intent.putExtra("data", mListStatuses.get(position));
                mContext.startActivity(intent);
            }
        });

        final String iconUrl = mListStatuses.get(position).getUser().getProfile_image_url();
        final String iconTag = (String) holder.ivUserIcon.getTag();
        //加载图片并给图片设置tag
        //若url和tag不同，则不加载图片
        if (TextUtils.equals(iconTag, "") || !TextUtils.equals(iconUrl, iconTag)) {
            //加载默认图片

        }
        //设置用户头像
        glideRequest.load(iconUrl)
                .asBitmap()
                .centerCrop()
                .error(R.mipmap.ic_launcher).transform(new GlideCircleTransform(mContext))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        Log.i(TAG, "onResourceReady: " + iconUrl);
                        //设置tag
                        holder.ivUserIcon.setTag(iconUrl);
                        //加载图片
                        holder.ivUserIcon.setImageBitmap(resource);
                    }
                });


        //   glideRequest.load(mListStatuses.get(position).getUser().getProfile_image_url()).transform(new GlideCircleTransform(mContext)).into(holder.ivUserIcon);
        // 使用正则表达式获取到来源字符串中的来源
        holder.tvWeiboFrom.setText(getSource(mListStatuses.get(position).getSource()));

        //      if (mListStatuses.get(position).getText().indexOf("@") != -1) {
        //在微博中解析出用户昵称
        //使用正则表达式匹配出名字，再在字符串中查找名字的位置，并对微博内容进行分段，再将名字部分转换为可点击的，再将微博内容拼接起来
        String line2 = mListStatuses.get(position).getText();
        //匹配规则
        String pattern2 = "@[\\u4e00-\\u9fa5a-zA-Z0-9_-]{4,30}";
        highLightUser(line2, pattern2, holder.tvWeiboContent);

//        }else{
        holder.tvWeiboContent.setText(mListStatuses.get(position).getText());
        //     }


        holder.tvWeiboTime.setText(getTime(mListStatuses.get(position).getCreated_at()));


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

        //如果有转发，显示被转发的微博的部分信息
        if (mListStatuses.get(position).getRetweeted_status() != null) {

            holder.lineRetweet.setVisibility(View.VISIBLE);
            holder.tvRetweetName.setText(mListStatuses.get(position).getRetweeted_status().getUser().getScreen_name());
            holder.tvRetweetContent.setText(mListStatuses.get(position).getRetweeted_status().getText());

//            //判断tag是否匹配
//            String tag = (String) holder.lineRetweet.getTag();
//            Log.i(TAG, "onBindViewHolder: tag is "+tag);
//            boolean tagIsSame = TextUtils.equals(weiboId, tag) || TextUtils.equals(tag, null);
            //如果转发中带有图片
            if (mListStatuses.get(position).getRetweeted_status().getPic_urls() != null&&loadImg) {

                mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                holder.rcvImages.setLayoutManager(mLinearLayoutManager);
                holder.rcvImages.setHasFixedSize(true);
                WeiboImagesRecyclerAdapter imageAdapter = new WeiboImagesRecyclerAdapter(mContext, mListStatuses.get(position).getRetweeted_status().getPic_urls());
                holder.rcvImages.setAdapter(imageAdapter);
                holder.rcvImages.setVisibility(View.VISIBLE);

                //给出现图片的item的图片显示部分设置tag
                holder.lineRetweet.setTag(weiboId);
            }
        }
        if (mListStatuses.get(position).getPic_urls().size() != 0&&loadImg) {
            //判断tag是否匹配
//            String tag = (String) holder.rcvImages.getTag();
//            Log.i(TAG, "onBindViewHolder: tag is "+tag);
        //    boolean tagIsSame = TextUtils.equals(weiboId, tag) || TextUtils.equals(tag, null);

                //用于item中图片的显示
                mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                holder.rcvImages.setLayoutManager(mLinearLayoutManager);
                holder.rcvImages.setHasFixedSize(true);
                holder.rcvImages.setAdapter(new WeiboImagesRecyclerAdapter(mContext, mListStatuses.get(position).getPic_urls()));
                holder.rcvImages.setVisibility(View.VISIBLE);

                //给出现图片的item的图片显示部分设置tag
                holder.rcvImages.setTag(weiboId);

        }
    }


    @Override
    public int getItemCount() {
        return mListStatuses == null ? 0 : mListStatuses.size();
    }


    /**
     * 高亮字符串中符合正则表达式的部分，此处用来高亮微博中的用户名
     *
     * @param content     微博内容
     * @param pattern     正则表达式
     * @param textContent 用来显示的textview
     */
    private void highLightUser(String content, String pattern, TextView textContent) {
        if (textContent.length() != 0) {
            return;
        }
        textContent.setText("");
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);
        StringBuilder contentBuilder = new StringBuilder();
        String contentTemp = content;
        int tempContentStart = 0; //微博内容的起始位置
        int tempContentEnd = 0;//微博内容的重点位置
        while (m.find()) {
            String userName = m.group().substring(0, m.group().length());
            LogUtils.e("正在解析的微博内容为：" + content);
            LogUtils.e("名字是： " + userName);
            //获取用户的名字在字符串中的起始位置
            tempContentEnd = contentTemp.indexOf(userName);
            //将用户名部分转化成可点击的
            SpannableString spStr = new SpannableString(userName);
            ClickableSpan clickableSpan = new MyClickableSpan(userName);
            spStr.setSpan(clickableSpan, 0, spStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //    line2.replace(m2.group().substring(0, m2.group().length()),spStr);
            //拼接字符串，从开始部分到用户名之前的位置
            contentBuilder.append(contentTemp.substring(tempContentStart, tempContentEnd));
            contentTemp = contentTemp.substring(tempContentEnd + spStr.length(), contentTemp.length());
            textContent.append(contentBuilder);
            //将可点击的用户名也拼接进去
            //   content.append(spStr);
            textContent.append(spStr);
            textContent.setMovementMethod(LinkMovementMethod.getInstance());
            //获取下一次开始的位置
            // tempContentStart=content.length();
        }
    }

    /**
     * 获取微博来源
     *
     * @param strSource
     * @return
     */
    private String getSource(String strSource) {
        String pattern = ">.*<";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(strSource);
        if (m.find()) {
            String source = m.group().substring(1, m.group(0).length() - 1);
            return source;
            //  LogUtils.e("来源为:" + source);
        } else {
            //   LogUtils.e("解析失败");
            return "未知";
        }
    }

    /**
     * 返回正常显示的微博时间
     *
     * @param created_at
     * @return
     */
    private String getTime(String created_at) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM d HH:mm:ss Z yyyy", Locale.ENGLISH);
        try {
            //   LogUtils.e(mListStatuses.get(position).getCreated_at());
            Date date = dateFormat.parse(created_at);
            SimpleDateFormat needFormat = new SimpleDateFormat("HH:mm");
            String s = needFormat.format(date);
            return s;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
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
