package ru.instagramclient.View.AlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import ru.instagramclient.Model.Like;
import ru.instagramclient.R;
import ru.instagramclient.View.Adapter.LikeAdapter;

import java.util.List;

/**
 * Created by Admin on 01.03.15.
 */
public class MyAlertDialogImpl implements MyAlertDialog {
    private Context context;
    private AlertDialog alertDialogProgress;

    public MyAlertDialogImpl(Context context){
        this.context = context;
    }

    @Override
    public void showAlertInfo(String textInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(textInfo);
        builder.setNegativeButton(context.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void showProgressDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.layout_alert_progress, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        alertDialogProgress = builder.create();
        alertDialogProgress.show();
        alertDialogProgress.setContentView(v);
    }

    @Override
    public void dismissProgressDialog() {
        if(alertDialogProgress != null) {
            if (alertDialogProgress.isShowing()) {
                alertDialogProgress.dismiss();
            }
        }
    }

    @Override
    public void showAlertAllLikes(List<Like> listLikes) {
        LikeAdapter adapter = new LikeAdapter(listLikes,  (Activity)context, R.layout.list_item_like);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.layout_show_all_like, null);
        final GridView gridView = (GridView)v.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        builder.setView(v);
        alertDialogProgress = builder.create();
        alertDialogProgress.show();
    }

    @Override
    public boolean progressIsShowing(){
        if(alertDialogProgress != null){
            return alertDialogProgress.isShowing();
        }
        else {
            return false;
        }
    }
}
