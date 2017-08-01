package cn.xavier.movie.network;

import cn.xavier.movie.bean.BingImagesInfo;
import cn.xavier.movie.bean.ImagesInfo;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yangxh on 17/8/1.
 */

public interface BingService {
    @Headers(RetrofitManager.CACHE_CONTROL_AGE+RetrofitManager.CACHE_STALE_SHORT)
    @GET("HPImageArchive.aspx")
    Observable<BingImagesInfo> getBingPic(@Query("format") String format,
                                          @Query("idx") String idx,
                                          @Query("n") String n,
                                          @Query("mkt") String mkt);
}
