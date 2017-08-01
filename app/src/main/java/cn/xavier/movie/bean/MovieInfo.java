package cn.xavier.movie.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


/**
 * Created by yangxh on 17/7/30.
 */

public class MovieInfo implements Parcelable {
    public String id;
    public ImagesInfo images;
    public RatingInfo rating;
    public String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.images, flags);
        dest.writeString(this.title);
        dest.writeParcelable(this.rating,flags);
    }

    public MovieInfo() {
    }

    protected MovieInfo(Parcel in) {
        this.id = in.readString();
        this.images = in.readParcelable(ImagesInfo.class.getClassLoader());
        this.title = in.readString();
        this.rating=in.readParcelable(RatingInfo.class.getClassLoader());
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
