package ru.instagramclient.Model;

import android.os.Parcel;
import android.os.Parcelable;
import ru.instagramclient.Enumirations.TypePost;

import java.util.List;

/**
 * Created by Admin on 12.03.15.
 */
public class MediaInfo implements Parcelable {
    private String id;
    private Long idPost;
    private String link;
    private Boolean userHasLike = false;
    private TypePost typePost;
    private List<Like> listLikes;
    private List<Comment> listComment;
    private long ownerId;
    private String ownerName;
    private String ownerFullName;
    private String ownerProfilePicture;
    private String smallImage;
    private String lowImage;
    private String standartImage;
    private int countLike;
    private int countComments;

    public MediaInfo(){

    }

    public MediaInfo(String id, String link, Boolean userHasLike, TypePost typePost, List<Like> listLikes, List<Comment> listComment, long ownerId, String ownerName, String ownerFullName, String ownerProfilePicture, String smallImage, String lowImage, String standartImage, int countLike, int countComments) {
        this.id = id;
        this.link = link;
        this.userHasLike = userHasLike;
        this.typePost = typePost;
        this.listLikes = listLikes;
        this.listComment = listComment;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerFullName = ownerFullName;
        this.ownerProfilePicture = ownerProfilePicture;
        this.smallImage = smallImage;
        this.lowImage = lowImage;
        this.standartImage = standartImage;
        this.countLike = countLike;
        this.countComments = countComments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getUserHasLike() {
        return userHasLike;
    }

    public void setUserHasLike(Boolean userHasLike) {
        this.userHasLike = userHasLike;
    }

    public TypePost getTypePost() {
        return typePost;
    }

    public void setTypePost(TypePost typePost) {
        this.typePost = typePost;
    }

    public List<Like> getListLikes() {
        return listLikes;
    }

    public void setListLikes(List<Like> listLikes) {
        this.listLikes = listLikes;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getOwnerProfilePicture() {
        return ownerProfilePicture;
    }

    public void setOwnerProfilePicture(String ownerProfilePicture) {
        this.ownerProfilePicture = ownerProfilePicture;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    public String getLowImage() {
        return lowImage;
    }

    public void setLowImage(String lowImage) {
        this.lowImage = lowImage;
    }

    public String getStandartImage() {
        return standartImage;
    }

    public void setStandartImage(String standartImage) {
        this.standartImage = standartImage;
    }

    public List<Comment> getListComment() {
        return listComment;
    }

    public void setListComment(List<Comment> listComment) {
        this.listComment = listComment;
    }

    public int getCountComments() {
        return countComments;
    }

    public void setCountComments(int countComments) {
        this.countComments = countComments;
    }

    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeLong(getIdPost());
        dest.writeString(getLink());
        dest.writeInt((getUserHasLike())?(1):(0));
        dest.writeLong(getOwnerId());
        dest.writeString(getOwnerName());
        dest.writeString(getOwnerFullName());
        dest.writeString(getOwnerProfilePicture());
        dest.writeString(getSmallImage());
        dest.writeString(getLowImage());
        dest.writeString(getStandartImage());
        dest.writeInt(getCountLike());
        dest.writeInt(getCountComments());
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public MediaInfo createFromParcel(Parcel source) {
            MediaInfo mediaInfo = new MediaInfo();
            mediaInfo.setId(source.readString());
            mediaInfo.setIdPost(source.readLong());
            mediaInfo.setLink(source.readString());
            mediaInfo.setUserHasLike(source.readInt() == 1);
            mediaInfo.setOwnerId(source.readLong());
            mediaInfo.setOwnerName(source.readString());
            mediaInfo.setOwnerFullName(source.readString());
            mediaInfo.setOwnerProfilePicture(source.readString());
            mediaInfo.setSmallImage(source.readString());
            mediaInfo.setLowImage(source.readString());
            mediaInfo.setStandartImage(source.readString());
            mediaInfo.setCountLike(source.readInt());
            mediaInfo.setCountComments(source.readInt());

            return mediaInfo;
        }

        @Override
        public MediaInfo[] newArray(int size) {
            return new MediaInfo[0];
        }
    };
}
