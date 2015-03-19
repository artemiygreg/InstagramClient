package ru.instagramclient.View.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.instagramclient.API.WebAPI;
import ru.instagramclient.API.WebAPIImpl;
import ru.instagramclient.Model.Comment;
import ru.instagramclient.Model.MediaInfo;
import ru.instagramclient.R;
import ru.instagramclient.Service.Image.ImageProccessor;
import ru.instagramclient.Service.Image.ImageService;
import ru.instagramclient.View.Activity.DetailPostActivity;
import ru.instagramclient.View.BitmapAdapter;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 12.03.15.
 */
public class MediaInfoAdapter extends RecyclerView.Adapter<MediaInfoAdapter.ViewHolder> {
    private String accessToken;
    private WebAPI webAPI;
    private List<MediaInfo> listMedia;
    private Activity activity;
    private OnItemClickListener onItemClickListener;
    private ImageService imageService;
    private Map<Integer, Bitmap> mapPost = new HashMap<Integer, Bitmap>();
    private Map<Integer, Bitmap> mapProfile = new HashMap<Integer, Bitmap>();
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
    private int position = 0;

    public MediaInfoAdapter(List<MediaInfo> listMedia, Activity activity, String accessToken){
        this.listMedia = listMedia;
        this.activity = activity;
        this.accessToken = accessToken;
        imageService = new ImageService(activity);
        webAPI = WebAPIImpl.getInstance();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
        public TextView nameOwner, countLikes, countComment,textViewDivider;
        public ImageButton btnLike, btnComment;
        public ImageView imageProfileOwner, imagePost;
        public LinearLayout layoutComment;


        public ViewHolder(View v) {
            super(v);
            view = v;
            nameOwner = (TextView)v.findViewById(R.id.nameOwner);
            countLikes = (TextView)v.findViewById(R.id.countLike);
            countComment = (TextView)v.findViewById(R.id.countComment);
            textViewDivider = (TextView)v.findViewById(R.id.textViewDivider);
            imageProfileOwner = (ImageView)v.findViewById(R.id.imageProfileOwner);
            imagePost = (ImageView)v.findViewById(R.id.imagePost);
            btnLike = (ImageButton)v.findViewById(R.id.btnLike);
            btnComment = (ImageButton)v.findViewById(R.id.btnComment);
            layoutComment = (LinearLayout)v.findViewById(R.id.layoutComment);
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
    public MediaInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_media, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MediaInfo mediaInfo = listMedia.get(position);
        this.position = position;
        if(mapProfile.containsKey(position)) {
            holder.imageProfileOwner.setImageBitmap(ImageProccessor.getRoundedCornersImage(mapProfile.get(position), 50));
        }
        else {
            imageService.setImageByLinkWithRounded(holder.imageProfileOwner, mediaInfo.getOwnerProfilePicture(), adapterProfile, position);
        }
        if(mapPost.containsKey(position)){
            holder.imagePost.setImageBitmap(mapPost.get(position));
        }
        else {
            imageService.setImageByLink(holder.imagePost, mediaInfo.getLowImage(), adapterPost, position);
        }
        holder.nameOwner.setText(mediaInfo.getOwnerName());
        int countLike = mediaInfo.getCountLike();
        int countComments = mediaInfo.getCountComments();
        if(countLike > 0){
            holder.countLikes.setText(Integer.toString(countLike));
            holder.countLikes.setVisibility(View.VISIBLE);
        }
        else {
            holder.countLikes.setVisibility(View.GONE);
        }
        holder.layoutComment.removeAllViewsInLayout();
        if(countComments > 0){
            holder.countComment.setText(Integer.toString(countComments));
            holder.countComment.setVisibility(View.VISIBLE);
            List<Comment> listComment = mediaInfo.getListComment();
            holder.textViewDivider.setVisibility(View.VISIBLE);
            holder.layoutComment.setVisibility(View.VISIBLE);
            showLastsComments(listComment, holder.layoutComment);
        }
        else {
            holder.layoutComment.setVisibility(View.GONE);
            holder.countComment.setVisibility(View.GONE);
            holder.textViewDivider.setVisibility(View.GONE);
        }
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
        holder.imagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(activity, DetailPostActivity.class);
                detailIntent.putExtra("mediaInfo", mediaInfo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mapPost.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                detailIntent.putExtra("image", byteArray);
                detailIntent.putExtra("accessToken", accessToken);
                activity.startActivity(detailIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listMedia.size();
    }
    public List<MediaInfo> getListMedia(){
        return listMedia;
    }
    public void addItems(MediaInfo list){
        listMedia.add(list);
    }
    public boolean showingLastItem(){
        return position == listMedia.size() - 1;
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
    private void showLastsComments(List<Comment> listComment, ViewGroup layoutComments){
        for(int i = 0; i < ((listComment.size() >= 3)?(3):(listComment.size())); i++){
            TextView username = new TextView(activity);
            username.setPadding(5, 5, 5, 5);
            username.setText(listComment.get(i).getUsername());
            username.setTextSize(14);
            username.setTypeface(Typeface.DEFAULT_BOLD);
            TextView message = new TextView(activity);
            message.setPadding(5, 5, 5, 5);
            message.setText(listComment.get(i).getText());
            message.setTextSize(12);
            TextView divider = new TextView(activity);
            divider.setHeight(2);
            divider.setBackgroundColor(activity.getResources().getColor(R.color.divider));
            layoutComments.addView(username);
            layoutComments.addView(message);
            layoutComments.addView(divider);
        }
    }
}
