package cn.xavier.movie.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yangxh on 17/8/1.
 */

public class BingImagesInfo implements Parcelable{
    public List<BingImage> images;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.images);
    }

    public BingImagesInfo() {
    }

    protected BingImagesInfo(Parcel in) {
        this.images = in.createTypedArrayList(BingImage.CREATOR);
    }

    public static final Creator<BingImagesInfo> CREATOR = new Creator<BingImagesInfo>() {
        @Override
        public BingImagesInfo createFromParcel(Parcel source) {
            return new BingImagesInfo(source);
        }

        @Override
        public BingImagesInfo[] newArray(int size) {
            return new BingImagesInfo[size];
        }
    };
}
