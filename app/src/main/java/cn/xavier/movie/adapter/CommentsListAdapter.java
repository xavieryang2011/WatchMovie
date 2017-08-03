package cn.xavier.movie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.xavier.movie.R;
import cn.xavier.movie.bean.CommentInfo;

/**
 * Created by yangxh on 17/8/2.
 */

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.ViewHolder> {

    private  ArrayList<CommentInfo> mInfos;
    private final Context mContext;

    public CommentsListAdapter(Context context, ArrayList<CommentInfo> infos) {
        this.mContext = context;
        this.mInfos = infos;
    }

    @Override
    public CommentsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsListAdapter.ViewHolder holder, int position) {
        CommentInfo info = mInfos.get(position);

        holder.mTvContent.setText(info.content);
    }

    @Override
    public int getItemCount() {
        return mInfos==null?0:mInfos.size();
    }

    public void changeData(ArrayList<CommentInfo> infos) {
        this.mInfos = infos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_content)
        TextView mTvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
