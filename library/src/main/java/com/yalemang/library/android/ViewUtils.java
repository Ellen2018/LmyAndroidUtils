package com.yalemang.library.android;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.yalemang.library.android.bean.ViewMessage;
import com.yalemang.library.android.bean.ViewTree;

import java.util.ArrayList;
import java.util.HashMap;
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
        if (view instanceof ViewGroup) {
            HashMap<Integer, List<ViewTree>> viewHashMap = viewGroupTreeMessage(0, 0,view);
            for (int i = 0; i < viewHashMap.size(); i++) {
                List<ViewTree> viewList = viewHashMap.get(i);
                for (ViewTree viewTree : viewList) {
                    Log.d("Ellen2018", "父亲级别:" + viewTree.getParentLevel());
                    Log.d("Ellen2018", "父亲:" + viewTree.getParentView().getClass().getName());
                    Log.d("Ellen2018", "父亲在上级中的位置:" + viewTree.getParentPosition());
                    Log.d("Ellen2018", "当前视图:"+viewTree.getView().getClass().getName());
                    Log.d("Ellen2018", "当前视图级别:"+viewTree.getLevel());
                    Log.d("Ellen2018", "当前视图在父视图中的位置:"+viewTree.getPosition());
                    Log.d("Ellen2018", "--------------------------------------------------");
                }
            }
        } else {
            viewTreeMessage(view);
        }
    }

    private static void viewTreeMessage(View view) {
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
            Log.d("Ellen2018", line + viewList.get(i).getClass().getName());
        }
    }

    /**
     * 递归实现
     *
     * @param level
     * @param view
     */
    private static HashMap<Integer, List<ViewTree>> viewGroupTreeMessage(int level, int parentPosition,View view) {
        HashMap<Integer, List<ViewTree>> viewHashMap = new HashMap<>();
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        if (viewHashMap.get(level) == null) {
            viewHashMap.put(level, new ArrayList<>());
        }
        for (int i = 0; i < childCount; i++) {
            View currentView = viewGroup.getChildAt(i);
            if (currentView instanceof ViewGroup) {
                //ViewGroup
                int otherLevel = level + 1;
                ViewTree viewTree = new ViewTree();
                viewTree.setLevel(otherLevel);
                viewTree.setParentLevel(level);
                viewTree.setParentView(view);
                viewTree.setView(currentView);
                viewTree.setParentPosition(parentPosition);
                viewTree.setPosition(i);
                viewHashMap.get(level).add(viewTree);
                HashMap<Integer, List<ViewTree>> hashMap = viewGroupTreeMessage(otherLevel,i, currentView);
                for (int j : hashMap.keySet()) {
                    if (viewHashMap.containsKey(j)) {
                        viewHashMap.get(j).addAll(hashMap.get(j));
                    } else {
                        viewHashMap.put(j, hashMap.get(j));
                    }
                }
            } else {
                //非ViewGroup
                ViewTree viewTree = new ViewTree();
                viewTree.setLevel(level + 1);
                viewTree.setParentLevel(level);
                viewTree.setParentView(view);
                viewTree.setPosition(i);
                viewTree.setParentPosition(parentPosition);
                viewTree.setView(currentView);
                viewHashMap.get(level).add(viewTree);
            }
        }
        return viewHashMap;
    }

    public static void activityViewTree(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        while (view.getParent() != null) {
            view = (View) view.getParent();
        }
        viewTree(view);
    }

}
