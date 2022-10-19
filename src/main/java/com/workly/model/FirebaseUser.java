/*
 * file: src/main/java/com/workly/model/FirebaseUser.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 13 August, 2022
 */

package com.workly.model;

import com.google.gson.annotations.SerializedName;

public class FirebaseUser {
  @SerializedName("kind")
  private String kind;
  @SerializedName("idToken")
  private String idToken;
  @SerializedName("photoUrl")
  private String pfp;
  @SerializedName("email")
  private String email;
  @SerializedName("refreshToken")
  private String refreshToken;
  @SerializedName("expiresIn")
  private String expiresIn;
  @SerializedName("localId")
  private String localId;

  public FirebaseUser() {
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public String getIdToken() {
    return idToken;
  }

  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }

  public String getPfp() {
    return pfp;
  }

  public void setPfp(String pfp) {
    this.pfp = pfp;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(String expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getLocalId() {
    return localId;
  }

  public void setLocalId(String localId) {
    this.localId = localId;
  }
}
