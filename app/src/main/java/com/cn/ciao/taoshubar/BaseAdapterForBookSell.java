package com.cn.ciao.taoshubar;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.GetCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/9 0009.
 */
public class BaseAdapterForBookSell extends BaseAdapter {

    List<String> ids =new LinkedList<String>();
    ArrayList<Book> sellingBookList;
    LayoutInflater myInflater;
    public void addBook(Book book) {
        sellingBookList.add(book);
        notifyDataSetChanged();
    }
    public void addBook(Book book,String id){
        ids.add(id);
        addBook(book);
    }
    public BaseAdapterForBookSell(Context context) {
        myInflater=LayoutInflater.from(context);
        sellingBookList = new ArrayList<Book>();
    }

    @Override
    public int getCount() {
        return sellingBookList.size();
    }

    @Override
    public Object getItem(int position) {
        return sellingBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Book item = (Book) getItem(position);
        BookItemInTheList listItem=(BookItemInTheList)myInflater.inflate(R.layout.draw_book_item_test,null);
        listItem.setBook(item);
        listItem.setCloseListenr(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AVQuery<AVObject> bookqueryItem = new AVQuery<AVObject>("BookOnSell");
                String id =ids.get(position);
                bookqueryItem.getInBackground(id, new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        avObject.deleteInBackground();
                    }
                });
                sellingBookList.remove(position);
                ids.remove(position);
                BaseAdapterForBookSell.this.notifyDataSetChanged();
            }
        });
        return listItem;
    }
}
