package com.app.egguncle.weiegg.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.egguncle.weiegg.CWConstant;
import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.adapter.WeiboRecyclerViewAdapter;
import com.app.egguncle.weiegg.entities.weibo.Statuses;
import com.app.egguncle.weiegg.utils.SPUtils;
import com.app.egguncle.weiegg.utils.WeiBoUtils;
import com.app.egguncle.weiegg.views.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.util.List;

public class FriendActivity extends AppCompatActivity {

    private AppBarLayout appBar;
    private CollapsingToolbarLayout toolbarLayout;
    private ImageView ivUserBackground;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private Statuses mStatuse;
    private RecyclerView rcvUser;
    private WeiboRecyclerViewAdapter mWeiboRecyclerViewAdapter;
    private List<Statuses> mListFriendStatuses;
    private SwipeRefreshLayout srhFriend;

    private ImageView ivFriendIcon;
    private TextView tvAttentionCount;
    private TextView tvFansCount;
    private TextView tvWeiboCount;
    private TextView tvRelationship;
    private ImageView ivRelationship;
    private TextView tvDescription;
    private RecyclerView rcvFriend;



    private RequestManager glideRequest;

    private WeiboParameters mParameters;
    private Oauth2AccessToken mOauth2AccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        initVar();
        initView();
    }

    private void initView() {
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setTitle(mStatuse.getUser().getScreen_name());

        ivUserBackground = (ImageView) findViewById(R.id.iv_user_background);
//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
       Glide.with(this).load(mStatuse.getUser().getCover_image_phone()).into(ivUserBackground);
    //    Glide.with(this).load("http://tva1.sinaimg.cn/crop.0.0.750.750.180/70302dd9jw8f909d5fp5dj20ku0kuq4n.jpg").into(ivUserBackground);

        tvAttentionCount = (TextView) findViewById(R.id.tv_attention_count);
        tvAttentionCount.setText(mStatuse.getUser().getFriends_count()+"");

        tvFansCount = (TextView) findViewById(R.id.tv_fans_count);
        tvFansCount.setText(mStatuse.getUser().getFollowers_count()+"");

        tvWeiboCount = (TextView) findViewById(R.id.tv_weibo_count);
        tvWeiboCount.setText(mStatuse.getUser().getStatuses_count()+"");

        tvRelationship = (TextView) findViewById(R.id.tv_relationship);
        ivRelationship = (ImageView) findViewById(R.id.iv_relationship);


        WeiBoUtils.getFollowMe(this,mParameters,mOauth2AccessToken.getToken(),mStatuse.getUser().getScreen_name(),tvRelationship,ivRelationship);

        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvDescription.setText(mStatuse.getUser().getDescription());

        ivFriendIcon = (ImageView) findViewById(R.id.iv_friend_icon);
        glideRequest.load(mStatuse.getUser().getAvatar_large()).transform(new GlideCircleTransform(this)).into(ivFriendIcon);

        rcvFriend= (RecyclerView) findViewById(R.id.rcv_friend);
        rcvFriend.setLayoutManager(new LinearLayoutManager(this));
        rcvFriend.setAdapter(mWeiboRecyclerViewAdapter);

        srhFriend= (SwipeRefreshLayout) findViewById(R.id.srh_friend);
        srhFriend.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srhFriend.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WeiBoUtils.getFriendWeiBo(FriendActivity.this,mParameters,mOauth2AccessToken.getToken(),WeiBoUtils.GET_NEW_WEIBO);
                        mWeiboRecyclerViewAdapter.notifyDataSetChanged();
                        srhFriend.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        srhFriend.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));


    }

    private void initVar() {
        mStatuse = (Statuses) getIntent().getSerializableExtra("data");
        mParameters = new WeiboParameters(CWConstant.APP_KEY);
        mOauth2AccessToken = SPUtils.getInstance(getApplicationContext()).getToken();
        glideRequest = Glide.with(this);

        WeiBoUtils.getFriendWeiBo(this,mParameters,mOauth2AccessToken.getToken(),WeiBoUtils.GET_NEW_WEIBO);
        mWeiboRecyclerViewAdapter=new WeiboRecyclerViewAdapter(this,WeiBoUtils.getmListFriendStatuses());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
