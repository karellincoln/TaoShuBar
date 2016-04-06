package com.cn.ciao.taoshubar;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/7 0007.
 */
public class ToStringUtils {


    /**
     * Object To json String
     *
     * @param obj
     * @return json String
     */
    public static String objToJsonString(Object obj) {

        // 初始化返回值
        String json = "str_empty";

        if (obj == null) {
            return json;
        }

        StringBuilder buff = new StringBuilder();
        Field[] fields = obj.getClass().getFields();
        try {
            buff.append("{");
            int i = 0;
            for (Field field : fields) {
                if (i != 0) {
                    buff.append(",");
                }
                buff.append("\"");
                buff.append(field.getName());
                buff.append("\"");
                buff.append(":");
                buff.append("\"");
                buff.append(field.get(obj) == null ? "" : field.get(obj));
                buff.append("\"");
                i++;
            }
            buff.append("}");
            json = buff.toString();
        } catch (Exception e) {
            throw new RuntimeException("cause:" + e.toString());
        }
        return json;
    }
    public static List<String> listToList(List comlist){
        List<String> list =new LinkedList<String>();
        for( Object temp:comlist){
            list.add(objToJsonString(temp));
        }
        return list;
    }
    public static  String listToString(List ss) {
        StringBuffer s = new StringBuffer("");
        if (null != ss) {
            String[] str = new String[ss.size()];
            for (int i=0; i<ss.size(); i++){
                str[i] = ss.get(i).toString();
            }
            arrayToString(str);
            s.append(arrayToString(str));
        }
        return s.toString();
    }

    /**
     * 把数组转换成'',格式的字符串输出
     * @param ss
     * @return
     */
    public static String arrayToString(String[] ss){
        StringBuffer s = new StringBuffer("");
        s.append("[");
        if(null != ss){
            for(int i=0;i<ss.length-1;i++){
                       s .append(ss[i])

                        .append(",");
            }
            if(ss.length>0){
                s.append(ss[ss.length-1]).append("]");
            }
        }
        return s.toString();
    }
}
