package com.wss.shopping;

import android.app.Application;
import android.content.Context;

/**
 * Describe：
 * Created by 吴天强 on 2017/9/1.
 */

public class CartApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
