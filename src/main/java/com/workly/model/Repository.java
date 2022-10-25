/*
 * file: src/main/java/com/workly/model/Repository.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 20 September, 2022
 */
package com.workly.model;

import com.google.gson.annotations.SerializedName;

public class Repository {
    /* id */
    @SerializedName("id")
    private int id;
    /* project */
    @SerializedName("project")
    private Project project;
    /* upstream */
    @SerializedName("upstream")
    private String upstream;

    public Repository() {
    }

    public Repository(int id, Project project, String upstream) {
        this.id = id;
        this.project = project;
        this.upstream = upstream;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getUpstream() {
        return upstream;
    }

    public void setUpstream(String upstream) {
        this.upstream = upstream;
    }
}