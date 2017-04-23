package com.app.egguncle.weiegg.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.egguncle.weiegg.CWConstant;
import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.utils.ImageUtil;
import com.app.egguncle.weiegg.utils.SPUtils;
import com.app.egguncle.weiegg.utils.WeiBoUtil;
import com.bumptech.glide.Glide;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.WeiboParameters;

public class SendActivity extends AppCompatActivity {

    private TextInputLayout tiContent;
    private ImageView ivExpression;
    private ImageView ivImage;
    private ImageView ivAt;
    private ImageView ivSend;
    private ImageView ivReadySend;

    private boolean hasImg=false;

    private final static String TAG="SendActivity";
    private static final int SELECT_PHOTO = 0;//调用相册照片
    private static String imagePath;

    private WeiBoUtil weiBoUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        initView();
        initVar();
        initAction();
    }

    private void initView() {
        tiContent = (TextInputLayout) findViewById(R.id.ti_content);

        tiContent.setHint("分享新鲜事");
        tiContent.setCounterEnabled(true);
        tiContent.setCounterMaxLength(140);


        ivExpression = (ImageView) findViewById(R.id.iv_expression);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        ivAt = (ImageView) findViewById(R.id.iv_at);
        ivSend = (ImageView) findViewById(R.id.iv_send);
        ivReadySend = (ImageView) findViewById(R.id.iv_ready_send);


    }

    private void initAction(){
        final EditText editText = tiContent.getEditText();
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
                    //如果有图片，则转为发送带图片的微博
                    if (hasImg){
                        weiBoUtil.sendMyWeiBoWithImg(SendActivity.this,editText.getText().toString(),imagePath);
                    }else{
                        weiBoUtil.sendMyWeiBo(SendActivity.this, editText.getText().toString());
                    }
                    finish();
                }
            }
        });

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //申请读取SD卡和调用相机的权限
                if (ContextCompat.checkSelfPermission(SendActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(SendActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        ) {
                    ActivityCompat.requestPermissions(SendActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);

                } else {
                    getImage();
                }

            }
        });

    }


    private void initVar() {
        weiBoUtil = WeiBoUtil.getWeiboUtils();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getImage();
                } else {
                    //权限申请未通过

                }
                break;
            default:

        }
    }

    private void getImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);//调用相册照片
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: ");
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "onActivityResult: SELECT_PHOTO");
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统用这个方法处理图片
                         imagePath = ImageUtil.handleImageOnKitKat(data);
                        Glide.with(this).load(imagePath).into(ivReadySend);
                        Log.i(TAG, "onActivityResult: "+imagePath.substring(imagePath.indexOf("."),imagePath.length()));
                    } else {
                        //4.4以下系统使用这个方法处理图片
                         imagePath = ImageUtil.handleImageBeforeKitKat(data);
                        Glide.with(this).load(imagePath).into(ivReadySend);
                        Log.i(TAG, "onActivityResult: "+imagePath.substring(imagePath.indexOf("."),imagePath.length()));
                      //  displayImage(imagePath);
                    }

                    hasImg=true;
                }


                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
