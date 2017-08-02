package cn.xavier.movie.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangxh on 17/8/2.
 */

public class PersonInfo implements Parcelable {
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public PersonInfo() {
    }

    protected PersonInfo(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<PersonInfo> CREATOR = new Creator<PersonInfo>() {
        @Override
        public PersonInfo createFromParcel(Parcel source) {
            return new PersonInfo(source);
        }

        @Override
        public PersonInfo[] newArray(int size) {
            return new PersonInfo[size];
        }
    };
}
