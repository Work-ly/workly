/*
 * file: src/main/java/com/workly/dao/DocumentDAO.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 26 September, 2022
 */
package com.workly.dao;

import com.workly.model.Document;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DocumentDAO implements DAO{
  /* dbConn */
  private Connection dbConn;

  /* query */
  private String query;
  /* stmnt */
  private PreparedStatement stmnt;
  /* rsltSet */
  private ResultSet rsltSet;

  public DocumentDAO(Connection dbConn) {
    this.dbConn = dbConn;
  }

  @Override
  public int create(Object obj) {
    Document document = (Document) obj;

    try {
      this.query
        = "INSERT INTO _document (\n"
        + "  title,\n"
        + "  content,\n"
        + "  _user_id,\n"
        + "  _project_id\n"
        + ") values (\n"
        + "  ?,\n"
        + "  ?,\n"
        + "  ?,\n"
        + "  ?,\n"
        + ") returning id;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, document.getTitle());
      this.stmnt.setString(2, document.getContent());
      this.stmnt.setInt(2, document.getUser().getId());
      this.stmnt.setInt(2, document.getProject().getId());
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        document.setId(this.rsltSet.getInt("id"));
      } else {
        return -1;
      }
    } catch (Exception e) {
      System.out.println("Could not create document on database - " + e.getMessage());

      return -1;
    }

    return document.getId();
  }

  @Override
  public Object get(Object obj) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public List<?> getAll() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public boolean update(Object obj) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public boolean delete(Object obj) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
}
