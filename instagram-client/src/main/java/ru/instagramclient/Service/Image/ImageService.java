package ru.instagramclient.Service.Image;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import ru.instagramclient.R;
import ru.instagramclient.View.BitmapAdapter;

/**
 * Created by Admin on 12.03.15.
 */
public class ImageService {
    private ImageLoader imageLoader = null;
    private Context context;

    public ImageService(Context context){
        if(imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.context = context;
    }
    public void setImageByLink(ImageView imageView, String link, final BitmapAdapter bitmapAdapter, final int position){
        imageLoader.displayImage(link, imageView, getOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(bitmapAdapter != null){
                    bitmapAdapter.cache(position, bitmap);
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }
    public void setImageByLinkWithRounded(ImageView imageView, String link, final BitmapAdapter bitmapAdapter, final int position){
        imageLoader.displayImage(link, imageView, getOptionsRounded(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(bitmapAdapter != null){
                    bitmapAdapter.cache(position, bitmap);
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }
    private DisplayImageOptions getOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageOnLoading(context.getResources().getDrawable(R.drawable.bg_image_post))
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        return options;
    }
    private DisplayImageOptions getOptionsRounded(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageOnLoading(context.getResources().getDrawable(R.drawable.bg_image_profile))
                .displayer(new RoundedBitmapDisplayer(50))
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        return options;
    }
}
