package ru.instagramclient.View.AlertDialog;

import ru.instagramclient.Model.Like;

import java.util.List;

/**
 * Created by Admin on 01.03.15.
 */
public interface MyAlertDialog {

    public void showAlertInfo(String textInfo);
    public void showProgressDialog();
    public boolean progressIsShowing();
    public void dismissProgressDialog();
    public void showAlertAllLikes(List<Like> listLikes);
}
