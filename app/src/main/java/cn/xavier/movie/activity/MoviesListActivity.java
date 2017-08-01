package cn.xavier.movie.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import cn.xavier.base.ui.BaseActivity;
import cn.xavier.movie.R;
import cn.xavier.movie.adapter.MoviesListAdapter;
import cn.xavier.movie.fragment.MoviesListFragment;

public class MoviesListActivity extends BaseActivity {


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
//        mFragment.setmOnRecyclerViewCreated(new onViewCreatedListener());
        transaction.replace(R.id.fl_container, mFragment);
        transaction.commit();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_news_list;
    }
}
