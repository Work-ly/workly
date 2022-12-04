/*
 * file: src/main/java/com/workly/controller/TeamController.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 11 November, 2022
 */

package com.workly.dao;

import com.workly.model.Team;
import com.workly.model.User;
import com.workly.model.UserTeam;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
    } catch (Exception e) {
      System.out.println("Could not create user team on database - " + e.getMessage());

      return -1;
    }

    return ut.getId();
  }

  @Override
  public Object get(Object obj) {
    int utId = (int)obj;
    UserTeam ut = new UserTeam();

    try {
      this.query = "SELECT * FROM _user_team WHERE id = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setInt(1, utId);
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        ut.setId(this.rsltSet.getInt("id"));

        User user = new User();
        user.setId(this.rsltSet.getInt("_user_id"));
        ut.setUser(user);

        Team team = new Team();
        team.setId(this.rsltSet.getInt("_team_id"));
        ut.setTeam(team);

        ut.setRole(this.rsltSet.getString("_role"));
      } else {
        return null;
      }

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not get user team - " + e.getMessage());

      return null;
    }

    return ut;
  }

  @Override
  public List<?> getAll() {
    return null;
  }

  public List<UserTeam> getByTeam(Team team) {
    List<UserTeam> uts = new ArrayList<>();

    try {
      this.query = "SELECT * FROM _user_team WHERE _team_id = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setInt(1, team.getId());
      this.rsltSet = this.stmnt.executeQuery();

      while (this.rsltSet.next()) {
        UserTeam ut = new UserTeam();
        ut.setId(this.rsltSet.getInt("id"));

        User user = new User();
        user.setId(this.rsltSet.getInt("_user_id"));
        ut.setUser(user);

        Team team_ = new Team();
        team_.setId(this.rsltSet.getInt("_team_id"));
        ut.setTeam(team_);

        ut.setRole(this.rsltSet.getString("_role"));

        uts.add(ut);
      }

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not get user teams - " + e.getMessage());
    }

    return uts;
  }

  public List<UserTeam> getByUser(User user) {
    List<UserTeam> uts = new ArrayList<>();

    try {
      this.query = "SELECT * FROM _user_team WHERE _user_id = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setInt(1, user.getId());
      this.rsltSet = this.stmnt.executeQuery();

      while (this.rsltSet.next()) {
        UserTeam ut = new UserTeam();
        ut.setId(this.rsltSet.getInt("id"));

        User user_ = new User();
        user.setId(this.rsltSet.getInt("_user_id"));
        ut.setUser(user_);

        Team team = new Team();
        team.setId(this.rsltSet.getInt("_team_id"));
        ut.setTeam(team);

        ut.setRole(this.rsltSet.getString("_role"));

        uts.add(ut);
      }
    } catch (Exception e) {
      System.out.println("Could not get user teams - " + e.getMessage());
    }

    return uts;
  }

  @Override
  public boolean update(Object obj) {
    UserTeam ut = (UserTeam)obj;

    try {
      this.query = "UPDATE _user_team SET _role = ? WHERE id = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, ut.getRole());
      this.stmnt.setInt(2, ut.getId());
      this.rsltSet = this.stmnt.executeQuery();

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not update user team - " + e.getMessage());
    }

    return true;
  }

  @Override
  public boolean delete(Object obj) {
    UserTeam ut = (UserTeam)obj;

    try {
      this.query = "DELETE FROM _user_team WHERE id = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setInt(1, ut.getId());
      this.stmnt.executeUpdate();

      this.stmnt.close();
    }  catch (Exception e) {
      System.out.println("Could not delete user team - " + e.getMessage());

      return false;
    }

    return true;
  }
}
