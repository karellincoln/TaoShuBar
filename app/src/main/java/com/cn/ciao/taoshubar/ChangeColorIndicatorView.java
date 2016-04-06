package com.cn.ciao.taoshubar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2016/2/27 0027.
 */
public class ChangeColorIndicatorView extends View{

    private final static int defaultDstColor = 0xff000055;
    private final static int defaultTextColor = 0xffffff;
    private final static int defaultViewColor =0xfffaf0;
    private float mAlpha = 0f;
    Bitmap indicatorIcon;
    Bitmap cacheBitmap;
    String indicatorText;
    Rect textRect;
    Rect bitmapRect;
    private final static int defaultTextSize = 14;
    Paint mTextPaint;

    public ChangeColorIndicatorView(Context context) {
        this(context, null);
    }

    public ChangeColorIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        textRect = new Rect();
        TypedArray ta = (TypedArray) context.obtainStyledAttributes(attrs, R.styleable.ChangeColorIndicatorView);
        indicatorText = ta.getString(R.styleable.ChangeColorIndicatorView_indicatortext);
        BitmapDrawable iconDrawable = (BitmapDrawable) ta.getDrawable(R.styleable.ChangeColorIndicatorView_indicatoricon);
        indicatorIcon = iconDrawable.getBitmap();
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, defaultTextSize, getResources().getDisplayMetrics());
        ta.recycle();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(0xff555555);
        mTextPaint.getTextBounds(indicatorText, 0, indicatorText.length(), textRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int bitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - textRect.height());
        int left = getMeasuredWidth() / 2 - bitmapWidth / 2;
        int top = getMeasuredHeight() / 2 - textRect.height() / 2 - bitmapWidth / 2;
        bitmapRect = new Rect(left, top, left + bitmapWidth, top + bitmapWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint antialias = new Paint();
        antialias.setDither(true);
        antialias.setAntiAlias(true);
        canvas.drawBitmap(indicatorIcon, null, bitmapRect, antialias);
        int alpha = (int) Math.ceil(mAlpha * 255);
        setupIndicatorBitmap(alpha);
        drawSrcText(canvas, alpha);
        drawDstText(canvas, alpha);
        antialias.setAlpha(alpha);
        canvas.drawBitmap(cacheBitmap,0,0,antialias);
    }

    public void setupIndicatorBitmap(int alpha) {
        cacheBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Paint bitmapPaint = new Paint();
        Canvas cacheCanvas = new Canvas(cacheBitmap);
        bitmapPaint.setAlpha(alpha);
        bitmapPaint.setColor(defaultDstColor);
        bitmapPaint.setDither(true);
        bitmapPaint.setAntiAlias(true);
        cacheCanvas.drawRect(bitmapRect, bitmapPaint);
        bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        bitmapPaint.setAlpha(255);
        cacheCanvas.drawBitmap(indicatorIcon, null, bitmapRect, bitmapPaint);
    }

    public void drawSrcText(Canvas canvas, int alpha) {
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(defaultTextColor);
        mTextPaint.setAlpha(255 - alpha);
        canvas.drawText(indicatorText, 0, indicatorText.length(), bitmapRect.left + bitmapRect.width() / 2 - textRect.width() / 2, bitmapRect.bottom + textRect.height(), mTextPaint);
    }

    public void drawDstText(Canvas canvas, int alpha) {
        mTextPaint.setColor(defaultDstColor);
        mTextPaint.setAlpha(alpha);
        canvas.drawText(indicatorText, 0, indicatorText.length(), bitmapRect.left + bitmapRect.width() / 2 - textRect.width() / 2, bitmapRect.bottom + textRect.height(), mTextPaint);
    }

    public void setmAlpha(float alpha) {
        mAlpha = alpha;
        invalidateIndicator();
    }

    public void invalidateIndicator() {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            invalidate();
        } else {
            postInvalidate();
        }
    }


}
