package ru.instagramclient.Json;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.instagramclient.Model.Comment;
import ru.instagramclient.Model.Like;
import ru.instagramclient.Model.MediaInfo;
import ru.instagramclient.Service.String.StringService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12.03.15.
 */
public class MediaInfoJson {

    public MediaInfoJson(){

    }

    public List<MediaInfo> createListFromJson(JSONObject jsonObject){
        List<MediaInfo> mediaInfoList = new ArrayList<MediaInfo>();
        try {
            for(int i = 0; i < jsonObject.getJSONArray("data").length(); i++){

                Log.e("pag", jsonObject.getJSONObject("pagination").toString());
                String id = jsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                Long idPost = StringService.parseString(id);
                String link = jsonObject.getJSONArray("data").getJSONObject(i).getString("link");
                boolean userHasLike = jsonObject.getJSONArray("data").getJSONObject(i).getBoolean("user_has_liked");
                String ownerFullName = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("user").getString("full_name");
                long ownerId = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("user").getLong("id");
                String ownerName = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("user").getString("username");
                String ownerProfilePicture = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("user").getString("profile_picture");
                String smallImage = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("images").getJSONObject("thumbnail").getString("url");
                String lowImage = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("images").getJSONObject("low_resolution").getString("url");
                String standartImage = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                int countLike = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("likes").getInt("count");
                int countComment = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("comments").getInt("count");

                JSONArray jsonArrayLikes = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("likes").getJSONArray("data");
                JSONArray jsonArrayComments = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("comments").getJSONArray("data");
                List<Like> listLikes = createListLikeFromJson(jsonArrayLikes);
                List<Comment> listComments = createListCommentsFromJson(jsonArrayComments);

                MediaInfo mediaInfo = new MediaInfo();
                mediaInfo.setId(id);
                mediaInfo.setIdPost(idPost);
                mediaInfo.setLink(link);
                mediaInfo.setUserHasLike(userHasLike);
                mediaInfo.setOwnerFullName(ownerFullName);
                mediaInfo.setOwnerId(ownerId);
                mediaInfo.setOwnerName(ownerName);
                mediaInfo.setOwnerProfilePicture(ownerProfilePicture);
                mediaInfo.setSmallImage(smallImage);
                mediaInfo.setLowImage(lowImage);
                mediaInfo.setStandartImage(standartImage);
                mediaInfo.setCountLike(countLike);
                mediaInfo.setCountComments(countComment);
                mediaInfo.setListLikes(listLikes);
                mediaInfo.setListComment(listComments);

                mediaInfoList.add(mediaInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mediaInfoList;
    }
    public List<Like> createListLikeFromJson(JSONArray jsonArray){
        List<Like> list = new ArrayList<Like>();
        try {
            for(int j = 0; j < jsonArray.length(); j++){
                Like like = new Like();
                like.setFullName(jsonArray.getJSONObject(j).getString("full_name"));
                like.setId(jsonArray.getJSONObject(j).getLong("id"));
                like.setUsername(jsonArray.getJSONObject(j).getString("username"));
                like.setProfilePicture(jsonArray.getJSONObject(j).getString("profile_picture"));

                list.add(like);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Comment> createListCommentsFromJson(JSONArray jsonArray){
        List<Comment> list = new ArrayList<Comment>();
        try {
            for(int j = 0; j < jsonArray.length(); j++){
                Comment comment = new Comment();
                comment.setProfilePicture(jsonArray.getJSONObject(j).getJSONObject("from").getString("profile_picture"));
                comment.setUsername(jsonArray.getJSONObject(j).getJSONObject("from").getString("username"));
                comment.setFullName(jsonArray.getJSONObject(j).getJSONObject("from").getString("full_name"));
                comment.setIdSender(jsonArray.getJSONObject(j).getJSONObject("from").getLong("id"));
                comment.setIdComment(jsonArray.getJSONObject(j).getLong("id"));
                comment.setCreatedTime(jsonArray.getJSONObject(j).getLong("created_time"));
                comment.setText(jsonArray.getJSONObject(j).getString("text"));

                list.add(comment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}