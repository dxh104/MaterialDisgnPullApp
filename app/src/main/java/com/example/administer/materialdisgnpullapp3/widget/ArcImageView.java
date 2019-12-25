package com.example.administer.materialdisgnpullapp3.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.administer.materialdisgnpullapp3.R;

/**
 * Created by XHD on 2019/11/20
 */
public class ArcImageView extends android.support.v7.widget.AppCompatImageView {
    float width, height;
    private int defaultRadius = 0;
    private int defaultLineWidth = 0;
    private int bottomRadius;
    private int lineWidth;
    private Path mPath;
    private Paint mPaint;

    public ArcImageView(Context context) {
        this(context, null);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        mPath = new Path();
        mPaint = new Paint();
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // 读取配置
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
        bottomRadius = array.getDimensionPixelOffset(R.styleable.ArcImageView_bottom_radius, defaultRadius);
        lineWidth = array.getDimensionPixelOffset(R.styleable.ArcImageView_line_width, defaultLineWidth);
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        createPaintPath();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(mPath, mPaint);
    }

    private void createPaintPath() {
        mPath.moveTo(0, height - bottomRadius - lineWidth);//起点
        mPath.lineTo(0, height+2);
        mPath.lineTo(width, height+2);//底部直线(+2是防止图片放大时，底部露出间隙)
        mPath.lineTo(width, height - bottomRadius - lineWidth);
        mPath.quadTo(width / 2, height + bottomRadius - lineWidth, 0, height - bottomRadius - lineWidth);//底部弧线
        mPath.close();
    }
}
