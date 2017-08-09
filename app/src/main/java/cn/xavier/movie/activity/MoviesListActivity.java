package cn.xavier.movie.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import cn.xavier.base.ui.BaseActivity;
import cn.xavier.base.utils.Constant;
import cn.xavier.base.utils.PrefUtil;
import cn.xavier.movie.R;
import cn.xavier.movie.adapter.MoviesListAdapter;
import cn.xavier.movie.fragment.MoviesListFragment;

public class MoviesListActivity extends BaseActivity {


    private static final long ANIMATION_TIME = 1000;
    @Bind(R.id.fl_main)
   ViewGroup fl_main;
    @Bind(R.id.iv_main)
    ImageView iv_main;
    private MoviesListFragment mFragment;

    @Override
    protected void afterCreate(Bundle bundle) {
     addFragment(0,0,null,null);
    }
    private void addFragment(int position, int scroll, MoviesListAdapter adapter, String curDate) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mFragment != null) {
            transaction.remove(mFragment);
        }
        mFragment = MoviesListFragment.newInstance(position, scroll, adapter, curDate);
        mFragment.setmOnRecyclerViewCreated(new onViewCreatedListener());
        transaction.replace(R.id.fl_container, mFragment);
        transaction.commit();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_movies_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_change_theme:
                changeTheme();
                break;
            case R.id.menu_main_about_me:
                AboutActivity.start(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeTheme() {
        boolean isNight= PrefUtil.isNight(this);
        if(isNight){
            setTheme(Constant.RESOURCES_DAYTHEME);
            PrefUtil.setDay(this);
        }else {
            setTheme(Constant.RESOURCES_NIGHTTHEME);
            PrefUtil.setNight(this);
        }
        setDrawableCatch();
        getState();
    }

    private void getState() {
        RecyclerView recyclerView=mFragment.getRecyclerView();
        recyclerView.stopScroll();

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            LinearLayoutManager layoutManager=(LinearLayoutManager)recyclerView.getLayoutManager();
            int position=layoutManager.findFirstVisibleItemPosition();
            int scroll=recyclerView.getChildAt(0).getTop();
            addFragment(position,scroll,mFragment.getAdapter(),null);
        }
    }

    private void setDrawableCatch() {
        //清除缓存
        fl_main.setDrawingCacheEnabled(false);

        fl_main.setDrawingCacheEnabled(true);
        iv_main.setImageBitmap(fl_main.getDrawingCache());
        iv_main.setAlpha(1f);
        iv_main.setVisibility(View.VISIBLE);
    }

     class onViewCreatedListener implements MoviesListFragment.OnRecyclerViewCreated {
        @Override
        public void RecyclerViewCreated() {
            startAnimation(iv_main);
        }
    }

    private void startAnimation(final ImageView main) {
        ValueAnimator animator=ValueAnimator.ofFloat(1f).setDuration(ANIMATION_TIME);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float n=(float)animation.getAnimatedValue();
                main.setAlpha(1f-n);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iv_main.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
