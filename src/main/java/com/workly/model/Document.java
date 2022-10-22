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
    @SerializedName("project_id")
    private int projectId;
    @SerializedName("upstream")
    private String upstream;

    public Document() {
    }

    public Document(int id, int projectId, String upstream) {
        this.id = id;
        this.projectId = projectId;
        this.upstream = upstream;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getUpstream() {
        return upstream;
    }

    public void setUpstream(String upstream) {
        this.upstream = upstream;
    }
}