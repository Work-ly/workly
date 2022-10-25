/*
 * file: src/main/java/com/workly/util/Message.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 13 August, 2022
 */

package com.workly.util;

import com.google.gson.annotations.SerializedName;

public class Message {
  /* type  */
  @SerializedName("type")
  private String type;
  /* msg */
  @SerializedName("message")
  private String msg;

  public Message(String type, String msg) {
    this.type = type;
    this.msg = msg;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
