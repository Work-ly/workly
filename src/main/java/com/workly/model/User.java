/*
 * file: src/main/java/com/workly/model/User.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 13 August, 2022
 */

package com.workly.model;

import com.google.gson.annotations.SerializedName;

public class User {
  @SerializedName("id")
  private int id;
  @SerializedName("uuid")
  private String uuid;
  @SerializedName("name")
  private String name;
  @SerializedName("email")
  private String email;
  @SerializedName("description")
  private String description;
  @SerializedName("password")
  private String password;
  @SerializedName("pfp")
  private Image pfp;
  @SerializedName("header")
  private Image header;
  @SerializedName("returnSecureToken")
  private boolean returnSecureToken;

  public User() {
  }

  public User(int id, String uuid, String name, String email, String description, String password, Image pfp, Image header, boolean returnSecureToken) {
    this.id = id;
    this.uuid = uuid;
    this.name = name;
    this.email = email;
    this.description = description;
    this.password = password;
    this.pfp = pfp;
    this.header = header;
    this.returnSecureToken = returnSecureToken;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Image getPfp() {
    return pfp;
  }

  public void setPfp(Image pfp) {
    this.pfp = pfp;
  }

  public Image getHeader() {
    return header;
  }

  public void setHeader(Image header) {
    this.header = header;
  }

  public boolean isReturnSecureToken() {
    return returnSecureToken;
  }

  public void setReturnSecureToken(boolean returnSecureToken) {
    this.returnSecureToken = returnSecureToken;
  }
}
