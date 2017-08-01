package cn.xavier.movie.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import cn.xavier.base.ui.BaseActivity;
import cn.xavier.base.utils.L;
import cn.xavier.movie.App;
import cn.xavier.movie.R;
import cn.xavier.movie.bean.BingImagesInfo;
import cn.xavier.movie.network.RetrofitManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yangxh on 17/8/1.
 */

public class SplashActivity extends BaseActivity {
    private static final float SCALE_VALUE = 1.3f;
    private static final long SCALE_DURATION = 1000;
    @Bind(R.id.iv_splash)
    ImageView iv_splash;

    protected void afterCreate(Bundle bundle) {
        loadSplashImage();//请求bing每日一图
    }

    private void loadSplashImage() {
        RetrofitManager.builderExtr().getBingPic()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BingImagesInfo>() {
                    @Override
                    public void call(BingImagesInfo info) {
                        L.d(info.images.get(0).url);
                        Glide.with(App.getContext()).load("http://www.bing.com/"+info.images.get(0).url)
                                .into(iv_splash);
                        splashAnim();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        iv_splash.setImageResource(R.drawable.splash_placeholder);
                        splashAnim();
                    }
                });
    }

    private void splashAnim() {
        ObjectAnimator scaleX= ObjectAnimator.ofFloat(iv_splash, View.SCALE_X,1.0f,SCALE_VALUE);
        ObjectAnimator scaleY=ObjectAnimator.ofFloat(iv_splash,View.SCALE_Y,1.0f,SCALE_VALUE);

        AnimatorSet as=new AnimatorSet();
        as.setDuration(SCALE_DURATION).play(scaleX).with(scaleY);
        as.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent=new Intent(SplashActivity.this,MoviesListActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        as.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_splash_activity;
    }
}
