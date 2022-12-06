/*
 * file: src/main/java/com/workly/controller/ProjectController.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 06 December, 2022
 */

package com.workly.controller;

import java.sql.Connection;
import java.util.ArrayList;

import com.workly.dao.ImageDAO;
import com.workly.dao.ProjectDAO;
import com.workly.dao.TeamDAO;
import com.workly.model.*;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.workly.dao.UserDAO;
import com.workly.handler.FirebaseHandler;
import com.workly.util.Message;

import spark.Route;

public class ProjectController {
  /* gson */
  private Gson gson = new Gson();
  /* fbHndlr */
  private FirebaseHandler fbHndlr;
  /* dbConn */
  private Connection dbConn;

  public ProjectController(@NotNull Connection dbConn, @NotNull FirebaseHandler fbHndlr) {
    this.dbConn = dbConn;
    this.fbHndlr = fbHndlr;
  }

  public Route create = (request, response) -> {
    response.type("application/json");

    String idToken = request.headers("Authorization").replace("Bearer ", "");
    FirebaseUser fbUser = this.fbHndlr.auth(idToken);
    if (fbUser == null) {
      response.status(HttpStatus.UNAUTHORIZED_401);

      return gson.toJson(new Message("ERROR", "Could not delete user - [firebase]"), Message.class);
    }

    String teamName = request.params(":name");
    TeamDAO teamDAO = new TeamDAO(this.dbConn);
    Team team = (Team)teamDAO.get(teamName);
    if (team == null) {
      response.status(HttpStatus.BAD_REQUEST_400);

      return gson.toJson(new Message("ERROR", "Could not create project - Team doesn't exist"), Message.class);
    }

    Project project = gson.fromJson(request.body(), Project.class);
    if (project == null) {
      response.status(HttpStatus.BAD_REQUEST_400);

      return gson.toJson(new Message("ERROR", "Could not create project - Invalid project given"), Message.class);
    }

    ProjectDAO projectDAO = new ProjectDAO(this.dbConn);
    project.setTeam(team);
    project.setId(projectDAO.create(project));
    if (project == null) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);

      return gson.toJson(new Message("ERROR", "Could not create project on database"), Message.class);
    }

    response.status(HttpStatus.OK_200);
    return gson.toJson(project, Project.class);
  };

  public Route get = (request, response) -> {
    response.type("application/json");

    String idToken = request.headers("Authorization").replace("Bearer ", "");
    FirebaseUser fbUser = this.fbHndlr.auth(idToken);
    if (fbUser == null) {
      response.status(HttpStatus.UNAUTHORIZED_401);

      return gson.toJson(new Message("ERROR", "Could not delete user - [firebase]"), Message.class);
    }

    response.status(HttpStatus.OK_200);
    return gson.toJson(new Message("INFO", "Got it"), Message.class);
  };

  public Route getByTeam = (request, response) -> {
    response.type("application/json");

    String idToken = request.headers("Authorization").replace("Bearer ", "");
    FirebaseUser fbUser = this.fbHndlr.auth(idToken);
    if (fbUser == null) {
      response.status(HttpStatus.UNAUTHORIZED_401);

      return gson.toJson(new Message("ERROR", "Could not delete user - [firebase]"), Message.class);
    }

    String teamName = request.params(":name");
    TeamDAO teamDAO = new TeamDAO(this.dbConn);
    Team team = (Team)teamDAO.get(teamName);
    if (team == null) {
      response.status(HttpStatus.BAD_REQUEST_400);

      return gson.toJson(new Message("ERROR", "Could not create project - Team doesn't exist"), Message.class);
    }

    ProjectDAO projectDAO = new ProjectDAO(this.dbConn);
    ArrayList<Project> projects = projectDAO.getByTeam(team);

    String resp = "{ \"projects\": [";
    for (int i = 0; i < projects.size(); ++i) {
      Project project = projects.get(i);
      resp += gson.toJson(project, Project.class);

      if (i < projects.size() - 1) {
        resp += ", ";
      }
    }
    resp += "]\n}";

    response.status(HttpStatus.OK_200);
    return resp;
  };

  public Route update = (request, response) -> {
    response.type("application/json");

    String idToken = request.headers("Authorization").replace("Bearer ", "");
    FirebaseUser fbUser = this.fbHndlr.auth(idToken);
    if (fbUser == null) {
      response.status(HttpStatus.UNAUTHORIZED_401);

      return gson.toJson(new Message("ERROR", "Could not delete user - [firebase]"), Message.class);
    }

    response.status(HttpStatus.OK_200);
    return gson.toJson(new Message("INFO", "Updated"), Message.class);
  };

  public Route delete = (request, response) -> {
    response.type("application/json");

    String idToken = request.headers("Authorization").replace("Bearer ", "");
    FirebaseUser fbUser = this.fbHndlr.auth(idToken);
    if (fbUser == null) {
      response.status(HttpStatus.UNAUTHORIZED_401);

      return gson.toJson(new Message("ERROR", "Could not delete user - [firebase]"), Message.class);
    }

    response.status(HttpStatus.OK_200);
    return gson.toJson(new Message("INFO", "Deleted"), Message.class);
  };
}
