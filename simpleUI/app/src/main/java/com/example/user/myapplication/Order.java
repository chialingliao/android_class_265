package com.example.user.myapplication;

import io.realm.RealmObject;

/**
 * Created by user on 2016/4/25.
 */
public class Order extends RealmObject{
    private String note ;
    private String menuResult;
    private String storeInfo;


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMenuResult() {
        return menuResult;
    }

    public void setMenuResult(String menuResult) {
        this.menuResult = menuResult;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }
}
