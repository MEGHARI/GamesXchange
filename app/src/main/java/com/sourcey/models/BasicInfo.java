package com.sourcey.models;

/**
 * Created by aghiles on 03/12/17.
 */

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class BasicInfo implements Parcelable {

    public String firstName = "aghiles";

    public String lastName = "MEGHARI";

    public String address = "6 rue galilée franconville la garenne";

    public String email= "meghariaghiles@gmail.com";

    public String password = "123456";


    public Uri imageUri ;



    public BasicInfo() { }

    protected BasicInfo(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        address = in.readString();
        email = in.readString();
        password = in.readString();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<BasicInfo> CREATOR = new Creator<BasicInfo>() {
        @Override
        public BasicInfo createFromParcel(Parcel in) {
            return new BasicInfo(in);
        }

        @Override
        public BasicInfo[] newArray(int size) {
            return new BasicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeParcelable(imageUri, flags);
    }
}