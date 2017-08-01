package cn.xavier.movie.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangxh on 17/8/1.
 */

public class RatingInfo implements Parcelable {
    public double max;
    public double average;
    public String stars;
    public double min;

    public RatingInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.max);
        dest.writeDouble(this.average);
        dest.writeString(this.stars);
        dest.writeDouble(this.min);
    }

    protected RatingInfo(Parcel in) {
        this.max = in.readDouble();
        this.average = in.readDouble();
        this.stars = in.readString();
        this.min = in.readDouble();
    }

    public static final Creator<RatingInfo> CREATOR = new Creator<RatingInfo>() {
        @Override
        public RatingInfo createFromParcel(Parcel source) {
            return new RatingInfo(source);
        }

        @Override
        public RatingInfo[] newArray(int size) {
            return new RatingInfo[size];
        }
    };
}
