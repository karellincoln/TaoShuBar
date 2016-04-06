package com.cn.ciao.taoshubar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/2 0002.
 */
public class BookItemInTheList extends LinearLayout {
    TextView authorNameView, bookNameView, originalPriceView, tracePriceView, traceDateView,
            tracePlaceView, targetView, traceTimeView, customerView, ownerView;
    ImageView bookIconView, closeButtonView;
    String authorName, bookName, originalPrice, tracePrice, tracePlace, targetName, traceTime, customerName, ownerName;
    Bitmap BookItem;
    OnClickListener closeListener;

    int iconRight;
    int cancelLeft;

    private boolean show = false;

    public BookItemInTheList(Context context) {
        super(context);
    }

    public BookItemInTheList(Context context, boolean show){
        super(context);
        this.show = show;
    }

    public void setShow(boolean show){
        this.show = show;
    }

    public BookItemInTheList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public BookItemInTheList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.book_item_list_layout, this);
        bookIconView = (ImageView) findViewById(R.id.book_description_icon);
        authorNameView = (TextView) findViewById(R.id.book_description_author);
        bookNameView = (TextView) findViewById(R.id.book_description_bookname);
        originalPriceView = (TextView) findViewById(R.id.book_description_orprice);
        tracePriceView = (TextView) findViewById(R.id.book_description_trprice);
        tracePlaceView = (TextView) findViewById(R.id.book_description_trplace);
        targetView = (TextView) findViewById(R.id.book_description_trtarget);
        ownerView = (TextView) findViewById(R.id.book_description_owner);
        customerView = (TextView) findViewById(R.id.book_description_customer);
        closeButtonView = (ImageView) findViewById(R.id.booke_description_close_button);
    }

    public void setBook(Book book) {
        setOwnerName(book.getOwnerName());
        setBookName(book.getBookname());
        Log.v("书名", bookNameView.getText().toString());
        setTracePrice(book.getTracePrice());
        setTracePlace(book.getTracePlace());
        setAuthorName(book.getAuthorName());
        setOriginalPrice(book.getOriginalPrice());
        Log.i("tracking    ","" + show);
        if(show) {
            traceTimeView = (TextView) findViewById(R.id.book_description_trtime);
            traceDateView = (TextView) findViewById(R.id.book_description_trdate);
            setTraceTime(book.getTraceTime());
        }
        Bitmap item = BitmapFactory.decodeResource(getResources(), R.mipmap.puggy);
        setBookItem(item);
        invalidate();
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        customerView.setText("买方:" + customerName);
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
        bookNameView.setText("书名：" + bookName);
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        ownerView.setText("卖方：" + ownerName);
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
        authorNameView.setText("作者：" + authorName);
    }

    public void setBookItem(Bitmap bookItem) {
        BookItem = bookItem;
        bookIconView.setImageBitmap(bookItem);
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
        targetView.setText("买方:" + targetName);
    }

    public void setTracePlace(String tracePlace) {
        this.tracePlace = tracePlace;
        tracePlaceView.setText("地点：" + tracePlace);
    }

    public void setTracePrice(String tracePrice) {
        this.tracePrice = tracePrice;
        tracePriceView.setText("价格：" + tracePrice);
    }


    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
        originalPriceView.setText("原价：" + originalPrice);
    }

    public void setTraceTime(String traceTime) {
        this.traceTime = traceTime;
        if(traceTime != null) {
            String[] date_time = traceTime.split(",");
            Log.i("track  time   ", traceTime);
            traceTimeView.setText("交易时间：" + date_time[1]);
            traceDateView.setText("交易日期：" + date_time[0]);
        }
    }

    public float px2dp(float pxInput) {
        float result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pxInput, getResources().getDisplayMetrics());
        return result;
    }
    public void setCloseListenr(OnClickListener onClickListener){
        this.closeListener=onClickListener;
        closeButtonView.setOnClickListener(closeListener);
    }

}
