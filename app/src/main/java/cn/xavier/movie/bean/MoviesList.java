package cn.xavier.movie.bean;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yangxh on 17/7/31.
 */

public class MoviesList implements Parcelable {
    public List<MovieInfo> subjects;
    protected MoviesList(Parcel in) {
        this.subjects=in.createTypedArrayList(MovieInfo.CREATOR);
    }

    public static final Creator<MoviesList> CREATOR = new Creator<MoviesList>() {
        @Override
        public MoviesList createFromParcel(Parcel in) {
            return new MoviesList(in);
        }

        @Override
        public MoviesList[] newArray(int size) {
            return new MoviesList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(subjects);
    }

    public List<MovieInfo> getMovies(){
        return subjects;
    }
    public void setMovies(List<MovieInfo> movies){
        this.subjects=movies;
    }
}
