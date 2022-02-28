package com.yalemang.library.android.viewutils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.yalemang.library.android.viewutils.bean.ViewMessage;
import com.yalemang.library.android.viewutils.bean.ViewTree;

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

    public static void activityViewTree(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        while (view.getParent() != null) {
            ViewParent viewParent = view.getParent();
            if(viewParent instanceof View){
                view = (View) viewParent;
            }
            if(!(view.getParent() instanceof View)){
                break;
            }
        }
        ViewGroup rootViewGroup = (ViewGroup) view;
        ViewTree rootViewTree = new ViewTree();
        rootViewTree.setView(view);
        rootViewTree.setPosition(0);
        rootViewTree.setLevel(0);
        ViewTree[] viewTrees = new ViewTree[rootViewGroup.getChildCount()];
        rootViewTree.setChildren(viewTrees);
        viewGroupTree(rootViewTree);

        //跳转到视图显示界面
        ViewTreeDialog viewTreeDialog = new ViewTreeDialog();
        viewTreeDialog.show(activity);
    }

    private static void viewGroupTree(ViewTree viewTree) {
        ViewGroup viewGroup = (ViewGroup) viewTree.getView();
        for (int i = 0; i < viewTree.getChildren().length; i++) {
            View view = viewGroup.getChildAt(i);
            ViewTree childViewTree = new ViewTree();
            childViewTree.setPosition(i);
            childViewTree.setView(view);
            childViewTree.setLevel(viewTree.getLevel() + 1);
            viewTree.getChildren()[i] = childViewTree;
            if (view instanceof ViewGroup) {
                ViewGroup childViewGroup = (ViewGroup) view;
                ViewTree[] viewTrees = new ViewTree[childViewGroup.getChildCount()];
                childViewTree.setChildren(viewTrees);
                viewGroupTree(childViewTree);
            }
        }
    }

}
