package com.cn.ciao.taoshubar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class BaseAdapterForBookBuy extends BaseAdapterForBookSell {
    public void removeAllItem(){
        sellingBookList.clear();
    }
    public BaseAdapterForBookBuy(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Book item = (Book) getItem(position);
        BookItemInTheList listItem=(BookItemInTheList)myInflater.inflate(R.layout.draw_book_item_test,null);
        listItem.setShow(true);
        listItem.setBook(item);
        listItem.setCloseListenr(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AVQuery<AVObject> bookqueryItem = new AVQuery<AVObject>("BookOnSell");
                String id = ids.get(position);
                bookqueryItem.getInBackground(id, new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        avObject.deleteInBackground();
                    }
                });
                sellingBookList.remove(position);
                ids.remove(position);
                BaseAdapterForBookBuy.this.notifyDataSetChanged();
            }
        });
        return listItem;
    }




}
