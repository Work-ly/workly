/*
 * file: src/main/java/com/workly/model/Team.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 20 September, 2022
 */
package com.workly.model;

import com.google.gson.annotations.SerializedName;

public class Team {
  /* id */
  @SerializedName("id")
  private int id;
  /* name */
  @SerializedName("name")
  private String name;
  /* description */
  @SerializedName("description")
  private String description;
  /* pfp */
  @SerializedName("pfp")
  private Image pfp;
  /* header */
  @SerializedName("header")
  private Image header;

  public Team() {
  }

  public Team(int id, String name, String description, Image pfp, Image header) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.pfp = pfp;
    this.header = header;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
}