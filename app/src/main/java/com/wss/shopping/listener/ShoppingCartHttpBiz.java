package com.wss.shopping.listener;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.wss.shopping.entity.ShoppingCartBean;
import com.wss.shopping.utils.JsonReponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Describe：购物车网络帮助类
 * Created by 吴天强 on 2017/9/1.
 */

public class ShoppingCartHttpBiz {

    // 从给定位置读取Json文件
    public static String readJson(InputStream is) {
        // 从给定位置获取文件
        // File file = new File(path);
        BufferedReader reader = null;
        // 返回值,使用StringBuffer
        StringBuffer data = new StringBuffer();
        //
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            // 每次读取文件的缓存
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                data.append(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭文件流
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

    // 给定路径与Json文件，存储到硬盘
    public static void writeJson(String path, Object json, String fileName) {
        BufferedWriter writer = null;
        File file = new File(path + fileName + ".json");
        // 如果文件不存在，则新建一个
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 写入
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // System.out.println("文件写入成功！");
    }

    public static void requestOrderList(Context context, ResponseCallBack callback) {
        try {
            InputStream is = context.getResources().getAssets().open("firm_order.json");
            String s = ShoppingCartHttpBiz.readJson(is);
            callback.handleResponse(new JSONObject(s), 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static List<ShoppingCartBean> handleOrderList(JSONObject response, int errCode) {
        List<ShoppingCartBean> list = null;
        if (isSuccess(response, errCode)) {
            list = JsonReponseHandler.getListFromJsonWithPageEntity(response, new TypeToken<List<ShoppingCartBean>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 是否成功
     *
     * @param response response
     * @param errCode  errCode
     * @return
     */
    public static boolean isSuccess(JSONObject response, int errCode) {
        return errCode == 0 && response != null;
    }

}
