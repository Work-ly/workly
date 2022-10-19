/*
 * file: src/main/java/com/workly/model/FirebaseLookupUser.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 14 August, 2022
 */

package com.workly.model;

import com.google.gson.annotations.SerializedName;

public class FirebaseLookupUser {
  @SerializedName("users")
  private FirebaseUser[] users;

  public FirebaseLookupUser() {
  }

  public FirebaseUser[] getUsers() {
    return users;
  }

  public void setUsers(FirebaseUser[] users) {
    this.users = users;
  }
}
