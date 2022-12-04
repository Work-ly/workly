/*
 * file: src/main/java/com/workly/controller/UserController.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 13 August, 2022
 */

package com.workly.controller;

import java.sql.Connection;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.workly.dao.UserDAO;
import com.workly.dao.UserTeamDAO;
import com.workly.handler.FirebaseHandler;
import com.workly.model.User;
import com.workly.model.UserTeam;
import com.workly.util.Message;
import org.eclipse.jetty.http.HttpStatus;

import spark.Route;

public class UserTeamController {
  /* gson */
  private Gson gson = new Gson();
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

    UserTeam ut = gson.fromJson(request.body(), UserTeam.class);
    ut.setUser(new User());
    ut.getUser().setName(request.params(":name"));
    if (!ut.getRole().equals("owner") && !ut.getRole().equals("member")) {
      response.status(HttpStatus.BAD_REQUEST_400);
      return gson.toJson(new Message("ERROR", "Could not add user to team - Invalid role"));
    }

    UserDAO userDAO = new UserDAO(this.dbConn);
    ut.setUser((User)userDAO.get(ut.getUser().getName()));
    if (ut.getUser() == null) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);
      return gson.toJson(new Message("ERROR", "Could not add user to team - [internal]"));
    }

    UserTeamDAO utDAO = new UserTeamDAO(this.dbConn);
    ut.setId(utDAO.create(ut));
    if (ut.getId() <= 0) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);
      return gson.toJson(new Message("ERROR", "Could not add user to team - [internal]"));
    }

    return gson.toJson(ut, UserTeam.class);
  };

  public Route get = (request, response) -> {
    response.type("application/json");

    response.status(HttpStatus.OK_200);
    return gson.toJson(new Message("INFO", "Got it"), Message.class);
  };

  public Route getByUser = (request, response) -> {
    response.type("application/json");

    response.status(HttpStatus.OK_200);
    return gson.toJson(new Message("INFO", "Got them by user"), Message.class);
  };

  public Route getByTeam = (request, response) -> {
    response.type("application/json");

    response.status(HttpStatus.OK_200);
    return gson.toJson(new Message("INFO", "Got them by team"), Message.class);
  };

  public Route update = (request, response) -> {
    response.type("application/json");

    response.status(HttpStatus.OK_200);
    return gson.toJson(new Message("INFO", "Updated"), Message.class);
  };

  public Route delete = (request, response) -> {
    response.type("application/json");

    response.status(HttpStatus.OK_200);
    return gson.toJson(new Message("INFO", "Deleted"), Message.class);
  };
}