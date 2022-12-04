/*
 * file: src/main/java/com/workly/controller/TeamController.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 11 November, 2022
 */

package com.workly.dao;

import com.workly.model.UserTeam;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class UserTeamDAO implements DAO {
  /* dbConn */
  private Connection dbConn;
  /* query */
  private String query;
  /* stmnt */
  private PreparedStatement stmnt;
  /* rsltSet */
  private ResultSet rsltSet;

  public UserTeamDAO(Connection dbConn) {
    this.dbConn = dbConn;
  }

  @Override
  public int create(Object obj) {
    UserTeam ut = (UserTeam)obj;

    try {
      this.query =
        "INSERT INTO _user_team (\n"
          + "  _user_id,\n"
          + "  _team_id,\n"
          + "  _role\n"
          + ") values (\n"
          + "  ?,\n"
          + "  ?,\n"
          + "  ?\n"
          + ") RETURNING id;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setInt(1, ut.getUser().getId());
      this.stmnt.setInt(2, ut.getTeam().getId());
      this.stmnt.setString(3, ut.getRole());
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        ut.setId(this.rsltSet.getInt("id"));
      }

      this.stmnt.close();
      this.rsltSet.close();
    } catch (Exception e) {
      System.out.println("Could not create user team on database - " + e.getMessage());

      return -1;
    }

    return ut.getId();
  }

  @Override
  public Object get(Object obj) {
    return null;
  }

  @Override
  public List<?> getAll() {
    return null;
  }

  @Override
  public boolean update(Object obj) {
    return false;
  }

  @Override
  public boolean delete(Object obj) {
    return false;
  }
}