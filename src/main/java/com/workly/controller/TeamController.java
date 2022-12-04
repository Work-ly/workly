/*
 * file: src/main/java/com/workly/controller/TeamController.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 20 September, 2022
 */

package com.workly.controller;

import com.google.gson.Gson;
import com.workly.dao.ImageDAO;
import com.workly.dao.TeamDAO;
import com.workly.dao.UserDAO;
import com.workly.dao.UserTeamDAO;
import com.workly.handler.FirebaseHandler;
import com.workly.model.FirebaseUser;
import com.workly.model.Team;
import com.workly.model.User;
import com.workly.util.Message;
import java.sql.Connection;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import spark.Route;

public class TeamController {
  /* gson */
  private Gson gson = new Gson();
  /* dbConn */
  private Connection dbConn;
  /* fbHndlr */
  private FirebaseHandler fbHndlr;

  public TeamController(@NotNull Connection dbConn, @NotNull FirebaseHandler fbHndlr) {
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

    Team team = gson.fromJson(request.body(), Team.class);
    if (team == null) {
      response.status(HttpStatus.BAD_REQUEST_400);

      return gson.toJson(new Message("ERROR", "Could not create team - [internal]"), Message.class);
    }

    ImageDAO imgDAO = new ImageDAO(dbConn);
    int pfpImgId = imgDAO.create(team.getPfp());
    if (pfpImgId <= 0) {
      response.status(HttpStatus.CREATED_201);
      pfpImgId = 1;
    }

    int headerImgId = imgDAO.create(team.getHeader());
    if (headerImgId <= 0) {
      response.status(HttpStatus.CREATED_201);
      headerImgId = 2;
    }

    team.getPfp().setId(pfpImgId);
    team.getHeader().setId(headerImgId);

    TeamDAO teamDAO = new TeamDAO(dbConn);
    int teamId = teamDAO.create(team);
    if (teamId <= 0) {
      response.status(HttpStatus.FAILED_DEPENDENCY_424);

      return gson.toJson(new Message("ERROR", "Could not create team - [internal]"));
    }
    team.setId(teamId);

    UserDAO userDAO = new UserDAO(dbConn);
    User user = (User)userDAO.getByUuid(fbUser.getLocalId());

    // TODO(J0sueTM): Add created team to current user
    UserTeamDAO userTeamDAO = new UserTeamDAO(dbConn);

    response.status(HttpStatus.OK_200);
    return gson.toJson(team, Team.class);
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