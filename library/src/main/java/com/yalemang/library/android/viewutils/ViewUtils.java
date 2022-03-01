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
        rootViewTree.setParent(null);
        ViewTree[] viewTrees = new ViewTree[1];
        rootViewTree.setChildren(viewTrees);
        viewGroupTree(rootViewTree);

        //跳转到视图显示界面
        ViewTreeDialog viewTreeDialog = new ViewTreeDialog(activity,rootViewTree);
        viewTreeDialog.show();
    }

    private static void viewGroupTree(ViewTree viewTree) {
        ViewGroup viewGroup = (ViewGroup) viewTree.getView();
        for (int i = 0; i < viewTree.getChildren().length; i++) {
            View view = viewGroup.getChildAt(i);
            if(viewTree.getLevel() == 0){
                if(!(view instanceof ViewGroup)){
                    break;
                }
            }
            ViewTree childViewTree = new ViewTree();
            childViewTree.setPosition(i);
            childViewTree.setView(view);
            childViewTree.setParent(viewTree);
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
