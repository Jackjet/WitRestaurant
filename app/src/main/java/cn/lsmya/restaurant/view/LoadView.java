package cn.lsmya.restaurant.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import cn.lsmya.restaurant.R;

public class LoadView extends AppCompatImageView {
    private int width;
    private int height;

    public LoadView(Context context) {
        super(context);
        init();
    }

    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
        setImageBitmap(bitmap);
        width = bitmap.getWidth() / 2;
        height = bitmap.getHeight() / 2;
    }

    /**
     * loading start
     */
    private Matrix max;
    private ValueAnimator animator;

    private void start() {
        if (max == null || animator == null) {
            max = new Matrix();
            animator = ValueAnimator.ofInt(0, 12);
            animator.setDuration(12 * 80);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(Animation.INFINITE);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float degrees = 30 * (Integer) valueAnimator.getAnimatedValue();
                    max.setRotate(degrees, width, height);
                    setImageMatrix(max);
                }
            });
        }

        animator.start();
    }

    private void stop() {
        animator.cancel();
        animator = null;
        max = null;

    }


}