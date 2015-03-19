package ru.instagramclient.Service.Image;

import android.graphics.*;

/**
 * Created by Artem on 30.09.2014.
 */
public class ImageProccessor {
    private ImageProccessor() {

    }

    public static Bitmap getRoundedCornersImage(Bitmap source, int radiusPixels) {

        if (source == null) {
            return null;
        }
        final int sourceWidth = source.getWidth();
        final int sourceHeight = source.getHeight();
        final Bitmap output = Bitmap.createBitmap(sourceWidth, sourceHeight, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setColor(0xFF000000);

        //final Rect rect = ;
        final RectF rectF = new RectF(new Rect(0, 0, sourceWidth, sourceHeight));

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, radiusPixels, radiusPixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);

        return output;
    }
}
