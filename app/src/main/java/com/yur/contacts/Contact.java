package com.yur.contacts;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yur on 07.12.2016.
 */

public class Contact extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private Boolean isFavourite;

    public Contact( String name, Boolean isFavourite) {
        this.isFavourite = isFavourite;
        this.name = name;
        this.id= UUID.randomUUID().toString();
    }

    public Contact() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }
}
