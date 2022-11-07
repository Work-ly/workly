/*
 * file: src/main/java/com/workly/controller/TeamController.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 11 November, 2022
 */

package com.workly.dao;

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
    return 0;
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
