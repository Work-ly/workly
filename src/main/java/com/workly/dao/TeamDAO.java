/*
 * file: src/main/java/com/workly/dao/TeamDAO.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 20 September, 2022
 */
package com.workly.dao;

import com.workly.model.Team;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class TeamDAO implements DAO {
  private Connection dbConn;

  private String query;
  private PreparedStatement stmnt;
  private ResultSet rsltSet;

  public TeamDAO(Connection dbConn) {
    this.dbConn = dbConn;
  }

  @Override
  public int create(Object obj) {
    Team team = (Team) obj;

    try {
      this.query
              = "INSERT INTO _team (\n"
              + "  name,\n"
              + "  description,\n"
              + "  pfp_img_id,\n"
              + "  header_img_id\n"
              + ") VALUES (\n"
              + "  ?,\n"
              + "  ?,\n"
              + "  ?,\n"
              + "  ?\n"
              + ") RETURNING id;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, team.getName());
      this.stmnt.setString(2, team.getDescription());
      this.stmnt.setInt(3, team.getPfp().getId());
      this.stmnt.setInt(4, team.getHeader().getId());
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        team.setId(this.rsltSet.getInt("id"));
      } else {
        return -1;
      }
    } catch (Exception e) {
      System.out.println("Could not create team on database - " + e.getMessage());

      return -1;
    }

    return team.getId();
  }

  @Override
  public Object get(Object obj) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<?> getAll() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean update(Object obj) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean delete(Object obj) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
