package com.app.egguncle.weiegg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.views.ToolbarX;

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RelativeLayout rlContent;
    private ToolbarX mToolbarX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_base);
        initView();
        //IOC 控制反转，在父类使用子类的实现
        View v = getLayoutInflater().inflate(getLayoutId(), rlContent, false);
        rlContent.addView(v);

    }

    public abstract int getLayoutId();

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rlContent = (RelativeLayout) findViewById(R.id.rl_content);
        mToolbarX=new ToolbarX(this,toolbar);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    //    overridePendingTransition(R.anim.anim_in_right_left, R.anim.anim_out_right_left);
    }

    @Override
    public void finish() {
        super.finish();
       // overridePendingTransition(R.anim.anim_in_left_right, R.anim.anim_out_left_right);
    }

    public ToolbarX getToolbar(){
        if(null==mToolbarX){
            mToolbarX=new ToolbarX(this,toolbar);
        }
        return mToolbarX;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    //    overridePendingTransition(R.anim.anim_in_right_left, R.anim.anim_out_right_left);
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
//    }
}
