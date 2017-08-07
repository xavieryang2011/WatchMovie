package cn.xavier.movie.network;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.xavier.base.utils.NetUtil;
import cn.xavier.movie.App;
import cn.xavier.movie.bean.BingImagesInfo;
import cn.xavier.movie.bean.MovieDetailInfo;
import cn.xavier.movie.bean.MovieInfo;
import cn.xavier.movie.bean.MoviesList;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by yangxh on 17/7/31.
 */

public class RetrofitManager {
    private static final long CACHE_STALE_LONG = 60 * 60 * 24 * 7;
    public static final long CACHE_STALE_SHORT = 60;
    private static final String BASE_DOUBAN_URL = "https://api.douban.com/v2/movie/";
    private static final String BASE_BING_URL="http://www.bing.com/";
    public static final String CACHE_CONTROL_AGE = "Cache-Control: public, max-age=";
    private static final String API_KEY ="0b2bdeda43b5688921839c8ecb20399b" ;
    private static final String MOVIE_CITY="北京";
    public static final int MOVIE_PAGE_COUNT = 10;
    private static OkHttpClient mOkHttpClient;
    private MovieService mMovieService;
    private  BingService mBingService;

    public static RetrofitManager builder() {
        return new RetrofitManager();
    }
    public static RetrofitManager builderExtr(){
        return new RetrofitManager(NetConstant.BING);
    }

    private RetrofitManager(int source){
        initOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_BING_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBingService = retrofit.create(BingService.class);
    }
    private RetrofitManager() {
        initOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_DOUBAN_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMovieService = retrofit.create(MovieService.class);

    }

    private void initOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (mOkHttpClient == null) {
                    Cache cache = new Cache(new File(App.getContext().getCacheDir(), "HttpCache")
                            , 1024 * 1024 * 100);

                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(mReWriteInterceptor)
                            .addNetworkInterceptor(mReWriteInterceptor)
                            .addInterceptor(httpLoggingInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    private Interceptor mReWriteInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request requst = chain.request();

            if (!NetUtil.isNetworkConnected()) {
                requst = requst.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response originResponse = chain.proceed(requst);
            if (NetUtil.isNetworkConnected()) {
                String cacheControl = requst.cacheControl().toString();
                return originResponse.newBuilder().header("CacheControl", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originResponse.newBuilder()
                        .header("CacheControl", "public,only-if-cached,max-stale=" + CACHE_STALE_LONG)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    public Observable<MoviesList> getLatestMovie(int start) {
        return mMovieService.getLatestMovies(start,MOVIE_PAGE_COUNT);
    }
    public Observable<BingImagesInfo> getBingPic(){
        return mBingService.getBingPic("js","0","1","zh-CN");
    }

    public Observable<MovieDetailInfo> getMovieDetail(String id) {
        return mMovieService.getMovieDetail(id,API_KEY,MOVIE_CITY);
    }
}
