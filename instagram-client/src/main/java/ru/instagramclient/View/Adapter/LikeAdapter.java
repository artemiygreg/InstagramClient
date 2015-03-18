package ru.instagramclient.View.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ru.instagramclient.Model.Likes;
import ru.instagramclient.R;
import ru.instagramclient.Service.Image.ImageService;
import ru.instagramclient.View.BitmapAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 17.03.15.
 */
public class LikeAdapter extends ArrayAdapter<Likes> {
    private List<Likes> listLike;
    private ViewHolder holder;
    private Activity activity;
    private ImageService imageService;
    private Map<Integer, Bitmap> mapProfile = new HashMap<>();
    private BitmapAdapter adapterProfile = new BitmapAdapter() {
        @Override
        public void cache(Integer key, Bitmap value) {
            mapProfile.put(key, value);
        }
    };

    public LikeAdapter(List<Likes> listLike, Activity activity, int resId) {
        super(activity, resId, listLike);
        this.listLike = listLike;
        this.activity = activity;
        imageService = new ImageService(activity);
    }

    static class ViewHolder {
        public View view;
        public TextView username;
        public ImageView imageProfile;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.list_item_like, null);
            holder.username = (TextView) view.findViewById(R.id.username);
            holder.imageProfile = (ImageView) view.findViewById(R.id.imageProfile);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        Likes likes = listLike.get(position);
//        holder.username.setText(likes.getUsername());
        if(mapProfile.containsKey(position)) {
            holder.imageProfile.setImageBitmap(mapProfile.get(position));
        }
        else {
            imageService.setImageByLink(holder.imageProfile, likes.getProfilePicture(), adapterProfile, position);
        }

        return view;
    }
}
