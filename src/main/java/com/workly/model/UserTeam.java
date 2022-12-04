/*
 * file: src/main/java/com/workly/model/UserTeam.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 29 November, 2022
 */

package com.workly.model;

import com.google.gson.annotations.SerializedName;

public class UserTeam {
  @SerializedName("id")
  private int id;
  @SerializedName("user")
  private User user;
  @SerializedName("team")
  private Team team;
  @SerializedName("role")
  private String role;

  public UserTeam() {
  }

  public UserTeam(int id, User user, Team team, String role) {
    this.id = id;
    this.user = user;
    this.team = team;
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

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
<<<<<<< HEAD


}
=======
  
  
}
>>>>>>> 19392886e20045ab39105287752fad92ffdf518b
