/*
 * file: src/main/java/com/workly/dao/TeamDAO.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 20 September, 2022
 */
package com.workly.dao;

import com.workly.model.Image;
import com.workly.model.Team;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class TeamDAO implements DAO {
  /* dbConn */
  private Connection dbConn;

  /* query */
  private String query;
  /* stmnt */
  private PreparedStatement stmnt;
  /* rsltSet */
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

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not create team on database - " + e.getMessage());

      return -1;
    }

    return team.getId();
  }

  @Override
  public Object get(Object obj) {
    String teamName = (String)obj;
    Team team = new Team();

    try {
      this.query = "SELECT * FROM _team WHERE name = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, teamName);
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        team.setId(this.rsltSet.getInt("id"));
        team.setName(this.rsltSet.getString("name"));
        team.setDescription(this.rsltSet.getString("description"));
        team.setPfp(new Image(this.rsltSet.getInt("pfp_img_id")));
        team.setHeader(new Image(this.rsltSet.getInt("header_img_id")));
      } else {
        return null;
      }

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not get team - " + e.getMessage());

      return null;
    }

    return team;
  }

  public Object getById(int id) {
    Team team = new Team();
    team.setId(id);

    try {
      this.query = "SELECT * FROM _team WHERE id = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setInt(1, id);
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        team.setId(this.rsltSet.getInt("id"));
        team.setName(this.rsltSet.getString("name"));
        team.setDescription(this.rsltSet.getString("description"));
        team.setPfp(new Image(this.rsltSet.getInt("pfp_img_id")));
        team.setHeader(new Image(this.rsltSet.getInt("header_img_id")));
      } else {
        return null;
      }

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not get team - " + e.getMessage());

      return null;
    }

    return team;
  }

  @Override
  public List<?> getAll() {
    return null;
  }

  @Override
  public boolean update(Object obj) {
    Team team = (Team)obj;

    try {
      this.query = "UPDATE _team SET name = ?, description = ? WHERE id = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, team.getName());
      this.stmnt.setString(2, team.getDescription());
      this.stmnt.setInt(3, team.getId());
      this.stmnt.executeUpdate();

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not update team - " + e.getMessage());

      return false;
    }

    return true;
  }

  @Override
  public boolean delete(Object obj) {
    Team team = (Team)obj;

    try {
      this.query = "DELETE FROM _team WHERE id = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setInt(1, team.getId());
      this.stmnt.executeUpdate();

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not delete team from database - " + e.getMessage());

      return false;
    }

    return true;
  }
}