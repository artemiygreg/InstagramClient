package ru.instagramclient.View.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import ru.instagramclient.API.WebAPI;
import ru.instagramclient.API.WebAPIImpl;
import ru.instagramclient.Json.JsonCallback;
import ru.instagramclient.Json.MediaInfoJson;
import ru.instagramclient.Model.Comments;
import ru.instagramclient.Model.Likes;
import ru.instagramclient.Model.MediaInfo;
import ru.instagramclient.R;
import ru.instagramclient.Service.Date.DateService;
import ru.instagramclient.Service.Image.ImageService;
import ru.instagramclient.View.AlertDialog.MyAlertDialog;
import ru.instagramclient.View.AlertDialog.MyAlertDialogImpl;
import ru.instagramclient.View.BitmapAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 17.03.15.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private MyAlertDialog myAlertDialog;
    private WebAPI webAPI;
    private List<Comments> listComments;
    private MediaInfo mediaInfo;
    private String accessToken;
    private Activity activity;
    private int position = 0;
    private OnItemClickListener onItemClickListener;
    private ImageService imageService;
    private Map<Integer, Bitmap> mapProfile = new HashMap<>();
    private Map<Integer, Bitmap> mapPost = new HashMap<>();
    private BitmapAdapter adapterPost = new BitmapAdapter() {
        @Override
        public void cache(Integer key, Bitmap value) {
            mapPost.put(key, value);
        }
    };
    private BitmapAdapter adapterProfile = new BitmapAdapter() {
    @Override
    public void cache(Integer key, Bitmap value) {
            mapProfile.put(key, value);
        }
    };

    public CommentAdapter(List<Comments> listComments, String accessToken, MediaInfo mediaInfo, Activity activity){
        Log.e("CommentAdapter constructor", "run");
        this.listComments = listComments;
        if(listComments.size() == 0){
            Comments comments = new Comments();
            comments.setUsername("");
            comments.setText("");
            comments.setCreatedTime(0);
            listComments.add(comments);
        }
        this.accessToken = accessToken;
        this.mediaInfo = mediaInfo;
        this.activity = activity;
        imageService = new ImageService(activity);
        webAPI = WebAPIImpl.getInstance();
        myAlertDialog = new MyAlertDialogImpl(activity);
    }

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public View view;
    public TextView nameOwner, message, dateCreated, countLike, showAllLike;
    public ImageButton btnLike;
    public ImageView imageProfileOwner, imagePost;
    public CardView cardView;



    public ViewHolder(View v) {
        super(v);
        Log.e("ViewHolder constructor", "run");
        view = v;
        nameOwner = (TextView)v.findViewById(R.id.nameOwner);
        message = (TextView)v.findViewById(R.id.message);
        dateCreated = (TextView)v.findViewById(R.id.dateCreated);
        countLike = (TextView)v.findViewById(R.id.countLikes);
        showAllLike = (TextView)v.findViewById(R.id.showAllLike);
        imageProfileOwner = (ImageView)v.findViewById(R.id.imageProfileOwner);
        imagePost = (ImageView)v.findViewById(R.id.imagePost);
        btnLike = (ImageButton)v.findViewById(R.id.btnLike);
        cardView = (CardView)v.findViewById(R.id.cardView);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(onItemClickListener != null) {
            onItemClickListener.onItemClick(v, getPosition());
        }
    }
}
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        Log.e("onCreateViewHolder", "run");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e("onBindViewHolder", "run");
        final Comments comments = listComments.get(position);

        if(position == 0) {
            holder.imagePost.setVisibility(View.VISIBLE);
            if (mapPost.containsKey(position)) {
                holder.imagePost.setImageBitmap(mapPost.get(position));
            } else {
                imageService.setImageByLink(holder.imagePost, mediaInfo.getStandartImage(), adapterPost, position);
            }
            int countLike = mediaInfo.getCountLike();
            if(countLike > 0){
                holder.countLike.setText(Integer.toString(countLike));
                holder.countLike.setVisibility(View.VISIBLE);
                holder.showAllLike.setVisibility(View.VISIBLE);
            }
            else {
                holder.countLike.setVisibility(View.GONE);
                holder.showAllLike.setVisibility(View.GONE);
            }
            holder.btnLike.setVisibility(View.VISIBLE);
            if(mediaInfo.getUserHasLike()){
                holder.btnLike.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_like_checked));
            }
            else {
                holder.btnLike.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_like));
            }
            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mediaInfo.getUserHasLike()){
                        removeLike(mediaInfo);
                    }
                    else {
                        setLike(mediaInfo);
                    }
                }
            });
            holder.showAllLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webAPI.getLikes(Long.toString(mediaInfo.getIdPost()), accessToken, new JsonCallback() {
                        @Override
                        public void run(JSONObject jsonObject) {
                            try {
                                MediaInfoJson mediaInfoJson = new MediaInfoJson();
                                List<Likes> listLikes = mediaInfoJson.createListLikeFromJson(jsonObject.getJSONArray("data"));
                                Log.e("list", "size - " + listLikes.size());
                                LikeAdapter adapter = new LikeAdapter(listLikes,  activity, R.layout.list_item_like);
                                myAlertDialog.showAlertAllLikes(listLikes, adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            if (mapProfile.containsKey(position)) {
                holder.imageProfileOwner.setImageBitmap(mapProfile.get(position));
            } else {
                imageService.setImageByLink(holder.imageProfileOwner, comments.getProfilePicture(), adapterProfile, position);
            }
            if(comments.getUsername().equals("") && comments.getText().equals("") && comments.getCreatedTime() == 0){
                holder.cardView.setVisibility(View.GONE);
            }
            else {
                holder.cardView.setVisibility(View.VISIBLE);
                holder.nameOwner.setText(comments.getUsername());
                holder.message.setText(comments.getText());
                holder.dateCreated.setText(DateService.convertTimestampToString(comments.getCreatedTime() * 1000, DateService.DATE_AND_TIME_FORMAT_RU));
            }
        }
        else {
            holder.imagePost.setVisibility(View.GONE);
            holder.btnLike.setVisibility(View.GONE);
            holder.showAllLike.setVisibility(View.GONE);
            holder.countLike.setVisibility(View.GONE);
            if (mapProfile.containsKey(position)) {
                holder.imageProfileOwner.setImageBitmap(mapProfile.get(position));
            } else {
                imageService.setImageByLinkWithRounded(holder.imageProfileOwner, comments.getProfilePicture(), adapterProfile, position);
            }
            holder.nameOwner.setText(comments.getUsername());
            holder.message.setText(comments.getText());
            holder.dateCreated.setText(DateService.convertTimestampToString(comments.getCreatedTime() * 1000, DateService.DATE_AND_TIME_FORMAT_RU));
        }
    }

    @Override
    public int getItemCount() {
        return listComments.size();
    }
    public void addItems(Comments list){
        listComments.add(list);
    }
    public static interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    private void setLike(MediaInfo mediaInfo){
        mediaInfo.setCountLike(mediaInfo.getCountLike() + 1);
        mediaInfo.setUserHasLike(true);
        notifyDataSetChanged();
        webAPI.setLike(Long.toString(mediaInfo.getIdPost()), accessToken);
    }
    private void removeLike(MediaInfo mediaInfo){
        mediaInfo.setCountLike(mediaInfo.getCountLike() - 1);
        mediaInfo.setUserHasLike(false);
        notifyDataSetChanged();
        webAPI.removeLike(Long.toString(mediaInfo.getIdPost()), accessToken);
    }
}
