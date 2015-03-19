package ru.instagramclient.Service.Preferences;

import ru.instagramclient.Model.User;

/**
 * Created by Admin on 19.03.15.
 */
public interface MyPreferences {
    public Long getId();
    public void setId(Long id);
    public String getAccessToken();
    public void setAccessToken(String accessToken);
    public String getUsername();
    public void setUsername(String username);
    public String getFullName();
    public void setFullName(String fullName);
    public String getImageProfile();
    public void setImageProfile(String imageProfile);
    public Object getObjectFromPrefs(String key);
    public User getUserFromPref();
    public void savePrefFromUser(User user);
    public boolean clearPrefs();
}
