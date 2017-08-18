package com.leiyun.to8totitle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 *
 * @author Yun.Lei
 *
 */

public class ColorTrackImageView extends AppCompatImageView {
    private Paint mPaint;
    private float mProgress;
    private int mTextOriginColor = 0xff000000;
    private int mTextChangeColor = 0xffff0000;

    public ColorTrackImageView(Context context) {
        this(context,null);
    }

    public ColorTrackImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.ColorTrackImageView);
        mTextOriginColor = ta.getColor(
                R.styleable.ColorTrackImageView_icon_origin_color, mTextOriginColor);
        mTextChangeColor = ta.getColor(
                R.styleable.ColorTrackImageView_icon_change_color, mTextChangeColor);
        ta.recycle();
        setColorFilter(mTextOriginColor);
    }


    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mTextChangeColor);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(0, getMeasuredHeight() * (1 - mProgress), getMeasuredWidth(), getMeasuredHeight());// left, top,
        if (drawableToBitmap(getDrawable()) != null) {
            canvas.drawBitmap(tintBitmap(drawableToBitmap(getDrawable()), mTextChangeColor), getLeft(), getTop(), mPaint);
        }
        canvas.restore();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;

    }

    public static Bitmap tintBitmap(Bitmap inBitmap, int tintColor) {
        if (inBitmap == null) {
            return null;
        }
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inBitmap, 0, 0, paint);
        return outBitmap;
    }

}
