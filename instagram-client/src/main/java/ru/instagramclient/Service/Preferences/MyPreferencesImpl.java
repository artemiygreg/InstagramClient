package ru.instagramclient.Service.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import ru.instagramclient.Model.User;

/**
 * Created by Admin on 12.03.15.
 */
public class MyPreferencesImpl implements MyPreferences {
    private Context context;
    private String accessToken;
    private Long id;
    private String username;
    private String fullName;
    private String imageProfile;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String FULL_NAME = "fullName";
    public static final String IMAGE_PROFILE = "imageProfile";

    public MyPreferencesImpl(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
    }
    @Override
    public Long getId() {
        return sharedPreferences.getLong(ID, 0);
    }
    @Override
    public void setId(Long id) {
        editor = sharedPreferences.edit();
        editor.putLong(ID, id);
        editor.apply();
    }
    @Override
    public String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN, "");
    }
    @Override
    public void setAccessToken(String accessToken) {
        editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.apply();
    }
    @Override
    public String getUsername() {
        return sharedPreferences.getString(USERNAME, "");
    }
    @Override
    public void setUsername(String username) {
        editor = sharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.apply();
    }
    @Override
    public String getFullName() {
        return sharedPreferences.getString(FULL_NAME, "");
    }
    @Override
    public void setFullName(String fullName) {
        editor = sharedPreferences.edit();
        editor.putString(IMAGE_PROFILE, fullName);
        editor.apply();
    }
    @Override
    public String getImageProfile() {
        return sharedPreferences.getString(IMAGE_PROFILE, "");
    }
    @Override
    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
        editor = sharedPreferences.edit();
        editor.putString(IMAGE_PROFILE, imageProfile);
        editor.apply();
    }
    @Override
    public Object getObjectFromPrefs(String key){
        if(key.equals(ID)) {
            return sharedPreferences.getLong(key, 0);
        }
        else {
            return sharedPreferences.getString(key, "");
        }
    }
    @Override
    public User getUserFromPref(){
        User user = new User();
        user.setUsername(getUsername());
        user.setFullName(getFullName());
        user.setImageProfile(getImageProfile());
        user.setAccessToken(getAccessToken());
        user.setId(getId());

        return user;
    }
    @Override
    public void savePrefFromUser(User user){
        editor = sharedPreferences.edit();
        editor.putString(IMAGE_PROFILE, user.getImageProfile());
        editor.putString(USERNAME, user.getUsername());
        editor.putString(FULL_NAME, user.getFullName());
        editor.putString(ACCESS_TOKEN, user.getAccessToken());
        editor.putLong(ID, user.getId());

        editor.apply();
    }
    @Override
    public boolean clearPrefs(){
        boolean cleared;
        try {
            editor = sharedPreferences.edit();
            editor.putString(IMAGE_PROFILE, "");
            editor.putString(USERNAME, "");
            editor.putString(FULL_NAME, "");
            editor.putString(ACCESS_TOKEN, "");
            editor.putLong(ID, 0);

            editor.apply();
            cleared = true;
        }
        catch (Exception e){
            cleared = false;
        }
        return cleared;
    }
}
