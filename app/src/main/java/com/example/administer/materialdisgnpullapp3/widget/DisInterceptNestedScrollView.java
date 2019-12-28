package com.example.administer.materialdisgnpullapp3.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by XHD on 2019/11/26
 * 防止子View事件失效
 */
public class DisInterceptNestedScrollView extends NestedScrollView {
    public DisInterceptNestedScrollView(Context context) {
        this(context, null);
    }

    public DisInterceptNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DisInterceptNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);//请求父ViewGroup不允许拦截子View事件
            requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }


}
