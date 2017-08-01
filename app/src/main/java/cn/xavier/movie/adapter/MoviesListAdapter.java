package cn.xavier.movie.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.xavier.base.utils.PrefUtil;
import cn.xavier.movie.R;
import cn.xavier.movie.bean.MovieInfo;
import cn.xavier.movie.db.dao.NewDao;

/**
 * Created by yangxh on 17/7/28.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesViewHolder> {

    private static final int ITEM_COLUMNS = 0;
    private final Context mContext;
    private final boolean isNight;
    private List<MovieInfo> mMoviesList;
    private final NewDao newDao;

    public MoviesListAdapter(Context context, List<MovieInfo> moviesList) {
        this.mContext = context;
        this.isNight = PrefUtil.isNight(context);
        this.mMoviesList = moviesList;
        this.newDao = new NewDao(context);
    }

    public List<MovieInfo> getMoviesList() {
        return mMoviesList;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_columns, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        final MovieInfo movieInfo = mMoviesList.get(position);
        if (movieInfo == null) {
            return;
        }
        bindViewHolder(holder, position, movieInfo);
    }

    private void bindViewHolder(MoviesViewHolder holder, int position, MovieInfo info) {
        holder.tv_column_item.setText(info.title);
        if (info.images != null) {
            Glide.with(mContext).load(info.images.large).placeholder(R.drawable.ic_placeholder).into(holder.iv_column_item);
        }
    }

    @Override
    public int getItemCount() {
        return mMoviesList == null ? 0 : mMoviesList.size();
    }

    public void changeData(List<MovieInfo> movies) {
        mMoviesList = movies;
        notifyDataSetChanged();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_column_item)
        ImageView iv_column_item;
        @Bind(R.id.tv_column_item)
        TextView tv_column_item;
        @Bind(R.id.cv_columns_item)
        CardView cv_columns_item;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
