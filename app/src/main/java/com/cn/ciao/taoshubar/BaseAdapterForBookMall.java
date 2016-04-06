package com.cn.ciao.taoshubar;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class BaseAdapterForBookMall extends BaseAdapterForBookSell {
    AVUser avUser;
    AlertDialog confirmDialog;
    private Context context;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;

    public BaseAdapterForBookMall(Context context,AVUser avUser) {
        super(context);
        this.context = context;
        this.avUser = avUser;
    }
    public void removeAllItem(){
        sellingBookList.clear();
        ids.clear();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Book item = (Book) getItem(position);
        Book_List_on_Mall listItem = (Book_List_on_Mall) myInflater.inflate(R.layout.book_list_on_mail, null);
        listItem.setBook(item);
        listItem.setCloseListenr(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean choose;
                Calendar calendar = Calendar.getInstance();
                final int year1 = year = calendar.get(Calendar.YEAR);
                final int month1 = month = calendar.get(Calendar.MONTH);
                final int day1 = day = calendar.get(Calendar.DAY_OF_MONTH);
                final int hour1 = hour = calendar.get(Calendar.HOUR);
                final int min1 = min = calendar.get(Calendar.MINUTE);

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.date_time_picker, null);
                DatePicker date = (DatePicker) view.findViewById(R.id.date);
                TimePicker time = (TimePicker) view.findViewById(R.id.time);

                date.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        BaseAdapterForBookMall.this.year = year;
                        BaseAdapterForBookMall.this.month = monthOfYear;
                        BaseAdapterForBookMall.this.day = dayOfMonth;

                        Log.i("date" , "" + year + "  " + month + "  " + day);
                    }
                });

                time.setIs24HourView(true);
                time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        BaseAdapterForBookMall.this.hour = hourOfDay;
                        BaseAdapterForBookMall.this.min = minute;
                        Log.i(" time", "" + hour + min);
                    }
                });

                builder.setView(view);

                builder.setPositiveButton("选择完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //判断年月日是否符合要求
                        if (year < year1) {
                            return;
                        } else if (year == year1) {
                            if (month < month1)
                                return;
                            else if (month == month1) {
                                if (day < day1) {
                                    return;
                                }
                            }
                        }

                        //判断时间是否符合要求
                        if (year == year1 && month == month1 && day == day1) {
                            if (hour < hour1)
                                return;
                            else if (hour == hour1 && min < min1)
                                return;
                        }

                        ++month;
                        final String dateAndTime = "" + year + "-" + month + "-" + day + "," + hour + ":" + min;
                        Log.i("|date time" , dateAndTime);
                        Log.i("clicking   ", "clicking");

                        AVQuery<AVObject> bookqueryItem = new AVQuery<AVObject>("BookOnSell");
                        String id = ids.get(position);
                        bookqueryItem.getInBackground(id, new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {

                                Log.i("saving   ", "saving");

                                avObject.put("OnTrace", "True");
                                avObject.put("CustomerName", avUser.getUsername());
                                avObject.put("Trace_Time", dateAndTime);
                                Book tempBook = (Book) getItem(position);
                                tempBook.setCustomerName(avUser.getUsername());
                                avObject.put("BookList", new JSONArray(tempBook.toList()));
                                avObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        sellingBookList.remove(position);
                                        ids.remove(position);
                                        BaseAdapterForBookMall.this.notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                        Log.i("saving ok ", " oking");

                    }
                }).setNegativeButton("取消", null);
                builder.show();

            }
        });
        return listItem;
    }
}
