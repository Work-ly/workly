/*
 * file: src/main/java/com/workly/Server.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 12 August, 2022
 */

package com.workly;

import com.workly.controller.TeamController;
import com.workly.controller.UserController;
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

    UserController usrCntrllr = new UserController(fbHndlr, dbSrv.getConn());
    Spark.path("/", () -> {
      Spark.get("/user/:name", "application/json", usrCntrllr.get);
      Spark.get("/users", "application/json", usrCntrllr.getAll);
      Spark.post("/user", "application/json", usrCntrllr.create);
      Spark.delete("/user", "application/json", usrCntrllr.delete);
    });
    Spark.path("/user", () -> {
      Spark.post("/auth", usrCntrllr.auth);
    });
    
    TeamController teamController = new TeamController(dbSrv.getConn());
    Spark.path("/", () -> {
        Spark.post("/team", "application/json", teamController.create);
    });

    System.out.println("Server running - http://localhost:" + cfg.getPort());
  }
}