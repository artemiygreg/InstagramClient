package ru.instagramclient.Json;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.instagramclient.Model.Comments;
import ru.instagramclient.Model.Likes;
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
        List<Likes> listLikes = new ArrayList<Likes>();
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
                mediaInfo.setListLikes(createListLikeFromJson(jsonArrayLikes));
                mediaInfo.setListComment(createListCommentsFromJson(jsonArrayComments));

                mediaInfoList.add(mediaInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mediaInfoList;
    }
    public List<Likes> createListLikeFromJson(JSONArray jsonArray){
        List<Likes> list = new ArrayList<Likes>();
        try {
            for(int j = 0; j < jsonArray.length(); j++){
                Likes likes = new Likes();
                likes.setFullName(jsonArray.getJSONObject(j).getString("full_name"));
                likes.setId(jsonArray.getJSONObject(j).getLong("id"));
                likes.setUsername(jsonArray.getJSONObject(j).getString("username"));
                likes.setProfilePicture(jsonArray.getJSONObject(j).getString("profile_picture"));

                list.add(likes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Comments> createListCommentsFromJson(JSONArray jsonArray){
        List<Comments> list = new ArrayList<Comments>();
        try {
            for(int j = 0; j < jsonArray.length(); j++){
                Comments comments = new Comments();
                comments.setProfilePicture(jsonArray.getJSONObject(j).getJSONObject("from").getString("profile_picture"));
                comments.setUsername(jsonArray.getJSONObject(j).getJSONObject("from").getString("username"));
                comments.setFullName(jsonArray.getJSONObject(j).getJSONObject("from").getString("full_name"));
                comments.setIdSender(jsonArray.getJSONObject(j).getJSONObject("from").getLong("id"));
                comments.setIdComment(jsonArray.getJSONObject(j).getLong("id"));
                comments.setCreatedTime(jsonArray.getJSONObject(j).getLong("created_time"));
                comments.setText(jsonArray.getJSONObject(j).getString("text"));

                list.add(comments);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}