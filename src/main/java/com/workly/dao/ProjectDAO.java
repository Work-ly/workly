/*
 * file: src/main/java/com/workly/dao/ProjectDAO.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 20 September, 2022
 */
package com.workly.dao;

import com.workly.model.Project;
import com.workly.model.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO implements DAO {

    /* dbConn */
  private Connection dbConn;

  /* query */
  private String query;
  /* stmnt */
  private PreparedStatement stmnt;
  /* rsltSet */
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
                    + "  _team_id\n"
                    + ") values (\n"
                    + "  ?,\n"
                    + "  ?,\n"
                    + "  ?\n"
                    + ") returning id;";

            this.stmnt = this.dbConn.prepareStatement(this.query);
            this.stmnt.setString(1, project.getName());
            this.stmnt.setString(2, project.getDescription());
            this.stmnt.setInt(3, project.getTeam().getId());
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
    String projectName = (String) obj;
    Project project = new Project();

    try {
      this.query
        = "SELECT * FROM _project WHERE name = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setString(1, projectName);
      this.rsltSet = this.stmnt.executeQuery();

      if (this.rsltSet.next()) {
        project.setId(this.rsltSet.getInt("id"));
        project.setName(this.rsltSet.getString("name"));
        project.setDescription(this.rsltSet.getString("description"));
      } else {
        return null;
      }
    } catch (Exception e) {
      System.out.println("Could not get project from database - " + e.getMessage());

      return null;
    }

    return project;
  }

  public ArrayList<Project> getByTeam(Object obj) {
    Team team = (Team)obj;
    ArrayList<Project> projects = new ArrayList<>();

    try {
      this.query
        = "SELECT * FROM _project WHERE _team_id = ?;";

      this.stmnt = this.dbConn.prepareStatement(this.query);
      this.stmnt.setInt(1, team.getId());
      this.rsltSet = this.stmnt.executeQuery();

      while (this.rsltSet.next()) {
        Project project = new Project();

        project.setId(this.rsltSet.getInt("id"));
        project.setName(this.rsltSet.getString("name"));
        project.setDescription(this.rsltSet.getString("description"));
        project.setTeam(team);

        projects.add(project);
      }
    } catch (Exception e) {
      System.out.println("Could not get project from database - " + e.getMessage());
    }

    return projects;
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
