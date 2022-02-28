package com.yalemang.library.android.viewutils.bean;

public class ViewMessage {
    //视图布局中层级
    private int xmlViewHierarchy;
    //系统视图层级
    private int systemViewHierarchy;

    public int getXmlViewHierarchy() {
        return xmlViewHierarchy;
    }

    public void setXmlViewHierarchy(int xmlViewHierarchy) {
        this.xmlViewHierarchy = xmlViewHierarchy;
    }

    public int getSystemViewHierarchy() {
        return systemViewHierarchy;
    }

    public void setSystemViewHierarchy(int systemViewHierarchy) {
        this.systemViewHierarchy = systemViewHierarchy;
    }
}
