package com.app.egguncle.weiegg.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.egguncle.weiegg.CWConstant;
import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.utils.SPUtils;
import com.app.egguncle.weiegg.utils.WeiBoUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.WeiboParameters;

public class SendActivity extends AppCompatActivity {

    private TextInputLayout tiContent;
    private ImageView ivExpression;
    private ImageView ivImage;
    private ImageView ivAt;
    private ImageView ivSend;



    private WeiBoUtil weiBoUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        initView();
        initVar();
    }

    private void initView() {
        tiContent = (TextInputLayout) findViewById(R.id.ti_content);
        final EditText editText = tiContent.getEditText();
        tiContent.setHint("分享新鲜事");
        tiContent.setCounterEnabled(true);
        tiContent.setCounterMaxLength(140);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText.getText().length() > 140) {
                    tiContent.setErrorEnabled(true);
                    tiContent.setError("超过指定字数");

                } else {
                    tiContent.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ivExpression = (ImageView) findViewById(R.id.iv_expression);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        ivAt = (ImageView) findViewById(R.id.iv_at);
        ivSend = (ImageView) findViewById(R.id.iv_send);
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().length() > 140) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SendActivity.this);
                    builder.setTitle("错误").setMessage("输入内容超过指定字数").create().show();
                } else if (editText.getText().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SendActivity.this);
                    builder.setTitle("错误").setMessage("输入内容不能为空").create().show();
                } else {
                    weiBoUtil.sendMyWeiBo(SendActivity.this, editText.getText().toString());
                    finish();
                }
            }
        });

    }

    private void initVar() {
        weiBoUtil = WeiBoUtil.getWeiboUtils();
    }
}
