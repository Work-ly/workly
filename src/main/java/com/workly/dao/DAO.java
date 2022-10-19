/*
 * file: src/main/java/com/workly/dao/DAO.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 13 August, 2022
 */

package com.workly.dao;

import java.util.List;

public interface DAO {
  public int create(Object obj);
  public Object get(Object obj);
  public List<?> getAll();
  public boolean update(Object obj);
  public boolean delete(Object obj);
}
