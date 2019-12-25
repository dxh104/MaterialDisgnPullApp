package com.example.administer.materialdisgnpullapp3.behavior;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.example.administer.materialdisgnpullapp3.R;


/**
 * Created by XHD on 2019/11/26
 */
public class AppbarZoomBehavior extends AppBarLayout.Behavior {

    private AppBarLayout mAppBar;
    private ImageView mIvScale, mIvHead;
    private ConstraintLayout mConstraintTitle;//标题区
    private CollapsingToolbarLayout mCollapsingToolbar;//折叠区
    private ConstraintLayout mConstraintNoCollaps;//非折叠区
    private ConstraintLayout mConstraintPersonInfo;//个人信息

    private int mAppBarHeight;//记录AppbarLayout原始高度
    private int mIvScaleHeight;//记录mIvScale高度
    private int mIvHeadHeight;//记录mIvHead高度
    private int mIvHeadBottom;//mIvHead初始底部距离
    private int mConstraintNoCollapsHeight;//记录mConstraintNoCollaps高度
    private int mConstraintPersonInfoHeight;//记录mConstraintPersonInfo高度
    private int onLayoutChildChild;//记录调用次数
    private int mdYUnconsumed;//未消费距离
    private boolean isAllowScale;//允许缩小放大

    public AppbarZoomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * AppBarLayout布局时调用
     */

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
        if (onLayoutChildChild == 0) {//初始化第一次
            init(parent, abl);
            onLayoutChildChild++;
        }
        return handled;
    }

    //第一次初始化
    private void init(CoordinatorLayout parent, AppBarLayout abl) {
        mAppBar = abl;
        mIvScale = parent.findViewById(R.id.iv_scale);
        mIvHead = parent.findViewById(R.id.iv_head);
        mConstraintNoCollaps = parent.findViewById(R.id.constraintNoCollaps);
        mConstraintTitle = parent.findViewById(R.id.constraintTitle);
        mCollapsingToolbar = parent.findViewById(R.id.collapsingToolbar);
        mConstraintPersonInfo = parent.findViewById(R.id.constraintPersonInfo);
        mAppBarHeight = mAppBar.getHeight();
        mIvScaleHeight = mIvScale.getHeight();
        mIvHeadHeight = mIvHead.getHeight();
        mConstraintNoCollapsHeight = mConstraintNoCollaps.getHeight();
        mConstraintPersonInfoHeight = mConstraintPersonInfo.getHeight();

        mIvHeadBottom = mIvHead.getBottom();

        abl.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                mConstraintTitle.setBackgroundColor(Color.parseColor("#ff0000"));
                mConstraintTitle.getBackground().setAlpha((int) (255 * Float.valueOf(Math.abs(i)) / Float.valueOf(appBarLayout.getTotalScrollRange())));
            }

        });
    }


    /**
     * 是否接受嵌套滚动,只有它返回true,后面的其他方法才会被调用
     */
    @SuppressLint("NewApi")
    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout parent, final AppBarLayout child, View directTargetChild, final View target, int nestedScrollAxes, int type) {
        if (child.getHeight() > mAppBarHeight && valueAnimator != null) {//非初始高度
            isAllowScale = false;//取消放缩
            valueAnimator.cancel();//取消动画
        }
        return true;
    }


    /**
     * 当准备开始嵌套滚动时调用
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if (child.getHeight() > mAppBarHeight && valueAnimator != null && valueAnimator.isRunning()) {//非初始高度且动画运行中
            valueAnimator.cancel();
        }
        mdYUnconsumed = dy;//最后一次惯性滑动的未消费距离
        if (mIvScale != null && child.getBottom() >= mAppBarHeight && dy < 0 && type == ViewCompat.TYPE_TOUCH) {//下滑
            zoomHeaderImageView(child, dy);
        } else {
            if (mIvScale != null && child.getBottom() > mAppBarHeight && dy > 0 && type == ViewCompat.TYPE_TOUCH) {//上滑
                zoomHeaderImageView(child, dy);
                consumed[1] = dy;//缩小时禁用recycleView滑动
            } else {
                super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
            }
        }
    }


    private static final float MAX_ZOOM_HEIGHT = 3000;//放大2倍的阻力，下拉最大高度到3000像素,放大至2倍
    private float mTotalDy;//手指在Y轴滑动的总距离
    private float mScaleValue;//图片缩放比例
    private int mAppBarLastBottom;//Appbar的最近一次的底部距离

    /**
     * 对ImageView进行缩放处理，对AppbarLayout进行高度的设置
     */
    private void zoomHeaderImageView(AppBarLayout abl, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, MAX_ZOOM_HEIGHT);
        mScaleValue = Math.max(1f, 1f + mTotalDy / MAX_ZOOM_HEIGHT);

        ViewCompat.setScaleX(mIvScale, mScaleValue);
        ViewCompat.setScaleY(mIvScale, mScaleValue);

        mAppBarLastBottom = mAppBarHeight + (int) (mIvScaleHeight * (mScaleValue - 1) / 2);
        abl.setBottom(mAppBarLastBottom);//放大AppBar

        mCollapsingToolbar.getLayoutParams().height = mAppBarLastBottom - mConstraintNoCollapsHeight;
        mCollapsingToolbar.requestLayout();//放大折叠区域---->移动非折叠区

        //移动个人信息区
        mConstraintPersonInfo.setTop(mAppBarLastBottom - mConstraintNoCollapsHeight - mConstraintPersonInfoHeight);//如果top也越界mConstraintPersonInfo可能会消失
        mConstraintPersonInfo.setBottom(mAppBarLastBottom - mConstraintNoCollapsHeight);

        //移动头像
        mIvHead.setTop(mAppBarLastBottom - (mAppBarHeight - mIvHeadBottom) - mIvHeadHeight);
        mIvHead.setBottom(mAppBarLastBottom - (mAppBarHeight - mIvHeadBottom));

    }

    private float mVelocityY;

    /**
     * 当嵌套滚动的子View准备快速滚动时调用
     */
    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        if (velocityY < -100) {//如果触发了快速滚动且垂直方向上速度大于100，则禁用缩小动画 允许放大缩小动画
            isAllowScale = true;//允许
        }
        mVelocityY = velocityY;
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


    private int duration;//动画时间 由速率决定
    private int rate = 1500;//决定速度 rate代表放大至两倍收回的要消耗的时间(值越大速度越慢)

    /**
     * 滑动停止的时候，恢复AppbarLayout、ImageView的原始状态
     */
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        isScrollBack();//是否秒弹回
        if (abl.getBottom() == mAppBarHeight && isAllowScale && mdYUnconsumed < 0) {//触碰顶部,启动放缩动画
            mTotalDy = Math.min(-mdYUnconsumed, MAX_ZOOM_HEIGHT);
            mScaleValue = Math.max(1f, 1f + mTotalDy / MAX_ZOOM_HEIGHT);//放大比例
            mAppBarLastBottom = mAppBarHeight;
            duration = (int) ((mScaleValue - 1) * rate * 2);
            recovery(abl, false, duration, 1f, mScaleValue, 1f);
        } else if (abl.getHeight() > mAppBarHeight) {//恢复初始高度
            float mAppbarDistance = abl.getBottom() - mAppBarHeight;//AppBar增长距离
            mScaleValue = mAppbarDistance * 2 / mIvScaleHeight + 1;//缩小比例
            duration = (int) ((mScaleValue - 1) * rate);
            recovery(abl, true, duration / 2, mScaleValue, 1f);
        }

    }

    private ValueAnimator valueAnimator;
    private int currentAppBarHeight;

    /**
     * 通过属性动画的形式，恢复AppbarLayout、ImageView的原始状态
     *
     * @param abl
     */
    private void recovery(final AppBarLayout abl, final boolean isRecovery, int duration, final float... values) {

        valueAnimator = ValueAnimator.ofFloat(values).setDuration(duration);
        currentAppBarHeight = abl.getHeight();

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewCompat.setScaleX(mIvScale, value);
                ViewCompat.setScaleY(mIvScale, value);
                if (isRecovery) {//还原
                    int dt = currentAppBarHeight - mAppBarHeight;
                    abl.setBottom((int) (currentAppBarHeight - (mScaleValue - value) * dt / (mScaleValue - 1)));

                } else {//放大缩小
                    abl.setBottom(mAppBarHeight + (int) (mIvScaleHeight * (value - 1) / 2));
                }
                mCollapsingToolbar.getLayoutParams().height = abl.getBottom() - mConstraintNoCollapsHeight;
                mCollapsingToolbar.requestLayout();//放大折叠区域---->移动非折叠区

                //移动个人信息区
                mConstraintPersonInfo.setTop(abl.getBottom() - mConstraintNoCollapsHeight - mConstraintPersonInfoHeight);//如果top也越界mConstraintPersonInfo可能会消失
                mConstraintPersonInfo.setBottom(abl.getBottom() - mConstraintNoCollapsHeight);

                //移动头像
                mIvHead.setTop(abl.getBottom() - (mAppBarHeight - mIvHeadBottom) - mIvHeadHeight);
                mIvHead.setBottom(abl.getBottom() - (mAppBarHeight - mIvHeadBottom));

                mTotalDy = (value - 1) * MAX_ZOOM_HEIGHT;
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAllowScale = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAllowScale = false;

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.start();


    }

    private void isScrollBack() {
        if (mVelocityY > 100) {//秒弹回
            if (valueAnimator != null && valueAnimator.isRunning())
                valueAnimator.cancel();
            if (mAppBar.getHeight() == mAppBarHeight)
                return;
            ViewCompat.setScaleX(mIvScale, 1f);
            ViewCompat.setScaleY(mIvScale, 1f);


            mAppBar.setBottom(mAppBarHeight);

            mCollapsingToolbar.getLayoutParams().height = mAppBar.getBottom() - mConstraintNoCollapsHeight;
            mCollapsingToolbar.requestLayout();//放大折叠区域---->移动非折叠区

            //移动个人信息区
            mConstraintPersonInfo.setTop(mAppBar.getBottom() - mConstraintNoCollapsHeight - mConstraintPersonInfoHeight);//如果top也越界mConstraintPersonInfo可能会消失
            mConstraintPersonInfo.setBottom(mAppBar.getBottom() - mConstraintNoCollapsHeight);

            //移动头像
            mIvHead.setTop(mAppBar.getBottom() - (mAppBarHeight - mIvHeadBottom) - mIvHeadHeight);
            mIvHead.setBottom(mAppBar.getBottom() - (mAppBarHeight - mIvHeadBottom));

            isAllowScale = false;
            mTotalDy = 0;
            mVelocityY = 0;
            return;
        }
    }

}
