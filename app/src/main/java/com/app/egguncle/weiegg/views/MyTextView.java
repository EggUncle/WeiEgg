package com.app.egguncle.weiegg.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.egguncle.weiegg.R;

/**
 * Created by egguncle on 16.10.27.
 * 自定义一个textview控件，用来显示微博中的内容，当内容中有@用户名 这样的字段时，高亮显示并且使其可以点击
 */
public class MyTextView extends View {

    private View view;
    private LinearLayout lineMytextview;

   // lineMytextview = (LinearLayout) findViewById(R.id.line_mytextview);


    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTextView(Context context) {
       this(context,null);
    }
    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        view=inflate(context, R.layout.mytextview_layout,null);
    }

    /**
     * @param content
     * 用来给控件设置内容
     */
    private void setContent(String content){

    }

}
