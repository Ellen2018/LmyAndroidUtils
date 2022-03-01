package com.yalemang.library.android.viewutils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.yalemang.library.android.viewutils.bean.ViewTree;

/**
 * 视图工具类
 */
public class ViewUtils {

    public static int TARGET_VIEW_DEPTH = 4;

    public static void viewTree(View view) {
        Activity activity = (Activity) view.getContext();
        FindViewTree findViewTree = getActivityViewTree(activity,view);
        ViewTree showViewTree = findViewTree.getTargetViewTree().getParent();
        ViewTree targetViewTree = findViewTree.getTargetViewTree();
        ViewTreeDialog viewTreeDialog = new ViewTreeDialog(activity, showViewTree, targetViewTree);
        viewTreeDialog.show();
    }

    public static ViewTree viewTree(Activity activity) {
        FindViewTree findViewTree = getActivityViewTree(activity,null);
        //跳转到视图显示界面
        ViewTreeDialog viewTreeDialog = new ViewTreeDialog(activity, findViewTree.getRootViewTree(), findViewTree.getRootViewTree());
        viewTreeDialog.show();
        return findViewTree.getRootViewTree();
    }

    private static FindViewTree getActivityViewTree(Activity activity,View targetView) {
        FindViewTree findViewTree = new FindViewTree();
        findViewTree.setTargetView(targetView);
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
        findViewTree.setRootViewTree(rootViewTree);
        if(findViewTree.getTargetView() == null){
            findViewTree.setTargetView(view);
        }
        rootViewTree.setChildren(viewTrees);
        viewGroupTree(rootViewTree,findViewTree);
        return findViewTree;
    }

    private static void viewGroupTree(ViewTree viewTree,FindViewTree findViewTree) {
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
            if(findViewTree.getTargetView().equals(view)){
                findViewTree.setTargetViewTree(childViewTree);
            }
            if (view instanceof ViewGroup) {
                ViewGroup childViewGroup = (ViewGroup) view;
                ViewTree[] viewTrees = new ViewTree[childViewGroup.getChildCount()];
                childViewTree.setChildren(viewTrees);
                viewGroupTree(childViewTree,findViewTree);
            }
        }
    }

    private static class FindViewTree{
        private ViewTree rootViewTree;
        private ViewTree targetViewTree;
        private View targetView;

        public View getTargetView() {
            return targetView;
        }

        public void setTargetView(View targetView) {
            this.targetView = targetView;
        }

        public ViewTree getRootViewTree() {
            return rootViewTree;
        }

        public void setRootViewTree(ViewTree rootViewTree) {
            this.rootViewTree = rootViewTree;
        }

        public ViewTree getTargetViewTree() {
            return targetViewTree;
        }

        public void setTargetViewTree(ViewTree targetViewTree) {
            this.targetViewTree = targetViewTree;
        }
    }

}
