/*
 * file: src/main/java/com/workly/model/Repository.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 20 September, 2022
 */
package com.workly.model;

import com.google.gson.annotations.SerializedName;

public class Repository {
    @SerializedName("id")
    private int id;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("upstream")
    private String upstream;

    public Repository() {
    }

    public Repository(int id, String uuid, String upstream) {
        this.id = id;
        this.uuid = uuid;
        this.upstream = upstream;
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

    public String getUpstream() {
        return upstream;
    }

    public void setUpstream(String upstream) {
        this.upstream = upstream;
    }
}
