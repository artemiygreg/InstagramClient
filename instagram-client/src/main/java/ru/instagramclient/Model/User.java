package ru.instagramclient.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 10.03.15.
 */
public class User implements Parcelable {
    private Long id;
    private String username;
    private String accessToken;
    private String fullName;
    private String imageProfile;

    public User(){

    }
    public User(Long id){
        this.id = id;
    }
    public User(Long id, String username, String fullName, String imageProfile) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.imageProfile = imageProfile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getAccessToken());
        dest.writeString(getFullName());
        dest.writeString(getUsername());
        dest.writeString(getImageProfile());
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public User createFromParcel(Parcel source) {
            User user = new User();
            user.setId(source.readLong());
            user.setAccessToken(source.readString());
            user.setFullName(source.readString());
            user.setUsername(source.readString());
            user.setImageProfile(source.readString());

            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[0];
        }
    };
}
