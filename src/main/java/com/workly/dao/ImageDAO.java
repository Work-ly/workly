/*
 * file: src/main/java/com/workly/dao/ImageDAO.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 18 September, 2022
 */

package com.workly.dao;

import com.workly.model.Image;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ImageDAO implements DAO {
  /* dbConn */
  private Connection dbConn;

  /* query */
  private String query;
  /* stmnt */
  private PreparedStatement stmnt;
  /* rsltSet */
  private ResultSet rsltSet;

  public ImageDAO(Connection dbConn) {
    this.dbConn = dbConn;
  }

  @Override
  public int create(Object obj) {
    Image img = (Image)obj;

    try {
      this.query =
        "INSERT INTO _img (\n"
          + "  type,\n"
          + "  data,\n"
          + "  width,\n"
          + "  height,\n"
          + "  offset_x,\n"
          + "  offset_y\n"
          + ") values (\n"
          + "  ?,\n"
          + "  decode(?, 'base64'),\n"
          + "  ?,\n"
          + "  ?,\n"
          + "  ?,\n"
          + "  ?"
          + ") returning id;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, img.getType());
      this.stmnt.setString(2, img.getData());
      this.stmnt.setInt(3, img.getWidth());
      this.stmnt.setInt(4, img.getHeight());
      this.stmnt.setInt(5, img.getOffsetX());
      this.stmnt.setInt(6, img.getOffsetY());
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        img.setId(this.rsltSet.getInt("id"));
      } else {
        return -1;
      }
    } catch (Exception e) {
      System.out.println("Could not create image on database - " + e.getMessage());

      return -1;
    }

    return img.getId();
  }

  @Override
  public Object get(Object obj) {
    Image img = new Image((int)obj);

    try {
      this.query = "SELECT type, encode(data, 'base64'), width, height, offset_x, offset_y FROM _img WHERE id = ?;";
      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setInt(1, img.getId());
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        img.setType(this.rsltSet.getString("type"));
        img.setData(this.rsltSet.getString("encode"));
        img.setWidth(this.rsltSet.getInt("width"));
        img.setHeight(this.rsltSet.getInt("height"));
        img.setOffsetX(this.rsltSet.getInt("offset_x"));
        img.setOffsetY(this.rsltSet.getInt("offset_y"));
      }
    } catch (Exception e) {
      System.out.println("Could not get image from database - " + e.getMessage());

      return null;
    }

    return img;
  }

  @Override
  public List<?> getAll() {
    return null;
  }

  @Override
  public boolean update(Object obj) {
    Image img = (Image)obj;

    try {
      this.query =
        "UPDATE _img\n"
          + "SET type = ?,\n"
          + "    data = decode(?, 'base64'),\n"
          + "    width = ?,\n"
          + "    height = ?,\n"
          + "    offset_x = ?,\n"
          + "    offset_y = ?\n"
          + "WHERE id = ?;";
      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, img.getType());
      this.stmnt.setString(2, img.getData());
      this.stmnt.setInt(3, img.getWidth());
      this.stmnt.setInt(4, img.getHeight());
      this.stmnt.setInt(5, img.getOffsetX());
      this.stmnt.setInt(6, img.getOffsetY());
      this.stmnt.setInt(7, img.getId());
      this.stmnt.executeUpdate();
    } catch (Exception e) {
      System.out.println("Could not update image on database - " + e.getMessage());

      return false;
    }

    return true;
  }

  @Override
  public boolean delete(Object obj) {
    Image img = (Image)obj;

    try {
      this.query = "DELETE FROM _img WHERE id = ?;";
      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setInt(1, img.getId());
      this.stmnt.executeUpdate();
    } catch (Exception e) {
      System.out.println("Could not delete image from database - " + e.getMessage());

      return false;
    }

    return true;
  }
}