package com.mumu.study.realm;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/16.
 */

public class Book extends RealmObject{
    private String name;
    private String author;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
