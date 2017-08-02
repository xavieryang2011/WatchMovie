package cn.xavier.movie.network;


import cn.xavier.movie.bean.ImagesInfo;
import cn.xavier.movie.bean.MovieDetailInfo;
import cn.xavier.movie.bean.MoviesList;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yangxh on 17/7/31.
 */

public interface MovieService {
    @Headers(RetrofitManager.CACHE_CONTROL_AGE+RetrofitManager.CACHE_STALE_SHORT)
    @GET("in_theaters")
    Observable<MoviesList> getLatestMovies();

    @Headers(RetrofitManager.CACHE_CONTROL_AGE+RetrofitManager.CACHE_STALE_SHORT)
    @GET("subject/{id}")
    Observable<MovieDetailInfo> getMovieDetail(@Path("id") String id,
                                               @Query("apikey") String apikey,
                                               @Query("city") String city);
}
