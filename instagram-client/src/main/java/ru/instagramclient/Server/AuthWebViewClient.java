package ru.instagramclient.Server;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ru.instagramclient.View.Logout;

/**
 * Created by Admin on 12.03.15.
 */
public class AuthWebViewClient extends WebViewClient {
    private GetToken getToken;
    private Logout logout;

    public AuthWebViewClient(GetToken getToken){
        super();
        this.getToken = getToken;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url){
        Log.e("url", url);
        if(url.startsWith(Server.REDIRECT_URL)){
            String parts [] = url.split("=");
            getToken.get(parts[1]);
            return true;
        }
        return false;
    }
    /*@Override
    public void onPageFinished(WebView webView, String url){
        Log.e("url", url);
        if(url.equals(WebAPIImpl.LOGOUT)){
            logout.success();
        }
    }*/
    public void setLogout(Logout logout){
        this.logout = logout;
    }
}
