/*
 * file: src/main/java/com/workly/model/Image.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 18 September, 2022
 */

package com.workly.model;

import com.google.gson.annotations.SerializedName;

public class Image {
  @SerializedName("id")
  private int id;
  @SerializedName("type")
  private String type;
  @SerializedName("data")
  private String data;
  @SerializedName("width")
  private int width;
  @SerializedName("height")
  private int height;
  @SerializedName("offset_x")
  private int offsetX;
  @SerializedName("offset_y")
  private int offsetY;

  public Image() {
  }

  public Image(int id, String type, String data, int width, int height, int offsetX, int offsetY) {
    this.id = id;
    this.type = type;
    this.data = data;
    this.width = width;
    this.height = height;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getOffsetX() {
    return offsetX;
  }

  public void setOffsetX(int offsetX) {
    this.offsetX = offsetX;
  }

  public int getOffsetY() {
    return offsetY;
  }

  public void setOffsetY(int offsetY) {
    this.offsetY = offsetY;
  }
}
