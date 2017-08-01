package cn.xavier.movie.network;


import cn.xavier.movie.bean.MoviesList;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * Created by yangxh on 17/7/31.
 */

public interface MovieService {
    @Headers(RetrofitManager.CACHE_CONTROL_AGE+RetrofitManager.CACHE_STALE_SHORT)
    @GET("in_theaters")
    Observable<MoviesList> getLatestMovies();
}
