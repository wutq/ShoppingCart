package com.wss.shopping.view;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.wss.shopping.CartApplication;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Describe：
 * Created by 吴天强 on 2017/8/22.
 */

public class Utils {


    /**
     * 返回资源色值 id
     *
     * @param resId resId
     * @return
     */
    public static int getColor(int resId) {
        return CartApplication.getContext().getResources().getColor(resId);
    }

    /**
     * 获取系统时间
     *
     * @return String
     */
    public static String getSysDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(System.currentTimeMillis());
    }

    /**
     * 只能输入英文大小写和数字的正则
     *
     * @param str str
     * @return String
     */
    @NonNull
    public static String stringFilter(String str) {
        String regEx = "[^a-zA-Z0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    /**
     * 随机生成字符串和数值
     *
     * @param length length
     * @return String
     */
    public static String getCharAndNum(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    @NonNull
    public static String getString(int strings) {
        return CartApplication.getContext().getString(strings);
    }

    public static void toast(String msg) {
        Toast.makeText(CartApplication.getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
