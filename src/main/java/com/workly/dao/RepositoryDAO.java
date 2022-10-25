/*
 * file: src/main/java/com/workly/dao/RepositoryDAO.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 26 September, 2022
 */
package com.workly.dao;

import com.workly.model.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
 
public class RepositoryDAO implements DAO {
    /* dbConn */
    private Connection dbConn;

    /* query */
    private String query;
    /* stmnt */
    private PreparedStatement stmnt;
    /* rsltSet */
    private ResultSet rsltSet;

    public RepositoryDAO(Connection dbConn) {
        this.dbConn = dbConn;
    }

    @Override
    public int create(Object obj) {
        Repository repository = (Repository) obj;

        try {
            this.query
                    = "INSERT INTO _repository (\n"
                    + "  uuid,\n"
                    + "  upstream,\n"
                    + ") values (\n"
                    + "  ?,\n"
                    + "  ?,\n"
                    + ") returning id;";

            this.stmnt = this.dbConn.prepareStatement(this.query);
            this.stmnt.setString(1, repository.getUuid());
            this.stmnt.setString(2, repository.getUpstream());
            this.rsltSet = this.stmnt.executeQuery();

            if (this.rsltSet.next()) {
                repository.setId(this.rsltSet.getInt("id"));
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Could not create repository on database - " + e.getMessage());

            return -1;
        }

        return repository.getId();
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

