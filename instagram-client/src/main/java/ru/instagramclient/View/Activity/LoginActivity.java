package ru.instagramclient.View.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import ru.instagramclient.API.WebAPI;
import ru.instagramclient.API.WebAPIImpl;

public class LoginActivity extends ActionBarActivity {
    private EditText login, password;
    private Button btnLogin;
    private WebAPI webAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webAPI = new WebAPIImpl(this);
        setContentView(webAPI.authFirstStep());
    }
}
