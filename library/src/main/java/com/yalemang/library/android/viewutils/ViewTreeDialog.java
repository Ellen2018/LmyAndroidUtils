package com.yalemang.library.android.viewutils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalemang.library.R;

public class ViewTreeDialog {

    private BottomSheetDialog bottomSheetDialog;
    private View contentView;

    public void show(Context context){
        bottomSheetDialog = new BottomSheetDialog(context);
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_view_tree,null,false);
        bottomSheetDialog.setContentView(contentView);
        bottomSheetDialog.show();
    }

}
