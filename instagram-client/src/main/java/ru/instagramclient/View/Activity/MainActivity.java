package ru.instagramclient.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import ru.instagramclient.API.WebAPI;
import ru.instagramclient.API.WebAPIImpl;
import ru.instagramclient.Json.JsonCallback;
import ru.instagramclient.Json.MediaInfoJson;
import ru.instagramclient.Model.MediaInfo;
import ru.instagramclient.Model.User;
import ru.instagramclient.Service.Preferences.MyPreferencesImpl;
import ru.instagramclient.R;
import ru.instagramclient.Service.Image.ImageService;
import ru.instagramclient.View.Adapter.MediaInfoAdapter;
import ru.instagramclient.View.Logout;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    private User user;
    private static final boolean PROGRESS_SHOW = true;
    private static final boolean PROGRESS_NOT_TO_SHOW = false;
    private String accessToken;
    private ImageService imageService;
    private WebAPI webAPI;
    private MediaInfoJson mediaInfoJson;
    private List<MediaInfo> mediaInfoList;
    private SwipeRefreshLayout swipe;
    private JSONObject json = null;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MediaInfoAdapter adapter;
    private MyPreferencesImpl myPreferencesImpl;
    private int count = 5;
    private int countDownloads = 0;
    private static boolean IS_EMPTY_RESULT = false;
    private String maxId;
    private boolean userScrolled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        imageService = new ImageService(this);
        mediaInfoJson = new MediaInfoJson();
        webAPI = new WebAPIImpl(this);
        myPreferencesImpl = new MyPreferencesImpl(this);
//        user = getIntent().getParcelableExtra("user");
        TextView username = (TextView)findViewById(R.id.username);
        TextView fullName = (TextView)findViewById(R.id.fullName);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        ImageView imageProfile = (ImageView)findViewById(R.id.imageProfile);
        swipe = (SwipeRefreshLayout)findViewById(R.id.swipe);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        if(myPreferencesImpl.getAccessToken().equals("")){
            login();
        }
        else {
            user = myPreferencesImpl.getUserFromPref();
            accessToken = user.getAccessToken();
            username.setText(user.getUsername());
            fullName.setText(user.getFullName());
            imageService.setImageByLinkWithRounded(imageProfile, user.getImageProfile(), null, 0);
        }

        webAPI.getMySelfMedia(PROGRESS_NOT_TO_SHOW, accessToken, new JsonCallback() {
            @Override
            public void run(JSONObject jsonObject) {
                mediaInfoList = mediaInfoJson.createListFromJson(jsonObject);
                adapter = new MediaInfoAdapter(mediaInfoList, MainActivity.this, accessToken);
                recyclerView.setAdapter(adapter);
                countDownloads++;
                try {
                    maxId = jsonObject.getJSONObject("pagination").getString("next_max_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, count, maxId);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    userScrolled = true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    userScrolled = true;
                }
                else {
                    userScrolled = false;
                }
                return false;
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(userScrolled) {
                    if (newState == 0 && !IS_EMPTY_RESULT && adapter.showingLastItem()) {
                        webAPI.getMySelfMedia(PROGRESS_SHOW, accessToken, new JsonCallback() {
                            @Override
                            public void run(JSONObject jsonObject) {
                                addItemsInListFromJson(jsonObject);
                                countDownloads++;
                            }
                        }, count, maxId);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webAPI.getMySelfMedia(PROGRESS_NOT_TO_SHOW, accessToken, new JsonCallback() {
                    @Override
                    public void run(JSONObject jsonObject) {
                        mediaInfoList = mediaInfoJson.createListFromJson(jsonObject);
                        adapter = new MediaInfoAdapter(mediaInfoList, MainActivity.this, accessToken);
                        recyclerView.setAdapter(adapter);
                        countDownloads++;
                        try {
                            maxId = jsonObject.getJSONObject("pagination").getString("next_max_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipe.setRefreshing(false);
                    }
                }, count * countDownloads, "");
            }
        });

    }
    private void login(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void addItemsInListFromJson(JSONObject jsonObject){
        try {
            Log.e("length", "" + jsonObject.getJSONArray("data").length());
            if (jsonObject.getJSONArray("data").length() != 0) {
                mediaInfoList = mediaInfoJson.createListFromJson(jsonObject);
                for (MediaInfo media : mediaInfoList) {
                    adapter.addItems(media);
                }
                adapter.notifyDataSetChanged();
                Log.e("maxId", "before = " + maxId);
                maxId = jsonObject.getJSONObject("pagination").getString("next_max_id");
            } else {
                IS_EMPTY_RESULT = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onClickLogout(View v){
        webAPI.logout(accessToken, new Logout() {
            @Override
            public void success() {
                if(myPreferencesImpl.clearPrefs()){
                    login();
                }
            }
            @Override
            public void failed(String error) {

            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
