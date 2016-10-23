package com.app.egguncle.weiegg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.egguncle.weiegg.CWConstant;
import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.utils.SPUtils;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class LandingPageActivity extends AppCompatActivity {

    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private SPUtils mSPUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
       // getToolbar().hide();
        mAuthInfo = new AuthInfo(getApplicationContext(), CWConstant.APP_KEY, CWConstant.REDIRECT_URL, CWConstant.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        mSPUtils = SPUtils.getInstance(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLogin();
//                startActivity(new Intent(LandingPageActivity.this, HomePageActivity.class));
//                finish();
            }
        }, 500);
    }

    private void checkLogin() {
        if (mSPUtils.isLogin()) {
            startActivity(new Intent(LandingPageActivity.this, HomePageActivity.class));
            finish();
        } else {

            mSsoHandler.authorizeWeb(new WeiboAuthListener() {
                @Override
                public void onComplete(Bundle bundle) {
                    Log.v("onCreate", bundle + "");
                    mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                    mSPUtils.saveToken(mAccessToken);
                    startActivity(new Intent(LandingPageActivity.this, HomePageActivity.class));
                    finish();
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    Log.v("onWeiboException", e.toString());
                    e.printStackTrace();
                }

                @Override
                public void onCancel() {
                    Log.v("MY_TAG", "onCancel");
                }
            });
        }
    }

//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_landing_page;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mSsoHandler) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
