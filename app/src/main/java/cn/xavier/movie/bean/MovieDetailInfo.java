package cn.xavier.movie.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yangxh on 17/8/2.
 */

public class MovieDetailInfo implements Parcelable {
    public ImagesInfo images;
    public String pubdate;
    public ArrayList<String> countries;
    public ArrayList<String> genres;
    public String summary;
    public ArrayList<PersonInfo> directors;
    public ArrayList<PersonInfo> casts;
    public ArrayList<String> aka;
    public ArrayList<CommentInfo> popular_comments;
    public ArrayList<String> durations;
    public ArrayList<String> languages;
    public int comments_count;

    public MovieDetailInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.images, flags);
        dest.writeString(this.pubdate);
        dest.writeStringList(this.countries);
        dest.writeStringList(this.genres);
        dest.writeString(this.summary);
        dest.writeTypedList(this.directors);
        dest.writeTypedList(this.casts);
        dest.writeStringList(this.aka);
        dest.writeTypedList(this.popular_comments);
        dest.writeStringList(this.durations);
        dest.writeStringList(this.languages);
        dest.writeInt(this.comments_count);
    }

    protected MovieDetailInfo(Parcel in) {
        this.images = in.readParcelable(ImagesInfo.class.getClassLoader());
        this.pubdate = in.readString();
        this.countries = in.createStringArrayList();
        this.genres = in.createStringArrayList();
        this.summary = in.readString();
        this.directors = in.createTypedArrayList(PersonInfo.CREATOR);
        this.casts = in.createTypedArrayList(PersonInfo.CREATOR);
        this.aka = in.createStringArrayList();
        this.popular_comments = in.createTypedArrayList(CommentInfo.CREATOR);
        this.durations = in.createStringArrayList();
        this.languages = in.createStringArrayList();
        this.comments_count = in.readInt();
    }

    public static final Creator<MovieDetailInfo> CREATOR = new Creator<MovieDetailInfo>() {
        @Override
        public MovieDetailInfo createFromParcel(Parcel source) {
            return new MovieDetailInfo(source);
        }

        @Override
        public MovieDetailInfo[] newArray(int size) {
            return new MovieDetailInfo[size];
        }
    };
}
