package com.app.egguncle.weiegg.views;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by egguncle on 16.10.10.
 */
public class ToolbarX {
    private Toolbar mToolbar;
    private AppCompatActivity mActivity;
    private ActionBar mActionBar;

    public ToolbarX(final AppCompatActivity mActivity, Toolbar mToolbar) {
        this.mActivity = mActivity;
        this.mToolbar = mToolbar;
        mActivity.setSupportActionBar(mToolbar);
        mActionBar = mActivity.getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
    }

    public ToolbarX setTitle(String text) {
        mActionBar.setTitle(text);
        return this;
    }

    public ToolbarX setSubTitle(String text) {
        mActionBar.setSubtitle(text);
        return this;
    }

    public ToolbarX setTitle(int resId) {
        mActionBar.setTitle(resId);
        return this;
    }

    public ToolbarX setSubTitle(int resId) {
        mActionBar.setSubtitle(resId);
        return this;
    }

    public ToolbarX setNavigationOnClickListener(View.OnClickListener listener) {
        mToolbar.setNavigationOnClickListener(listener);
        return this;
    }

    public ToolbarX setNavigationIcon(int resId){
        mToolbar.setNavigationIcon(resId);
        return this;
    }

    public ToolbarX setDisplayHomeAsUpEnabled(boolean show){
        mActionBar.setDisplayHomeAsUpEnabled(show);
        return this;
    }

    public ToolbarX hide(){
        mActionBar.hide();
        return this;
    }

}
