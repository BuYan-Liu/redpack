package com.zhenglong.util;

import com.zhenglong.global.WxParams;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Lion
 * @描述
 * @date 2019/5/19
 */
public class StringUtils {
    public static String dateToStr(Date date,String pattern) {
        if (pattern==null || pattern.isEmpty()) pattern="yyyy-MM-dd HH:mm:ss";
        if (date==null) date=new Date();
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static int yuanToFen(String yuan) {
        int amount=0;
        try {
            return (int) (Double.parseDouble(yuan)*10*10);
        }catch (Exception e) {
            amount=0;
        }
        return amount;
    }

    public static String getTokenStr(String key) {
        return WxParams.MD5(key+getRandomString(10));
    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static boolean isEmpty(String key) {
        if (key==null || "".equals(key.trim())) return true;
        return false;
    }

    @NotNull
    public static String subString(String fromStr, String startStr, String endStr, boolean startWithKey) {
        if (StringUtils.isEmpty(fromStr)) return "";
        if (startWithKey)
            return fromStr.substring(fromStr.indexOf(startStr), fromStr.indexOf(endStr));
        return fromStr.substring(fromStr.indexOf(startStr)+1, fromStr.indexOf(endStr));
    }

}
