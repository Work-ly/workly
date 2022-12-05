/*
 * file: src/main/java/com/workly/controller/UserController.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 13 August, 2022
 */

package com.workly.controller;

import java.sql.Connection;
import java.util.ArrayList;

import com.workly.dao.ImageDAO;
import com.workly.dao.TeamDAO;
import com.workly.model.*;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.workly.dao.UserDAO;
import com.workly.dao.UserTeamDAO;
import com.workly.handler.FirebaseHandler;
import com.workly.util.Message;
import org.eclipse.jetty.http.HttpStatus;

import spark.Route;

public class UserTeamController {
  /* gson */
  private final Gson gson = new Gson();
  /* fbHndlr */
  private FirebaseHandler fbHndlr;
  /* dbConn */
  private Connection dbConn;

  public UserTeamController(@NotNull Connection dbConn, @NotNull FirebaseHandler fbHndlr) {
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

    UserTeam ut = gson.fromJson(request.body(), UserTeam.class);
    if (!ut.getRole().equals("owner") && !ut.getRole().equals("member")) {
      response.status(HttpStatus.BAD_REQUEST_400);
      return gson.toJson(new Message("ERROR", "Could not add user to team - Invalid role"));
    }

    UserDAO userDAO = new UserDAO(this.dbConn);
    ut.setUser((User)userDAO.get(ut.getUser().getName()));
    if (ut.getUser() == null) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);
      return gson.toJson(new Message("ERROR", "Could not add user to team - User doesn't exist"));
    }

    TeamDAO teamDAO = new TeamDAO(this.dbConn);
    ut.setTeam((Team)teamDAO.getById(ut.getTeam().getId()));
    if (ut.getTeam() == null) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);
      return gson.toJson(new Message("ERROR", "Could not add user to team - Team doesn't exist"));
    }

    UserTeamDAO utDAO = new UserTeamDAO(this.dbConn);
    ut.setId(utDAO.create(ut));
    if (ut.getId() <= 0) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);
      return gson.toJson(new Message("ERROR", "Could not add user to team on db"));
    }

    return gson.toJson(ut, UserTeam.class);
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

  public Route getTeamsByUser = (request, response) -> {
    response.type("application/json");

    String idToken = request.headers("Authorization").replace("Bearer ", "");
    FirebaseUser fbUser = this.fbHndlr.auth(idToken);
    if (fbUser == null) {
      response.status(HttpStatus.UNAUTHORIZED_401);

      return gson.toJson(new Message("ERROR", "Could not delete user - [firebase]"), Message.class);
    }

    UserDAO userDAO = new UserDAO(this.dbConn);
    User user = (User)userDAO.get(request.params(":name"));
    if (user == null) {
      response.status(HttpStatus.BAD_REQUEST_400);
      return gson.toJson(new Message("ERROR", "Could not get teams from user - User doesn't exist"));
    }

    UserTeamDAO utDAO = new UserTeamDAO(this.dbConn);
    ArrayList<UserTeam> uts = (ArrayList<UserTeam>) utDAO.getByUser(user);

    String resp = "{ \"teams\": [";
    TeamDAO teamDAO = new TeamDAO(this.dbConn);
    ImageDAO imgDAO = new ImageDAO(this.dbConn);
    for (int i = 0; i < uts.size(); ++i) {
      Team team = uts.get(i).getTeam();
      team = (Team)teamDAO.getById(team.getId());
      if (team != null) {
        team.setPfp((Image)imgDAO.get(team.getPfp().getId()));
        team.setHeader((Image)imgDAO.get(team.getHeader().getId()));

        resp += gson.toJson(team, Team.class);

        if (i < uts.size() - 1) {
          resp += ", ";
        }
      }
    }
    resp += "]\n}";

    response.status(HttpStatus.OK_200);
    return resp;
  };

  public Route getUsersByTeam = (request, response) -> {
    response.type("application/json");

    String idToken = request.headers("Authorization").replace("Bearer ", "");
    FirebaseUser fbUser = this.fbHndlr.auth(idToken);
    if (fbUser == null) {
      response.status(HttpStatus.UNAUTHORIZED_401);

      return gson.toJson(new Message("ERROR", "Could not delete user - [firebase]"), Message.class);
    }

    TeamDAO teamDAO = new TeamDAO(this.dbConn);
    Team team = (Team)teamDAO.get(request.params(":name"));
    if (team == null) {
      response.status(HttpStatus.BAD_REQUEST_400);
      return gson.toJson(new Message("ERROR", "Could not get users from team - Team doesn't exist"));
    }

    UserTeamDAO utDAO = new UserTeamDAO(this.dbConn);
    ArrayList<UserTeam> uts = (ArrayList<UserTeam>)utDAO.getByTeam(team);

    String resp = "{ \"users\": [";
    UserDAO userDAO = new UserDAO(this.dbConn);
    ImageDAO imgDAO = new ImageDAO(this.dbConn);
    for (int i = 0; i < uts.size(); ++i) {
      User user = uts.get(i).getUser();
      user = (User)userDAO.getById(user.getId());
      if (user != null) {
        user.setPfp((Image)imgDAO.get(user.getPfp().getId()));
        user.setHeader((Image)imgDAO.get(user.getHeader().getId()));

        resp += gson.toJson(user, User.class);

        if (i < uts.size() - 1) {
          resp += ", ";
        }
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