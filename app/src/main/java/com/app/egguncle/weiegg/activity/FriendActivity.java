package com.app.egguncle.weiegg.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.egguncle.weiegg.CWConstant;
import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.adapter.WeiboRecyclerViewAdapter;
import com.app.egguncle.weiegg.entities.weibo.RetweetedStatus;
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
    private RetweetedStatus mRetStatus;

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
        tvAttentionCount = (TextView) findViewById(R.id.tv_attention_count);
        tvFansCount = (TextView) findViewById(R.id.tv_fans_count);
        tvWeiboCount = (TextView) findViewById(R.id.tv_weibo_count);
        tvRelationship = (TextView) findViewById(R.id.tv_relationship);
        ivRelationship = (ImageView) findViewById(R.id.iv_relationship);
        ivUserBackground = (ImageView) findViewById(R.id.iv_user_background);
        ivFriendIcon = (ImageView) findViewById(R.id.iv_friend_icon);
        rcvFriend = (RecyclerView) findViewById(R.id.rcv_friend);
        tvDescription = (TextView) findViewById(R.id.tv_description);
//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        if (mStatuse != null) {
            setTitle(mStatuse.getUser().getScreen_name());
            Glide.with(this).load(mStatuse.getUser().getCover_image_phone()).into(ivUserBackground);
            WeiBoUtils.getFollowMe(this, mParameters, mOauth2AccessToken.getToken(), mStatuse.getUser().getScreen_name(), tvRelationship, ivRelationship);
            tvDescription.setText(mStatuse.getUser().getDescription());
            tvAttentionCount.setText(mStatuse.getUser().getFriends_count() + "");
            tvFansCount.setText(mStatuse.getUser().getFollowers_count() + "");
            tvWeiboCount.setText(mStatuse.getUser().getStatuses_count() + "");
            glideRequest.load(mStatuse.getUser().getAvatar_large()).transform(new GlideCircleTransform(this)).into(ivFriendIcon);
        }
        if(mRetStatus!=null){
            setTitle(mRetStatus.getUser().getScreen_name());
            Glide.with(this).load(mRetStatus.getUser().getCover_image_phone()).into(ivUserBackground);
            WeiBoUtils.getFollowMe(this, mParameters, mOauth2AccessToken.getToken(), mRetStatus.getUser().getScreen_name(), tvRelationship, ivRelationship);
            tvDescription.setText(mRetStatus.getUser().getDescription());
            tvAttentionCount.setText(mRetStatus.getUser().getFriends_count() + "");
            tvFansCount.setText(mRetStatus.getUser().getFollowers_count() + "");
            tvWeiboCount.setText(mRetStatus.getUser().getStatuses_count() + "");
            glideRequest.load(mRetStatus.getUser().getAvatar_large()).transform(new GlideCircleTransform(this)).into(ivFriendIcon);
        }

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
        if (getIntent().getStringExtra("type").equals("normal")) {
            mStatuse = (Statuses) getIntent().getSerializableExtra("data");
        }
        if (getIntent().getStringExtra("type").equals("retweet")) {
            mRetStatus = (RetweetedStatus) getIntent().getSerializableExtra("data");
        }


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
