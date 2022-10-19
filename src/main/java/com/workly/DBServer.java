/*
 * file: src/main/java/com/workly/DBServer.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 12 August, 2022
 */

package com.workly;

import com.workly.util.MicroserviceConfig;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBServer {
  private final String URL;
  private final Properties props;
  private Connection conn;

  public DBServer(@NotNull MicroserviceConfig cfg) {
    this.URL = "jdbc:postgresql://"
      + cfg.getHost() + ":" + cfg.getPort() + "/" + cfg.getExtraConfigs().get("database");

    this.props = new Properties();
    props.setProperty("user", cfg.getExtraConfigs().get("user"));
    props.setProperty("password", cfg.getExtraConfigs().get("password"));
    try {
      this.conn = DriverManager.getConnection(this.URL, props);
      System.out.println("Connected to database - " + this.URL);
    } catch (SQLException e) {
      System.out.println("Could not connect to database - " + e.getMessage());
    }
  }

  public String getURL() {
    return URL;
  }

  public Properties getProps() {
    return props;
  }

  public Connection getConn() {
    return conn;
  }

  public void setConn(Connection conn) {
    this.conn = conn;
  }
}
