package com.app.egguncle.weiegg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.egguncle.weiegg.CWConstant;
import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.adapter.WeiboRecyclerViewAdapter;
import com.app.egguncle.weiegg.utils.LogUtils;
import com.app.egguncle.weiegg.utils.SPUtils;
import com.app.egguncle.weiegg.utils.WeiBoUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;


public class HomePageActivity extends AppCompatActivity {


    private Oauth2AccessToken mOauth2AccessToken;
    //  private EditText editText;

    //   private String url = "https://api.weibo.com/2/statuses/friends_timeline.json";
    private AsyncWeiboRunner mAsyncWeiboRunner;
    private WeiboParameters mParameters;
    private String httpMethod;
    private SwipeRefreshLayout srhHome;
    private RecyclerView rcvHome;
    private WeiboRecyclerViewAdapter weiboRecyclerViewAdapter;
    //   private List<Statuses> statusesList;
    private LinearLayoutManager mLinearLayoutManager;
    private int lastVisibleItem;

    private AppBarLayout appbar;
    private FloatingActionButton fabSendWeibo;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initVars();
        initViews();
        WeiBoUtils.getUserInformation(this, mParameters, mOauth2AccessToken.getToken());
        rcvHome.setAdapter(weiboRecyclerViewAdapter);
    }

    private void initVars() {
        mOauth2AccessToken = SPUtils.getInstance(getApplicationContext()).getToken();
        mAsyncWeiboRunner = new AsyncWeiboRunner(this);
        mParameters = new WeiboParameters(CWConstant.APP_KEY);
        httpMethod = "GET";
        SharedPreferences sharedPreferences = getSharedPreferences("WEIEGG", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("ACCESS_TOKEN", "null");
        Boolean isLogin = sharedPreferences.getBoolean("IS_LOGIN", false);
        LogUtils.e(accessToken);

        weiboRecyclerViewAdapter = new WeiboRecyclerViewAdapter(HomePageActivity.this, WeiBoUtils.getmListStatuses());
        WeiBoUtils.getPublicWeiBo(this, mParameters, mOauth2AccessToken.getToken(), WeiBoUtils.GET_NEW_WEIBO,weiboRecyclerViewAdapter);
        WeiBoUtils.getUid(this, mParameters, mOauth2AccessToken.getToken());

    }


    private void initViews() {
        fabSendWeibo = (FloatingActionButton) findViewById(R.id.fab_send_weibo);
        fabSendWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this,SendActivity.class));
            }
        });

        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        srhHome = (SwipeRefreshLayout) findViewById(R.id.srh_home);
        rcvHome = (RecyclerView) findViewById(R.id.rcv_home);
        mLinearLayoutManager = new LinearLayoutManager(this);
        rcvHome.setLayoutManager(mLinearLayoutManager);
        rcvHome.setItemAnimator(new DefaultItemAnimator());


        srhHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srhHome.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WeiBoUtils.getPublicWeiBo(HomePageActivity.this, mParameters, mOauth2AccessToken.getToken(), WeiBoUtils.GET_NEW_WEIBO,weiboRecyclerViewAdapter);
                        weiboRecyclerViewAdapter.notifyDataSetChanged();
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
                    WeiBoUtils.getPublicWeiBo(HomePageActivity.this, mParameters, mOauth2AccessToken.getToken(), WeiBoUtils.GET_OLD_WEIBO,weiboRecyclerViewAdapter);
                    weiboRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });


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
