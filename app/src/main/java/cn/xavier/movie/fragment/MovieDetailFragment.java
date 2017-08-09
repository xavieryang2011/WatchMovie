package cn.xavier.movie.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import cn.xavier.base.ui.BaseFragment;
import cn.xavier.base.utils.L;
import cn.xavier.base.utils.PrefUtil;
import cn.xavier.movie.R;
import cn.xavier.movie.activity.MovieDetailActivity;
import cn.xavier.movie.adapter.CommentsListAdapter;
import cn.xavier.movie.bean.CommentInfo;
import cn.xavier.movie.bean.MovieDetailInfo;
import cn.xavier.movie.bean.MovieInfo;
import cn.xavier.movie.bean.PersonInfo;
import cn.xavier.movie.network.RetrofitManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yangxh on 17/8/2.
 */

public class MovieDetailFragment extends BaseFragment {
    @Bind(R.id.iv_movie_icon)
    ImageView mIvMovieIcon;
    @Bind(R.id.tv_director)
    TextView mTvDirector;
    @Bind(R.id.tv_actors)
    TextView mTvActors;
    @Bind(R.id.tv_movie_type)
    TextView mTvMovieType;
    @Bind(R.id.tv_movie_country)
    TextView mTvMovieCountry;
    @Bind(R.id.tv_movie_lanuage)
    TextView mTvMovieLanuage;
    @Bind(R.id.tv_movie_date)
    TextView mTvMovieDate;
    @Bind(R.id.tv_movie_long)
    TextView mTvMovieLong;
    @Bind(R.id.tv_another_name)
    TextView mTvAnotherName;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rcl_movie_detail_comments)
    RecyclerView mRclMovieDetailComments;
    @Bind(R.id.tv_summary)
    TextView mTvSummary;
    @Bind(R.id.tv_comments)
    TextView mTvComments;
    @Bind(R.id.clpb_loading)
    ContentLoadingProgressBar mClpbLoading;
    @Bind(R.id.tv_error)
    TextView mTvError;
    @Bind(R.id.tv_load_empty)
    TextView mTvLoadEmpty;
    @Bind(R.id.clpt_movie_detail)
    CollapsingToolbarLayout mClptMovieDetail;
    @Bind(R.id.tv_content)
    TextView mTvContent;

    private MovieInfo mMovieInfo;
    private CommentsListAdapter mCommentAdapter;
    private Snackbar mWrongSnackbar;

    @Override
    protected void afterCreate(Bundle bundle) {
        if (getArguments() != null) {
            mMovieInfo = getArguments().getParcelable(MovieDetailActivity.MOVIE_INFO);
        }
        setHasOptionsMenu(true);
        init();
        loadDate(mMovieInfo.id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        AppCompatActivity activity=(AppCompatActivity)getActivity();
        activity.setSupportActionBar(mToolbar);

//        ActionBar actionBar=activity.getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        setTitleBar();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        mRclMovieDetailComments.setLayoutManager(linearLayoutManager);
        mRclMovieDetailComments.setHasFixedSize(true);
        mCommentAdapter=new CommentsListAdapter(getActivity(),new ArrayList<CommentInfo>());
        mRclMovieDetailComments.setAdapter(mCommentAdapter);
        mRclMovieDetailComments.setNestedScrollingEnabled(false);

        mWrongSnackbar=Snackbar.make(mRclMovieDetailComments,"加载失败，请刷新重试",Snackbar.LENGTH_INDEFINITE)
                .setAction("刷新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadDate(mMovieInfo.id);
                    }
                });

    }

    private void setTitleBar() {
        mClptMovieDetail.setTitle(mMovieInfo.title);
        mClptMovieDetail.setTitleEnabled(true);

//        TypedValue typedValue=new TypedValue();
//        getActivity().getTheme().resolveAttribute(android.R.attr.,typedValue,true);
//        int[] attribute=new int[]{android.R.attr.textColor};
//        TypedArray array=getActivity().obtainStyledAttributes(typedValue.resourceId,attribute);
//        int textColor=array.getColor(0,getResources().getColor(R.color.white));
//        array.recycle();
        if(PrefUtil.isNight(getActivity())) {
            mClptMovieDetail.setCollapsedTitleTextColor(getResources().getColor(R.color.toolbar_Text_Color_Night));
        }else {
            mClptMovieDetail.setCollapsedTitleTextColor(getResources().getColor(R.color.toolbar_Text_Color_Day));
        }
    }

    private void loadDate(String id) {
        RetrofitManager.builder().getMovieDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mClpbLoading.setVisibility(View.VISIBLE);
                    }
                })
                .subscribe(new Action1<MovieDetailInfo>() {
                    @Override
                    public void call(MovieDetailInfo info) {
                        L.d(info.popular_comments.toString());
                        mClpbLoading.setVisibility(View.GONE);
                        mTvError.setVisibility(View.GONE);
                        setData(info);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mTvError.setVisibility(View.VISIBLE);
                        mClpbLoading.setVisibility(View.GONE);
                        mWrongSnackbar.show();
                    }
                });
    }

    private void setData(MovieDetailInfo info) {
        mTvContent.setText("剧情介绍：");
        Glide.with(getActivity()).load(info.images.large).placeholder(R.drawable.ic_placeholder)
                .into(mIvMovieIcon);
        String strActors = "演员：";
        for (PersonInfo person :
                info.casts) {
            strActors += person.name + "/";
        }
        mTvActors.setText(strActors.substring(0, strActors.length() - 1));

        String strDirectors = "导演：";
        for (PersonInfo person :
                info.directors) {
            strDirectors += person.name + "/";
        }
        mTvDirector.setText(strDirectors.substring(0, strDirectors.length() - 1));

        mTvMovieDate.setText("上映日期："+info.pubdate);
        mTvMovieCountry.setText("国家：" + getStringFromArray(info.countries));
        mTvAnotherName.setText("别名：" + getStringFromArray(info.aka));
        mTvMovieLong.setText("时长：" + info.durations.get(0));
        mTvMovieLanuage.setText("语言：" + getStringFromArray(info.languages));
        mTvSummary.setText(info.summary);
        mTvComments.setText("评论区" + "(" + info.comments_count + ")" + ":");
        mTvMovieType.setText("类型：" + getStringFromArray(info.genres));

        setCommontsData(info.popular_comments);
    }

    private void setCommontsData(ArrayList<CommentInfo> comments) {
        mCommentAdapter.changeData(comments);
    }

    private String getStringFromArray(ArrayList<String> items) {
        String result = "";

        if(items==null||items.size()==0) return result ;
        for (String item :
                items) {
            result += item + "/";
        }
        return result.substring(0, result.length() - 1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_movie_detail_fragment;
    }

    public static MovieDetailFragment instance(MovieInfo movieInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MovieDetailActivity.MOVIE_INFO, movieInfo);

        MovieDetailFragment frament = new MovieDetailFragment();
        frament.setArguments(bundle);
        return frament;
    }

}
