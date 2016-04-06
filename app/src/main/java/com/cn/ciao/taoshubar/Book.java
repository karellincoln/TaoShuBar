package com.cn.ciao.taoshubar;

import org.json.JSONArray;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/3 0003.
 */
public class Book {
    public String bookname, authorName, customerName, originalPrice, tracePrice, traceTime, tracePlace,
            ownerName;

    public Book(){

    }
    public Book(String bookname, String authorName, String originalPrice) {
        this.bookname = bookname;
        this.authorName = authorName;
        this.originalPrice = originalPrice;
        customerName ="null";
        tracePrice="null";
        traceTime="null";
        tracePlace="null";
        ownerName="null";
    }

    public Book(String bookname, String authorName, String originalPrice, String tracePrice, String traceTime, String tracePlace, String ownerName) {
        this.bookname = bookname;
        this.authorName = authorName;
        this.originalPrice = originalPrice;
        this.tracePrice = tracePrice;
        this.traceTime = traceTime;
        this.tracePlace = tracePlace;
        this.ownerName = ownerName;
        customerName ="null";

    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authoarName) {
        this.authorName = authoarName;
    }

    public String getCustomertName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getTracePrice() {
        return tracePrice;
    }

    public void setTracePrice(String tracePrice) {
        this.tracePrice = tracePrice;
    }

    public String getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(String traceTime) {
        this.traceTime = traceTime;
    }

    public String getTracePlace() {
        return tracePlace;
    }

    public void setTracePlace(String tracePlace) {
        this.tracePlace = tracePlace;
    }
    public List<String> toList(){
        List<String> temp =new LinkedList<>();
        temp.add(getBookname());
        temp.add(getAuthorName());
        temp.add(getOriginalPrice());
        temp.add(getTracePrice());
        temp.add(getTracePlace());
        temp.add(getTraceTime());
        temp.add(getCustomertName());
        temp.add(getOwnerName());
        return temp;
    }
    public static List<Book> fromList(JSONArray temp) throws Exception{
        List<Book> list =new LinkedList<>();
        for(int i=0;i<temp.length()/8;i++) {
            Book result = new Book(temp.getString(0+8*i),
                    temp.getString(1+8*i),
                    temp.getString(2+8*i));
            result.setTracePrice(temp.getString(3+8*i));
            result.setTracePlace(temp.getString(4+8*i));
            result.setTraceTime(temp.getString(5+8*i));
            result.setCustomerName(temp.getString(6+8*i));
            result.setOwnerName(temp.getString(7+8*i));
            list.add(result);
        }
        return list ;
    }
}
