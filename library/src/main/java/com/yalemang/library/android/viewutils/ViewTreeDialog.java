package com.yalemang.library.android.viewutils;

import android.content.Context;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalemang.library.R;

public class ViewTreeDialog {

    private BottomSheetDialog bottomSheetDialog;
    private View contentView;

    public void show(Context context){
        bottomSheetDialog = new BottomSheetDialog(context);
        contentView = View.inflate(context, R.layout.dialog_view_tree,null);
        bottomSheetDialog.setContentView(contentView);
        bottomSheetDialog.show();
    }

}
