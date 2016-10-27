package com.app.egguncle.weiegg.utils;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

/**
 * Created by egguncle on 16.10.27.
 * 用来显示微博中的内容，当内容中有@用户名 这样的字段时，高亮显示并且使其可以点击
 */
public class MyClickableSpan extends ClickableSpan {
    String mText;
    public MyClickableSpan(String text){
        super();
        mText=text;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.BLUE);
    }

    @Override
    public void onClick(View view) {
        LogUtils.e(mText+" 被点击了");
    }
}
