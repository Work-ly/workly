/*
 * file: src/main/java/com/workly/Server.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 12 August, 2022
 */

package com.workly;

import com.workly.controller.ProjectController;
import com.workly.controller.TeamController;
import com.workly.controller.UserController;
import com.workly.controller.UserTeamController;
import com.workly.handler.FirebaseHandler;
import com.workly.util.MicroserviceConfig;
import org.jetbrains.annotations.NotNull;
import spark.Spark;

public class Server {
  private final MicroserviceConfig cfg;

  public Server(@NotNull MicroserviceConfig cfg, @NotNull FirebaseHandler fbHndlr, @NotNull DBServer dbSrv) {
    this.cfg = cfg;

    Spark.port(Integer.parseInt(this.cfg.getPort()));
    Spark.init();

    setupBefores();

    UserController usrCntrllr = new UserController(dbSrv.getConn(), fbHndlr);
    TeamController teamCntrllr = new TeamController(dbSrv.getConn(), fbHndlr);
    UserTeamController utCntrllr = new UserTeamController(dbSrv.getConn(), fbHndlr);
    ProjectController projectCntrllr = new ProjectController(dbSrv.getConn(), fbHndlr);

    Spark.post("/user", "application/json", usrCntrllr.create);
    Spark.get("/user/:name", "application/json", usrCntrllr.get);
    Spark.get("/users", "application/json", usrCntrllr.getAll);
    Spark.put("/user", "application/json", usrCntrllr.update);
    Spark.delete("/user", "application/json", usrCntrllr.delete);
    Spark.post("/user/login", usrCntrllr.login);

    Spark.post("/team", "application/json", teamCntrllr.create);
    Spark.get("/team/:name", "application/json", teamCntrllr.get);
    Spark.put("/team/:name", "application/json", teamCntrllr.update);
    Spark.delete("/team/:name", "application/json", teamCntrllr.delete);

    Spark.post("/team/user", "application/json", utCntrllr.create);
    Spark.get("/team/:name/users", "application/json", utCntrllr.getUsersByTeam);
    Spark.get("/user/:name/teams", "application/json", utCntrllr.getTeamsByUser);
    Spark.put("/team/user/:name", "application/json", utCntrllr.update);
    Spark.delete("/team/user/:name", "application/json", utCntrllr.delete);

    Spark.post("/team/:name/project", "application/json", projectCntrllr.create);
    Spark.get("/team/:name/project/:name", "application/json", projectCntrllr.get);
    Spark.get("/team/:name/projects", "application/json", projectCntrllr.getByTeam);
    Spark.put("/team/:name/project/:name", "application/json", projectCntrllr.update);
    Spark.delete("/team/:name/project/:name", "application/json", projectCntrllr.delete);

    System.out.println("Server running - http://localhost:" + cfg.getPort());
  }

  private void setupBefores() {
    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });
    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
  }
}