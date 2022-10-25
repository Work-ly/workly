/*
 * file: src/main/java/com/workly/controller/TeamController.java
 * author: Gustavo Bizo Jardim <gustavobizo@protonmail.com>
 * date: 20 September, 2022
 */

package com.workly.controller;

import com.google.gson.Gson;
import com.workly.dao.ImageDAO;
import com.workly.dao.TeamDAO;
import com.workly.model.Team;
import com.workly.util.Fetcher;
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
  /* userHndlr */
  private Fetcher userHndlr;

  public TeamController(@NotNull Connection dbConn) {
    this.dbConn = dbConn;
    this.userHndlr = new Fetcher("");
  }
  
  public Route create = (request, response) -> {
    response.type("application/json");
    
    Message err = this.userHndlr.fetch("POST", "/user/auth", "");
    if (err.getType() != "INFO")
      return gson.toJson(err, Message.class);

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
    
    response.status(HttpStatus.OK_200);
    return gson.toJson(team, Team.class);
  };
}
