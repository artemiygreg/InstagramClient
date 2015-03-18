package ru.instagramclient.View.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import ru.instagramclient.API.WebAPI;
import ru.instagramclient.API.WebAPIImpl;
import ru.instagramclient.Json.JsonCallback;
import ru.instagramclient.Json.MediaInfoJson;
import ru.instagramclient.Model.Comments;
import ru.instagramclient.Model.MediaInfo;
import ru.instagramclient.Model.User;
import ru.instagramclient.MyPrefs;
import ru.instagramclient.R;
import ru.instagramclient.View.Adapter.CommentAdapter;
import ru.instagramclient.View.AlertDialog.MyAlertDialog;
import ru.instagramclient.View.AlertDialog.MyAlertDialogImpl;

import java.util.List;

public class DetailPostActivity extends ActionBarActivity {
    private MyPrefs myPrefs;
    private User user;
    private String accessToken;
    private MediaInfo mediaInfo;
    private WebAPI webAPI;
    private MyAlertDialog myAlertDialog;
    private EditText editTextComment;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<Comments> listComments;
    private MediaInfoJson mediaInfoJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaInfoJson = new MediaInfoJson();
        myPrefs = new MyPrefs(this);
        user = myPrefs.getUserFromPref();
        setContentView(R.layout.detail_post_activity);
        editTextComment = (EditText)findViewById(R.id.editText);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        webAPI = new WebAPIImpl(this);
        myAlertDialog = new MyAlertDialogImpl(this);
        mediaInfo = getIntent().getParcelableExtra("mediaInfo");
        byte[] image = getIntent().getByteArrayExtra("image");
        accessToken = getIntent().getStringExtra("accessToken");
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, new BitmapFactory.Options());

        webAPI.getComments(Long.toString(mediaInfo.getIdPost()), accessToken, new JsonCallback() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    listComments = mediaInfoJson.createListCommentsFromJson(jsonObject.getJSONArray("data"));
                    Log.e("size", "" + listComments.size());
                    adapter = new CommentAdapter(listComments, accessToken, mediaInfo, DetailPostActivity.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void onClickSendComment(View view){
        String text = editTextComment.getText().toString();
        if(text.equals("")){
            myAlertDialog.showAlertInfo(getResources().getString(R.string.inputText));
        }
        else {
            Comments comments = new Comments();
            comments.setText(text);
            comments.setFullName(user.getFullName());
            comments.setUsername(user.getUsername());
            comments.setProfilePicture(user.getImageProfile());
            adapter.addItems(comments);
            adapter.notifyDataSetChanged();
            editTextComment.setText("");
            webAPI.sendComment(Long.toString(mediaInfo.getIdPost()), text, accessToken);
        }
    }
}
