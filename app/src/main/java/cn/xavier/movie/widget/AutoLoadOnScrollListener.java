package cn.xavier.movie.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by yangxh on 17/8/7.
 */

public abstract class AutoLoadOnScrollListener extends RecyclerView.OnScrollListener {
    private final LinearLayoutManager mLinearLayoutManager;
    private int totalItemCount,lastVisibleItemPosition;
    private int currentPage=1;
    private boolean loading;

    public AutoLoadOnScrollListener(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager=linearLayoutManager;

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        totalItemCount=mLinearLayoutManager.getItemCount();
        lastVisibleItemPosition=mLinearLayoutManager.findLastVisibleItemPosition();

        if(!loading && (lastVisibleItemPosition>totalItemCount-3)&&dy>0){
            currentPage++;
            onLoadMore(currentPage);
            loading=true;
        }
    }

    public abstract void onLoadMore(int page);
    public void setLoading(boolean loading){
        this.loading=loading;
    }
}
