package com.app.egguncle.weiegg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.adapter.WeiboRecyclerViewAdapter;
import com.app.egguncle.weiegg.utils.LogUtils;
import com.app.egguncle.weiegg.utils.SPUtils;
import com.app.egguncle.weiegg.utils.WeiBoUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;


public class HomePageActivity extends AppCompatActivity {

    private Oauth2AccessToken mOauth2AccessToken;

    //   private String url = "https://api.weibo.com/2/statuses/friends_timeline.json";
    private WeiBoUtil weiBoUtil;
    private AsyncWeiboRunner mAsyncWeiboRunner;
    private WeiboParameters mParameters;
    private String httpMethod;
    private SwipeRefreshLayout srhHome;
    private RecyclerView rcvHome;
    private WeiboRecyclerViewAdapter weiboRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int lastVisibleItem;

    private AppBarLayout appbar;
    private FloatingActionButton fabSendWeibo;

    private Toolbar toolbar;

    //广播相关
    private HomePageActivity.HomeReceiver homeReceiver;
    public static LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    public final static String HOME_BROADCAST = "com.app.egguncle.weiegg.HOME_BOROADCAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initVars();
        initViews();
        initAction();

        rcvHome.setAdapter(weiboRecyclerViewAdapter);
    }

    private void initVars() {
        mOauth2AccessToken = SPUtils.getInstance(getApplicationContext()).getToken();
        mAsyncWeiboRunner = new AsyncWeiboRunner(this);
      //  mParameters = new WeiboParameters(CWConstant.APP_KEY);
        httpMethod = "GET";
        SharedPreferences sharedPreferences = getSharedPreferences("WEIEGG", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("ACCESS_TOKEN", "null");
        Boolean isLogin = sharedPreferences.getBoolean("IS_LOGIN", false);
        LogUtils.e(accessToken);



        //广播相关
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(HOME_BROADCAST);
        homeReceiver = new HomePageActivity.HomeReceiver();
        localBroadcastManager.registerReceiver(homeReceiver, intentFilter);

        //网络模块
        weiBoUtil=WeiBoUtil.getWeiboUtils();
        weiBoUtil.getUserInformation(this);
        weiboRecyclerViewAdapter = new WeiboRecyclerViewAdapter(HomePageActivity.this, weiBoUtil.getmListStatuses());
        weiBoUtil.getPublicWeiBo(this, weiBoUtil.GET_NEW_WEIBO);
        weiBoUtil.getUid(this);
    }


    private void initViews() {
        fabSendWeibo = (FloatingActionButton) findViewById(R.id.fab_send_weibo);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        srhHome = (SwipeRefreshLayout) findViewById(R.id.srh_home);
        rcvHome = (RecyclerView) findViewById(R.id.rcv_home);
        mLinearLayoutManager = new LinearLayoutManager(this);
        rcvHome.setLayoutManager(mLinearLayoutManager);
        rcvHome.setItemAnimator(new DefaultItemAnimator());
    }

    public void initAction(){
        fabSendWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this,SendActivity.class));
            }
        });
        srhHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srhHome.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        weiBoUtil.getPublicWeiBo(HomePageActivity.this, weiBoUtil.GET_NEW_WEIBO);

                        srhHome.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        srhHome.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));


        //recyclerview滚动监听
        rcvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                // 滑动状态停止并且剩余少于两个item时，自动加载下一页
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 5 >= mLinearLayoutManager.getItemCount()) {
                    LogUtils.e("你们镇定一下，我要加载数据了");
                    weiBoUtil.getPublicWeiBo(HomePageActivity.this, weiBoUtil.GET_OLD_WEIBO);

                }
//                //当惯性滚动或静止时，加载图片
//                if(newState==RecyclerView.SCROLL_STATE_SETTLING||newState == RecyclerView.SCROLL_STATE_IDLE){
//                    weiboRecyclerViewAdapter.loadImg();
//                }else if(newState==RecyclerView.SCROLL_STATE_DRAGGING){
//                    weiboRecyclerViewAdapter.stopLoadImg();
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });

    }

    private class HomeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            weiboRecyclerViewAdapter.notifyDataSetChanged();
        }
    }


    public static LocalBroadcastManager getLocalBroadcastManager(){
        return localBroadcastManager;
    }

//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_home_page;
//    }

    //使用异步加载来处理一下第一次的数据显示
//    private class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            srhHome.setRefreshing(true);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            WeiBoUtils.getPublicWeiBo(HomePageActivity.this, mParameters, mOauth2AccessToken.getToken(), WeiBoUtils.GET_NEW_WEIBO,weiboRecyclerViewAdapter);
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            weiboRecyclerViewAdapter.notifyDataSetChanged();
//        }
//    }


}
