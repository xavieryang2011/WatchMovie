package cn.xavier.movie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.xavier.base.ui.BaseActivity;

/**
 * Created by yangxh on 17/8/8.
 */

public class AboutActivity extends BaseActivity {
    @Override
    protected void afterCreate(Bundle bundle) {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    public static void start(Context context) {
        Intent intent=new Intent(context,AboutActivity.class);
        context.startActivity(intent);
    }
}
