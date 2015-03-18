package ru.instagramclient.API;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import ru.instagramclient.Json.JsonCallback;
import ru.instagramclient.Model.User;
import ru.instagramclient.MyPrefs;
import ru.instagramclient.Server.AuthWebViewClient;
import ru.instagramclient.Server.GetToken;
import ru.instagramclient.Server.Server;
import ru.instagramclient.View.Activity.LoginActivity;
import ru.instagramclient.View.Activity.MainActivity;
import ru.instagramclient.View.AlertDialog.MyAlertDialog;
import ru.instagramclient.View.AlertDialog.MyAlertDialogImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 10.03.15.
 */
public class WebAPIImpl implements WebAPI {
    private static WebAPI instance = null;
    private MyAlertDialog myAlert;
    private Context context;
    private MyPrefs myPrefs;
    private RequestQueue requestQueue;
    private static final int POST = Request.Method.POST;
    private static final int GET = Request.Method.GET;
    private static final int DELETE = Request.Method.DELETE;
    private static final String AUTH = "/oauth/authorize/?client_id=" + Server.CLIENT_ID + "&redirect_uri=" + Server.REDIRECT_URL + "&response_type=code&scope=likes+comments";
    private static final String GET_ACCESS_TOKEN = "/oauth/access_token";
    private static final String GET_MY_SELF_MEDIA = "/users/self/feed?";
    private static final String MEDIA = "/media/";
    private static final String LIKES = "/likes";
    private static final String COMMENTS = "/comments";
    private static final String LOGOUT = Server.PROTOCOL + Server.HOST + "/account/logout/";

    public WebAPIImpl(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        myPrefs = new MyPrefs(context);
        myAlert = new MyAlertDialogImpl(context);
        instance = this;
    }
    public static WebAPI getInstance() {
            return instance;
    }

    @Override
    public WebView authFirstStep() {
        String url = Server.PROTOCOL + Server.API_HOST + AUTH;

        WebView webView = new WebView(context);
        webView.setWebViewClient(new AuthWebViewClient(new GetToken() {
            @Override
            public void get(String code) {
                getAuthToken(code);
            }
        }));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        return webView;
    }

    @Override
    public void getMySelfMedia(boolean showProgressDialog, String accessToken, final JsonCallback jsonCallback, int count, String maxId) {
        if(showProgressDialog) {
            myAlert.showProgressDialog();
        }
        String url = Server.PROTOCOL + Server.API_VERSION + GET_MY_SELF_MEDIA + Vars.ACCESS_TOKEN + "=" + accessToken + "&" +Vars.COUNT+"=" + count + "&"+Vars.MAX_ID+"=" + maxId;
        JsonObjectRequest request = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                myAlert.dismissProgressDialog();
                Log.e("response", jsonObject.toString());
                jsonCallback.run(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                myAlert.dismissProgressDialog();
            }
        });
        requestQueue.add(request);
    }

    private void getAuthToken(final String code) {
        String url = Server.PROTOCOL + Server.API_HOST + GET_ACCESS_TOKEN;
        StringRequest request = new StringRequest(POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.e("getAuthToken", "onResponse result - " + result);
                User user = new User();
                try {
                    JSONObject json = new JSONObject(result);
                    user.setId(json.getJSONObject(Vars.USER).getLong(Vars.ID));
                    user.setAccessToken(json.getString(Vars.ACCESS_TOKEN));
                    user.setUsername(json.getJSONObject(Vars.USER).getString(Vars.USERNAME));
                    user.setFullName(json.getJSONObject(Vars.USER).getString(Vars.FULL_NAME));
                    user.setImageProfile(json.getJSONObject(Vars.USER).getString(Vars.PROFILE_PICTURE));

                    myPrefs.savePrefFromUser(user);

                    Intent main = new Intent(context, MainActivity.class);
                    context.startActivity(main);
                    ((LoginActivity)context).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("onErrorResponse", volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put(Vars.CLIENT_ID, Server.CLIENT_ID);
                map.put(Vars.CLIENT_SECRET, Server.CLIENT_SECRET);
                map.put(Vars.GRANT_TYPE, Vars.AUTHORIZATION_CODE);
                map.put(Vars.REDIRECT_URI, Server.REDIRECT_URL);
                map.put(Vars.CODE, code);

                return map;
            }
        };
        requestQueue.add(request);
    }
    @Override
    public void logout(final String accessToken, final JsonCallback jsonCallback) {
        myAlert.showProgressDialog();
        StringRequest request = new  StringRequest(POST, LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("response", s);
                myAlert.dismissProgressDialog();
                /*try {
                    jsonCallback.run(new JSONObject(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error", volleyError.getMessage());
                myAlert.dismissProgressDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put(Vars.ACCESS_TOKEN, accessToken);

                return map;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void setLike(String mediaId, final String accessToken) {
        myAlert.showProgressDialog();
        String url = Server.PROTOCOL + Server.API_VERSION + MEDIA + mediaId + LIKES;
        StringRequest request = new StringRequest(POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("response", s);
                myAlert.dismissProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error", volleyError.getMessage());
                myAlert.dismissProgressDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put(Vars.ACCESS_TOKEN, accessToken);

                return map;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void getLikes(String mediaId, String accessToken, final JsonCallback jsonCallback) {
        myAlert.showProgressDialog();
        String url = Server.PROTOCOL + Server.API_VERSION + MEDIA + mediaId + LIKES + "?" + Vars.ACCESS_TOKEN + "=" + accessToken;
        Log.e("url", url);
        JsonObjectRequest request = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("response", jsonObject.toString());
                myAlert.dismissProgressDialog();
                jsonCallback.run(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error", volleyError.getMessage());
                myAlert.dismissProgressDialog();
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void removeLike(String mediaId, String accessToken) {
        myAlert.showProgressDialog();
        String url = Server.PROTOCOL + Server.API_VERSION + MEDIA + mediaId + LIKES +"?"+Vars.ACCESS_TOKEN+"="+accessToken;
        JsonObjectRequest request = new JsonObjectRequest(DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("response", jsonObject.toString());
                myAlert.dismissProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(request);
    }

    @Override
    public void getComments(String mediaId, String accessToken, final JsonCallback jsonCallback) {
        myAlert.showProgressDialog();
        String url = Server.PROTOCOL + Server.API_VERSION + MEDIA + mediaId + COMMENTS + "?" + Vars.ACCESS_TOKEN + "=" + accessToken;
        Log.e("url", url);
        JsonObjectRequest request = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("response", jsonObject.toString());
                myAlert.dismissProgressDialog();
                jsonCallback.run(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error", volleyError.getMessage());
                myAlert.dismissProgressDialog();
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void sendComment(String mediaId, final String text, final String accessToken) {
        myAlert.showProgressDialog();
        String url = Server.PROTOCOL + Server.API_VERSION + MEDIA + mediaId + COMMENTS;
        StringRequest request = new StringRequest(POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("response", s);
                myAlert.dismissProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error", volleyError.getMessage());
                myAlert.dismissProgressDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put(Vars.TEXT, text);
                map.put(Vars.ACCESS_TOKEN, accessToken);

                return map;
            }
        };
        requestQueue.add(request);
    }
}
