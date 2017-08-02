package cn.xavier.movie.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import butterknife.Bind;
import cn.xavier.base.ui.BaseActivity;
import cn.xavier.movie.R;
import cn.xavier.movie.bean.MovieInfo;
import cn.xavier.movie.fragment.MovieDetailFragment;

/**
 * Created by yangxh on 17/8/2.
 */

public class MovieDetailActivity extends BaseActivity {
    public static final String MOVIE_INFO = "movie_info";
    private MovieInfo mMovieInfo;
    private MovieDetailFragment mFragment;
    @Bind(R.id.fl_container)
    FrameLayout fl_container;

    @Override
    protected void afterCreate(Bundle bundle) {
        mMovieInfo = getIntent().getParcelableExtra(MOVIE_INFO);
        addFrament();
    }

    private void addFrament() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mFragment == null) {
            mFragment = MovieDetailFragment.instance(mMovieInfo);
        }
        transaction.replace(R.id.fl_container, mFragment);
        transaction.commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_movie_detail_activiy;
    }

    public static void start(Context context, MovieInfo info) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE_INFO, info);
        context.startActivity(intent);
    }
}
