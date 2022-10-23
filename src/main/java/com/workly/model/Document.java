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
  @SerializedName("title")
  private String title;
  @SerializedName("content")
  private String content;
  @SerializedName("user")
  private User user;
  @SerializedName("project")
  private Project project;

  public Document() {
  }

  public Document(int id, String title, String content, User user, Project project) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.user = user;
    this.project = project;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }
}