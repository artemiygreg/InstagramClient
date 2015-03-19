package ru.instagramclient.API;

import android.webkit.WebView;
import ru.instagramclient.Json.JsonCallback;
import ru.instagramclient.View.Logout;

/**
 * Created by Admin on 10.03.15.
 */
public interface WebAPI {

    public WebView authFirstStep();
    public void getMySelfMedia(boolean showProgressDialog, String accessToken, JsonCallback jsonCallback, int count, String minId);
    public void logout(String accessToken, Logout logout);
    public void setLike(String mediaId, String accessToken);
    public void getLikes(String mediaId, String accessToken, JsonCallback jsonCallback);
    public void removeLike(String mediaId, String accessToken);
    public void getComments(String mediaId, String accessToken, JsonCallback jsonCallback);
    public void sendComment(String mediaId, String text, String accessToken);
}
