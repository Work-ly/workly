/*
 * file: src/main/java/com/workly/Server.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 12 August, 2022
 */

package com.workly;

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
    Spark.get("/team/:name/users", "application/json", utCntrllr.getByTeam);
    Spark.get("/user/:name/teams", "application/json", utCntrllr.getByUser);
    Spark.put("/team/user", "application/json", utCntrllr.update);
    Spark.delete("/team/user", "application/json", utCntrllr.delete);

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