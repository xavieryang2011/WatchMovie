package cn.xavier.movie.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

import butterknife.Bind;
import cn.xavier.base.ui.BaseFragment;
import cn.xavier.base.utils.L;
import cn.xavier.movie.R;
import cn.xavier.movie.adapter.MoviesListAdapter;
import cn.xavier.movie.bean.MovieInfo;
import cn.xavier.movie.bean.MoviesList;
import cn.xavier.movie.network.RetrofitManager;
import cn.xavier.movie.widget.AutoLoadOnScrollListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yangxh on 17/7/28.
 */

public class MoviesListFragment extends BaseFragment implements PullToRefreshView.OnRefreshListener {
    private static final String EXTRA_POSITION = "extra_position";
    private static final String EXTRA_SCROLL = "extra_scroll";
    private static final String EXTRA_CURDATE = "extra_curdate";
    private MoviesListAdapter mExtraAdapter;
    private OnRecyclerViewCreated mOnRecyclerViewCreated;
    private int mPosition;
    private int mScroll;
    private String mCurDate;

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.ptr_column_list)
    PullToRefreshView mPtr_column_List;
    @Bind(R.id.tv_load_empty)
    TextView tv_load_empty;
    @Bind(R.id.tv_error)
    TextView tv_error;
    @Bind(R.id.clpb_loading)
    ContentLoadingProgressBar clpb_loading;
    @Bind(R.id.rcl_column_list)
    RecyclerView rcl_column_list;

    private MoviesListAdapter mMoviesListAdapter;
    private LinearLayoutManager mLineralayoutManager;
    private AutoLoadOnScrollListener mAutoLoadListner;
    private Snackbar mWrongSnackbar;
    private Snackbar mMoreWrongSnackbar;
    private int mCurPage;
    private boolean move;

    @Override
    protected void afterCreate(Bundle bundle) {
        mPosition = getArguments().getInt(EXTRA_POSITION);
        mScroll = getArguments().getInt(EXTRA_SCROLL);
        mCurDate = getArguments().getString(EXTRA_CURDATE);

        init();
        if (mMoviesListAdapter.getMoviesList().size() == 0) {
            loadLatestMovie();
        }
    }

    private void loadMoreMovie(int start) {
        RetrofitManager.builder().getLatestMovie(start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgress();
                    }
                })
                .map(new Func1<MoviesList, MoviesList>() {
                    @Override
                    public MoviesList call(MoviesList list) {
                        L.object(list);
                        return list;
                    }
                })
                .subscribe(new Action1<MoviesList>() {
                    @Override
                    public void call(MoviesList list) {
                        mPtr_column_List.setRefreshing(false);
                        hideProgress();
                        if (list == null||list.subjects.size()==0) {
                            Toast.makeText(getActivity(), "没有更多了！", Toast.LENGTH_SHORT).show();
                        } else {
                            mMoviesListAdapter.addData(list.subjects);
                        }
                        tv_error.setVisibility(View.GONE);
                        mAutoLoadListner.setLoading(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.e(throwable, "getMoreMovie");
                        mPtr_column_List.setRefreshing(false);
                        tv_error.setVisibility(View.VISIBLE);
                        tv_load_empty.setVisibility(View.GONE);
                        hideProgress();
                    }
                });
    }

    private void loadLatestMovie() {
        RetrofitManager.builder().getLatestMovie(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgress();
                    }
                })
                .map(new Func1<MoviesList, MoviesList>() {
                    @Override
                    public MoviesList call(MoviesList list) {
                        L.object(list);
                        return list;
                    }
                })
                .subscribe(new Action1<MoviesList>() {
                    @Override
                    public void call(MoviesList list) {
                        mPtr_column_List.setRefreshing(false);
                        hideProgress();
                        if (list == null||list.subjects.size()==0) {
                            tv_load_empty.setVisibility(View.VISIBLE);
                        } else {
                            mMoviesListAdapter.changeData(list.subjects);
                            tv_load_empty.setVisibility(View.GONE);
                        }
                        tv_error.setVisibility(View.GONE);
                        mAutoLoadListner.setLoading(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.e(throwable, "getLatestMovie");
                        mPtr_column_List.setRefreshing(false);
                        tv_error.setVisibility(View.VISIBLE);
                        tv_load_empty.setVisibility(View.GONE);
                        hideProgress();
                    }
                });
    }

    private void showProgress() {
        clpb_loading.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        clpb_loading.setVisibility(View.GONE);
    }

    private void init() {
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) getActivity();
        mAppCompatActivity.setSupportActionBar(mToolBar);
        mPtr_column_List.setOnRefreshListener(this);

        mLineralayoutManager = new LinearLayoutManager(getActivity());
        rcl_column_list.setLayoutManager(mLineralayoutManager);
        rcl_column_list.setHasFixedSize(true);
        mMoviesListAdapter = new MoviesListAdapter(getActivity(), new ArrayList<MovieInfo>());
        rcl_column_list.setAdapter(mMoviesListAdapter);

        mAutoLoadListner = new AutoLoadOnScrollListener(mLineralayoutManager) {
            @Override
            public void onLoadMore(int page) {
                mCurPage=page;
                loadMoreMovie(page * RetrofitManager.MOVIE_PAGE_COUNT);
            }
        };
        rcl_column_list.addOnScrollListener(mAutoLoadListner);
        rcl_column_list.addOnScrollListener(new RecylerViewListener());

        if(mExtraAdapter!=null){
            mMoviesListAdapter.setAnim(false);
            mMoviesListAdapter.setList(mExtraAdapter.getMoviesList());
            mMoviesListAdapter.notifyDataSetChanged();
            move();
        }

        mMoreWrongSnackbar=Snackbar.make(rcl_column_list,"加载更多的失败，点击重试！",Snackbar.LENGTH_INDEFINITE)
                .setAction("重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadMoreMovie(mCurPage);
                    }
                });
        mWrongSnackbar=Snackbar.make(rcl_column_list,"加载失败！",Snackbar.LENGTH_INDEFINITE)
                .setAction("刷新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadLatestMovie();
                    }
                });
    }

    private void move() {
        if (mPosition < 0 || mPosition >= rcl_column_list.getAdapter().getItemCount()) {
            return;
        }
        int firstItem = mLineralayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLineralayoutManager.findLastVisibleItemPosition();

        if (mPosition <= firstItem) {
            rcl_column_list.scrollToPosition(mPosition);
            move = true;
        } else if (mPosition <= lastItem) {
            int top = rcl_column_list.getChildAt(mPosition - firstItem).getTop() - mScroll;
            rcl_column_list.scrollBy(0, top);
            if (mOnRecyclerViewCreated != null) {
                mOnRecyclerViewCreated.RecyclerViewCreated();
            }
        } else {
            rcl_column_list.scrollToPosition(mPosition);
            move = true;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_movies_list;
    }

    public static MoviesListFragment newInstance(int position, int scroll, MoviesListAdapter adapter, String curDate) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_POSITION, position);
        bundle.putInt(EXTRA_SCROLL, scroll);
        bundle.putString(EXTRA_CURDATE, curDate);
        MoviesListFragment fragment = new MoviesListFragment();
        fragment.setArguments(bundle);
        fragment.mExtraAdapter = adapter;
        return fragment;
    }

    public void setmOnRecyclerViewCreated(OnRecyclerViewCreated mOnRecyclerViewCreated) {
        this.mOnRecyclerViewCreated = mOnRecyclerViewCreated;
    }

    @Override
    public void onRefresh() {
        loadLatestMovie();
    }

    public RecyclerView getRecyclerView() {
        return rcl_column_list;
    }

    public MoviesListAdapter getAdapter() {
        return mMoviesListAdapter;
    }


    public interface OnRecyclerViewCreated {
        void RecyclerViewCreated();
    }

    private class RecylerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(move){
                move=false;
                int n=mPosition-mLineralayoutManager.findFirstVisibleItemPosition();
                if(0<=n&&n<rcl_column_list.getChildCount()){
                    int top=rcl_column_list.getChildAt(n).getTop()-mScroll;
                    rcl_column_list.smoothScrollBy(0,top);
                }
                if(mOnRecyclerViewCreated!=null){
                    mOnRecyclerViewCreated.RecyclerViewCreated();
                }
            }
        }
    }
}
