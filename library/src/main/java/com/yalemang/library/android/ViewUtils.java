package com.yalemang.library.android;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.yalemang.library.android.bean.ViewMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 视图工具类
 */
public class ViewUtils {

    /**
     * 获取视图层级
     *
     * @param view
     * @return
     */
    public static ViewMessage getViewHierarchy(View view) {
        ViewMessage viewMessage = new ViewMessage();
        int hierarchy = 1;
        if (view.getParent() != null) {
            View targetView = view;
            ViewParent viewParent = targetView.getParent();
            while (viewParent != null) {
                hierarchy++;
                viewParent = viewParent.getParent();
            }
        }
        viewMessage.setSystemViewHierarchy(hierarchy);
        viewMessage.setXmlViewHierarchy(hierarchy - 6);
        return viewMessage;
    }

    public static void viewTree(View view) {
        List<View> viewList = new ArrayList<>();
        ViewParent viewParent = view.getParent();
        viewList.add(view);
        while (viewParent != null) {
            View targetView = (View) viewParent;
            viewParent = viewParent.getParent();
            viewList.add(targetView);
        }

        for (int i = viewList.size() - 1; i >= 0; i--) {
            String line = "";
            for (int j = 0; j < viewList.size() - i; j++) {
                line = line + "--";
            }
            Log.d("Ellen2018",line + viewList.get(i).getClass().getName());
        }
    }

}
