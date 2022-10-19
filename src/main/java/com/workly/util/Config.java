/*
 * file: src/java/com/workly/util/Config.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch
 * date: 12 August, 2022
 */

package com.workly.util;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.util.Scanner;

public class Config {
  public class Data {
    @SerializedName("server")
    private MicroserviceConfig server;
    @SerializedName("apis")
    private MicroserviceConfig[] APIs;

    public Data() {
    }

    public MicroserviceConfig getServer() {
      return server;
    }

    public void setServer(MicroserviceConfig server) {
      this.server = server;
    }

    public MicroserviceConfig[] getAPIs() {
      return APIs;
    }

    public void setAPIs(MicroserviceConfig[] APIs) {
      this.APIs = APIs;
    }
  }

  private Data data;
  private String rawCfg;

  public Config(String fileName) {
    try {
      Scanner in = new Scanner(new FileReader(fileName));
      StringBuilder strBldr = new StringBuilder();
      while (in.hasNext()) {
        strBldr.append(in.next());
      }
      in.close();
      this.rawCfg = strBldr.toString();
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }

    Gson gson = new Gson();
    this.data = gson.fromJson(this.rawCfg, Data.class);
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public String getRawCfg() {
    return rawCfg;
  }

  public void setRawCfg(String rawCfg) {
    this.rawCfg = rawCfg;
  }
}
