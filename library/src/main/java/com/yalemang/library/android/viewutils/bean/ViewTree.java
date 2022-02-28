package com.yalemang.library.android.viewutils.bean;

import android.view.View;

import java.util.Arrays;

public class ViewTree {
    private int level;
    private View view;
    private int position;
    private ViewTree parent;
    private ViewTree[] children;

    @Override
    public String toString() {
        return "ViewTree{" +
                "level=" + level +
                ", view=" + view +
                ", position=" + position +
                ", parent=" + parent +
                ", children=" + Arrays.toString(children) +
                '}';
    }

    public ViewTree(){}

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ViewTree[] getChildren() {
        return children;
    }

    public void setChildren(ViewTree[] children) {
        this.children = children;
    }

    public int getPosition() {
        return position;
    }

    public ViewTree getParent() {
        return parent;
    }

    public void setParent(ViewTree parent) {
        this.parent = parent;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
