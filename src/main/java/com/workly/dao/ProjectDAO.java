/*
 * file: src/main/java/com/workly/dao/ProjectDAO.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 20 September, 2022
 */
package com.workly.dao;

import com.workly.model.Project;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ProjectDAO implements DAO {

    private Connection dbConn;

    private String query;
    private PreparedStatement stmnt;
    private ResultSet rsltSet;

    public ProjectDAO(Connection dbConn) {
        this.dbConn = dbConn;
    }

    @Override
    public int create(Object obj) {
        Project project = (Project) obj;

        try {
            this.query
                    = "INSERT INTO _project (\n"
                    + "  name,\n"
                    + "  description,\n"
                    + ") values (\n"
                    + "  ?,\n"
                    + "  ?,\n"
                    + ") returning id;";

            this.stmnt = this.dbConn.prepareStatement(this.query);
            this.stmnt.setString(1, project.getName());
            this.stmnt.setString(2, project.getDescription());
            this.rsltSet = this.stmnt.executeQuery();

            if (this.rsltSet.next()) {
                project.setId(this.rsltSet.getInt("id"));
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Could not create project on database - " + e.getMessage());

            return -1;
        }

        return project.getId();
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
