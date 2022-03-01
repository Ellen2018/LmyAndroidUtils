package com.yalemang.library.android.viewutils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.yalemang.library.android.viewutils.bean.ViewTree;

/**
 * 视图工具类
 */
public class ViewUtils {

    public static void viewTree(View view) {
        Activity activity = (Activity) view.getContext();
        ViewTree activityViewTree = getActivityViewTree(activity);
        ViewTree targetViewTree = findViewTree(activityViewTree,view);
        ViewTreeDialog viewTreeDialog = new ViewTreeDialog(activity, targetViewTree.getParent(), targetViewTree);
        viewTreeDialog.show();
    }


    private static ViewTree findViewTree(ViewTree viewTree, View view) {
        ViewTree targetViewTree = null;
        if (viewTree.getView().equals(view)) {
            targetViewTree = viewTree;
        } else {
            if (viewTree.getChildren() != null) {
                for (ViewTree vt : viewTree.getChildren()) {
                    if (vt.getView().equals(view)) {
                        targetViewTree = vt;
                        break;
                    }
                }
                if (targetViewTree == null) {
                    for (ViewTree vt : viewTree.getChildren()) {
                        if(vt.getView() instanceof ViewGroup){
                            targetViewTree = findViewTree(vt,view);
                        }
                    }
                }
            }
        }
        return targetViewTree;
    }

    public static ViewTree activityViewTree(Activity activity) {
        ViewTree rootViewTree = getActivityViewTree(activity);
        //跳转到视图显示界面
        ViewTreeDialog viewTreeDialog = new ViewTreeDialog(activity, rootViewTree, rootViewTree);
        viewTreeDialog.show();
        return rootViewTree;
    }

    private static ViewTree getActivityViewTree(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        while (view.getParent() != null) {
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof View) {
                view = (View) viewParent;
            }
            if (!(view.getParent() instanceof View)) {
                break;
            }
        }
        ViewTree rootViewTree = new ViewTree();
        rootViewTree.setView(view);
        rootViewTree.setPosition(0);
        rootViewTree.setLevel(0);
        rootViewTree.setParent(null);
        ViewTree[] viewTrees = new ViewTree[1];
        rootViewTree.setChildren(viewTrees);
        viewGroupTree(rootViewTree);
        return rootViewTree;
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
