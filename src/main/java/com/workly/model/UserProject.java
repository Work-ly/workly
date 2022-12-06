package com.workly.model;

import com.google.gson.annotations.SerializedName;

public class UserProject {
  @SerializedName("id")
  private int id;
  @SerializedName("user")
  private User user;
  @SerializedName("project")
  private Project project;
  @SerializedName("role")
  private String role;

  public UserProject() {
  }

  public UserProject(int id, User user, Project project, String role) {
    this.id = id;
    this.user = user;
    this.project = project;
    this.role = role;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
