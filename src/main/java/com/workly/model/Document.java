/*
 * file: src/main/java/com/workly/model/Document.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 20 September, 2022
 */
package com.workly.model;

import com.google.gson.annotations.SerializedName;

public class Document {
    @SerializedName("id")
    private int id;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("link")
    private String link;

    public Document() {
    }

    public Document(int id, String uuid, String link) {
        this.id = id;
        this.uuid = uuid;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
}
