package cn.xavier.movie.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangxh on 17/8/1.
 */

public class BingImage implements Parcelable{
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    public BingImage() {
    }

    protected BingImage(Parcel in) {
        this.url = in.readString();
    }

    public static final Creator<BingImage> CREATOR = new Creator<BingImage>() {
        @Override
        public BingImage createFromParcel(Parcel source) {
            return new BingImage(source);
        }

        @Override
        public BingImage[] newArray(int size) {
            return new BingImage[size];
        }
    };
}
