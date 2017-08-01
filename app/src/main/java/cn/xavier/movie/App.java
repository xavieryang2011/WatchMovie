package cn.xavier.movie;

import android.app.Application;
import android.content.Context;

import cn.xavier.base.utils.AppContextUtil;
import cn.xavier.base.utils.L;

/**
 * Created by yangxh on 17/7/31.
 */

public class App extends Application {
    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        L.init();
        AppContextUtil.init(this);
    }

    public static Context getContext() {
        return mApplicationContext;
    }
}
