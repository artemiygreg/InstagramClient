package ru.instagramclient.Model;

/**
 * Created by Admin on 12.03.15.
 */
public class Like {
    private long id;
    private String profilePicture;
    private String username;
    private String fullName;
    private int countLikes;

    public Like(){

    }

    public Like(long id, String profilePicture, String username, String fullName, int countLikes) {
        this.id = id;
        this.profilePicture = profilePicture;
        this.username = username;
        this.fullName = fullName;
        this.countLikes = countLikes;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(int countLikes) {
        this.countLikes = countLikes;
    }
}
