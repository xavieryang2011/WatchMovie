package cn.xavier.movie.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangxh on 17/8/2.
 */

public class CommentInfo implements Parcelable {
    public String content;
    public String created_at;

    public CommentInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.created_at);
    }

    protected CommentInfo(Parcel in) {
        this.content = in.readString();
        this.created_at = in.readString();
    }

    public static final Creator<CommentInfo> CREATOR = new Creator<CommentInfo>() {
        @Override
        public CommentInfo createFromParcel(Parcel source) {
            return new CommentInfo(source);
        }

        @Override
        public CommentInfo[] newArray(int size) {
            return new CommentInfo[size];
        }
    };
}
