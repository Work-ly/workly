/*
 * file: src/main/java/com/workly/App.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 12 August, 2022
 */

package com.workly;

import com.workly.handler.FirebaseHandler;
import com.workly.util.Config;
import org.apache.log4j.PropertyConfigurator;

public class App
{
  public static void main(String[] args)
  {
    PropertyConfigurator.configure("log4j.properties");
    
    Config cfg = new Config("config.json");
    DBServer dbSrv = new DBServer(cfg.getData().getAPIs()[0]);
    FirebaseHandler fbHndlr = new FirebaseHandler(cfg.getData().getAPIs()[1]);
    new Server(cfg.getData().getServer(), fbHndlr, dbSrv);
  }
}
