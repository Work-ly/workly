/*
 * file: src/main/java/com/workly/dao/UserDAO.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 13 August, 2022
 */

package com.workly.dao;

import com.workly.model.Image;
import com.workly.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO {
  /* dbConn */
  private Connection dbConn;
  /* query */
  private String query;
  /* stmnt */
  private PreparedStatement stmnt;
  /* rsltSet */
  private ResultSet rsltSet;

  public UserDAO(Connection dbConn) {
    this.dbConn = dbConn;
  }

  @Override
  public int create(Object obj) {
    User user = (User)obj;

    try {
      this.query =
        "INSERT INTO _user (\n"
          + "  uuid,\n"
          + "  name,\n"
          + "  email,\n"
          + "  description,\n"
          + "  pfp_img_id,\n"
          + "  header_img_id\n"
          + ") values (\n"
          + "  ?,\n"
          + "  ?,\n"
          + "  ?,\n"
          + "  ?,\n"
          + "  ?,\n"
          + "  ?\n"
          + ") RETURNING id;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, user.getUuid());
      this.stmnt.setString(2, user.getName());
      this.stmnt.setString(3, user.getEmail());
      this.stmnt.setString(4, user.getDescription());
      this.stmnt.setInt(5, user.getPfp().getId());
      this.stmnt.setInt(6, user.getHeader().getId());
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        user.setId(this.rsltSet.getInt("id"));
      }

      this.stmnt.close();
      this.rsltSet.close();
    } catch (Exception e) {
      System.out.println("Could not create user on database - " + e.getMessage());

      return -1;
    }

    return user.getId();
  }

  @Override
  public Object get(Object obj) {
    String userName = (String)obj;
    User user = new User();

    try {
      this.query = "SELECT * FROM _user where name = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, userName);
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        user.setId(this.rsltSet.getInt("id"));
        user.setUuid(this.rsltSet.getString("uuid"));
        user.setName(this.rsltSet.getString("name"));
        user.setEmail(this.rsltSet.getString("email"));
        user.setDescription(this.rsltSet.getString("description"));
        user.setPfp(new Image(this.rsltSet.getInt("pfp_img_id")));
        user.setHeader(new Image(this.rsltSet.getInt("header_img_id")));
      } else {
        return null;
      }

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not get user - " + e.getMessage());

      return null;
    }

    return user;
  }

  public Object getByUuid(String uuid) {
    User user = new User();
    user.setUuid(uuid);

    try {
      this.query = "SELECT * FROM _user WHERE uuid = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, user.getUuid());
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        user.setId(this.rsltSet.getInt("id"));
        user.setName(this.rsltSet.getString("name"));
        user.setEmail(this.rsltSet.getString("email"));
        user.setDescription(this.rsltSet.getString("description"));
        user.setPfp(new Image(this.rsltSet.getInt("pfp_img_id")));
        user.setHeader(new Image(this.rsltSet.getInt("header_img_id")));
      } else {
        return null;
      }

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not get user - " + e.getMessage());

      return null;
    }

    return user;
  }

  @Override
  public List<?> getAll() {
    List<User> users = new ArrayList<>();

    try {
      this.query = "SELECT * FROM _user;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.rsltSet = this.stmnt.executeQuery();

      while (this.rsltSet.next()) {
        User user = new User();

        user.setId(this.rsltSet.getInt("id"));
        user.setUuid(this.rsltSet.getString("uuid"));
        user.setName(this.rsltSet.getString("name"));
        user.setEmail(this.rsltSet.getString("email"));
        user.setDescription(this.rsltSet.getString("description"));
        user.setPfp(new Image(this.rsltSet.getInt("pfp_img_id")));
        user.setHeader(new Image(this.rsltSet.getInt("header_img_id")));

        users.add(user);
      }

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not get user - " + e.getMessage());
    }

    return users;
  }

  @Override
  public boolean update(Object obj) {
    return true;
  }

  @Override
  public boolean delete(Object obj) {
    User user = (User)obj;

    try {
      this.query = "DELETE FROM _user WHERE uuid = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, user.getUuid());
      this.stmnt.executeUpdate();

      this.stmnt.close();
    } catch (Exception e) {
      System.out.println("Could not delete user from database - " + e.getMessage());

      return false;
    }

    return true;
  }
}