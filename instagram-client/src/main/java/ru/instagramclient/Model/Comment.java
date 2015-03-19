package ru.instagramclient.Model;

/**
 * Created by Admin on 12.03.15.
 */
public class Comment {
    private long idComment;
    private String text;
    private long createdTime = 0;
    private long idSender;
    private String profilePicture;
    private String username;
    private String fullName;

    public Comment(){

    }

    public Comment(long idComment, String text, long createdTime, long idSender, String profilePicture, String username, String fullName) {
        this.idComment = idComment;
        this.text = text;
        this.createdTime = createdTime;
        this.idSender = idSender;
        this.profilePicture = profilePicture;
        this.username = username;
        this.fullName = fullName;
    }

    public long getIdComment() {
        return idComment;
    }

    public void setIdComment(long idComment) {
        this.idComment = idComment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getIdSender() {
        return idSender;
    }

    public void setIdSender(long idSender) {
        this.idSender = idSender;
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
}
