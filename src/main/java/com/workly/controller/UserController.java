/*
 * file: src/main/java/com/workly/controller/UserController.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 13 August, 2022
 */

package com.workly.controller;

import java.sql.Connection;
import java.util.ArrayList;

import com.workly.dao.ImageDAO;
import com.workly.model.Image;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.workly.dao.UserDAO;
import com.workly.handler.FirebaseHandler;
import com.workly.model.FirebaseUser;
import com.workly.model.User;
import com.workly.util.Message;

import spark.Filter;
import spark.Route;
import spark.Spark;

public class UserController {
  private Gson gson = new Gson();
  private FirebaseHandler fbHndlr;
  private Connection dbConn;

  public UserController(@NotNull FirebaseHandler fbHndlr, @NotNull Connection dbConn) {
    this.fbHndlr = fbHndlr;
    this.dbConn = dbConn;
  }

  public Message auth(String idToken) {
    if (idToken == null || "".equals(idToken)) {
      return new Message("ERROR", "User unauthorized");
    }

    idToken = idToken.replace("Bearer ", "");
    FirebaseUser fbUser = this.fbHndlr.auth(idToken);
    if (fbUser == null) {
      return new Message("ERROR", "User unauthorized");
    }

    return new Message("INFO", "User authorized");
  }

  public Route create = (request, response) -> {
    response.type("application/json");

    User user = gson.fromJson(request.body(), User.class);
    user.setReturnSecureToken(true);
    FirebaseUser fbUser = new FirebaseUser();

    fbUser = this.fbHndlr.create(fbUser, user);
    if (fbUser == null) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);

      return gson.toJson(new Message("ERROR", "Could not create user - [firebase]"), Message.class);
    }

    ImageDAO imgDAO = new ImageDAO(dbConn);
    int pfpImgId = imgDAO.create(user.getPfp());
    if (pfpImgId <= 0) {
      response.status(HttpStatus.CREATED_201);
      pfpImgId = 1;
    }

    int headerImgId = imgDAO.create(user.getHeader());
    if (headerImgId <= 0) {
      response.status(HttpStatus.CREATED_201);
      headerImgId = 2;
    }

    user.getPfp().setId(pfpImgId);
    user.getHeader().setId(headerImgId);

    user.setUuid(fbUser.getLocalId());
    UserDAO userDAO = new UserDAO(dbConn);
    int userId = userDAO.create(user);
    if (userId <= 0) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);

      return gson.toJson(new Message("ERROR", "Could not create user - [internal]"));
    }

    user.setId(userId);
    user.setPassword("");
    String resp = "{\n\"wly_user\": "
      + gson.toJson(user)
      + ",\n\"firebase_user\": "
      + gson.toJson(fbUser)
      + "\n}";
    response.status(HttpStatus.OK_200);

    return resp;
  };

  public Route get = (request, response) -> {
    response.type("application/json");

    String userName = request.params(":name");
    if (userName == null || "".equals(userName)) {
      System.out.println("Could not get user - Invalid username given");

      response.status(HttpStatus.FAILED_DEPENDENCY_424);
      return gson.toJson(new Message("ERROR", "Could not get user"));
    }

    UserDAO userDAO = new UserDAO(this.dbConn);
    User user = (User)userDAO.get(userName);
    if (user == null) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);

      return gson.toJson(new Message("ERROR", "Could not get user"));
    }

    ImageDAO imgDAO = new ImageDAO(this.dbConn);
    int pfpImgId = user.getPfp().getId();
    int headerImgId = user.getHeader().getId();
    user.setPfp((Image)imgDAO.get(pfpImgId));
    user.setHeader((Image)imgDAO.get(headerImgId));

    return gson.toJson(user, User.class);
  };

  public Route getAll = (request, response) -> {
    response.type("application/json");

    Message result = this.auth(request.headers("Authorization"));
    if (!result.getType().equals("INFO")) {
      response.status(HttpStatus.UNAUTHORIZED_401);
      return gson.toJson(result, Message.class);
    }

    UserDAO userDAO = new UserDAO(this.dbConn);
    ArrayList<User> users = (ArrayList<User>)userDAO.getAll();

    String resp = "{ \"users\": [";
    ImageDAO imgDAO = new ImageDAO(this.dbConn);
    for (int i = 0; i < users.size(); ++i) {
      User user = users.get(i);

      int pfpImgId = user.getPfp().getId();
      int headerImgId = user.getHeader().getId();
      user.setPfp((Image)imgDAO.get(pfpImgId));
      user.setHeader((Image)imgDAO.get(headerImgId));

      resp += gson.toJson(user, User.class);

      if (i < users.size() - 1) {
        resp += ", ";
      }
    }
    resp += "]\n}";

    return resp;
  };

  public Route delete = (request, response) -> {
    response.type("application/json");

    Message result = this.auth(request.headers("Authorization"));
    if (!result.getType().equals("INFO")) {
      response.status(HttpStatus.UNAUTHORIZED_401);
      return gson.toJson(result, Message.class);
    }

    String idToken = request.headers("Authorization").replace("Bearer ", "");
    FirebaseUser fbUser = this.fbHndlr.auth(idToken);
    if (fbUser == null) {
      response.status(HttpStatus.UNAUTHORIZED_401);

      return gson.toJson(new Message("ERROR", "Could not delete user - [firebase]"), Message.class);
    }
    fbUser.setIdToken(idToken);

    User user = new User();
    user.setUuid(fbUser.getLocalId());

    boolean status = this.fbHndlr.delete(fbUser);
    if (!status) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);
      return gson.toJson(new Message("ERROR", "Could not delete user - [firebase]"), Message.class);
    }

    UserDAO userDAO = new UserDAO(this.dbConn);
    status = userDAO.delete(user);
    if (!status) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);

      return gson.toJson(new Message("ERROR", "Could not delete user"), Message.class);
    }

    // TODO(J0sueTM): delete images

    return gson.toJson(new Message("INFO", "Deleted user successfully"), Message.class);
  };
}
