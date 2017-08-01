package cn.xavier.movie.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangxh on 17/8/1.
 */

 public class ImagesInfo implements Parcelable {
    public String small;
    public String large;
    public String medium;

    protected ImagesInfo(Parcel in) {
        small=in.readString();
        large=in.readString();
        medium=in.readString();
    }

    public static final Creator<ImagesInfo> CREATOR = new Creator<ImagesInfo>() {
        @Override
        public ImagesInfo createFromParcel(Parcel in) {
            return new ImagesInfo(in);
        }

        @Override
        public ImagesInfo[] newArray(int size) {
            return new ImagesInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(small);
        dest.writeString(large);
        dest.writeString(medium);
    }
}
