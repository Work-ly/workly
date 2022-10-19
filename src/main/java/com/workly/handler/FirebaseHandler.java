/*
 * file: src/main/java/com/workly/handler/FirebaseHandler.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 14 August, 2022
 */

package com.workly.handler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.Gson;
import com.workly.model.FirebaseLookupUser;
import com.workly.model.FirebaseUser;
import com.workly.model.User;
import com.workly.util.Message;
import com.workly.util.MicroserviceConfig;

public class FirebaseHandler {
  public Gson gson = new Gson();
  public MicroserviceConfig fbCfg;

  public FirebaseHandler(MicroserviceConfig fbCfg) {
    this.fbCfg = fbCfg;
  }

  public Message fetch(String uri, String method, String reqBody) {
    String fbResp;
    try {
      URL url = new URL(
        this.fbCfg.getHost()
          + uri
          + "?key="
          + this.fbCfg.getExtraConfigs().get("api_key")
      );
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod(method);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");

      if (method == "POST") {
        OutputStream fbReqOutStream = conn.getOutputStream();
        OutputStreamWriter fbReqOutWriter = new OutputStreamWriter(fbReqOutStream, "UTF-8");
        fbReqOutWriter.write(reqBody);
        fbReqOutWriter.flush();
        fbReqOutWriter.close();
      }

      conn.connect();

      BufferedInputStream bufIn;
      if (conn.getResponseCode() != HttpStatus.OK_200) {
        bufIn = new BufferedInputStream(conn.getErrorStream());
      } else {
        bufIn = new BufferedInputStream(conn.getInputStream());
      }

      ByteArrayOutputStream bufOut = new ByteArrayOutputStream();
      int cur;
      while ((cur = bufIn.read()) != -1) {
        bufOut.write((byte)cur);
      }
      fbResp = bufOut.toString();

      if (conn.getResponseCode() != HttpStatus.OK_200) {
        return new Message("ERROR", fbResp);
      }
    } catch (Exception e) {
      return new Message("ERROR", e.getMessage());
    }

    return new Message("INFO", fbResp);
  }

  public FirebaseUser create(FirebaseUser fbUser, User user) {
    String reqBody = gson.toJson(user, User.class);

    Message status = this.fetch("/accounts:signUp", "POST", reqBody);
    if (status.getType() != "INFO") {
      System.out.println("Could not create user on firebase - " + status.getMsg());

      return null;
    }

    fbUser = gson.fromJson(status.getMsg(), FirebaseUser.class);
    if (status.getType() != "INFO") {
      System.out.println("Could not create user on firebase - " + status.getMsg());

      return null;
    }

    return fbUser;
  }

  public boolean delete(FirebaseUser fbUser) {
    String reqBody = "{ \"idToken\": \"" + fbUser.getIdToken() + "\" }";

    Message status = this.fetch("/accounts:delete", "POST", reqBody);
    if (status.getType() != "INFO") {
      System.out.println("Could not delete user on firebase - " + status.getMsg());

      return false;
    }

    return true;
  }

  public FirebaseUser auth(String token) {
    if (token == null) {
      System.out.println("Could not authorize - Missing authorization header");

      return null;
    }

    String reqBody = "{ \"idToken\": \"" + token + "\" }";
    Message status = this.fetch("/accounts:lookup", "POST", reqBody);
    if (status.getType() != "INFO") {
      System.out.println("Could not authorize - " + status.getMsg());

      return null;
    }

    FirebaseLookupUser fbUsrData = this.gson.fromJson(status.getMsg(), FirebaseLookupUser.class);

    return fbUsrData.getUsers()[0];
  }
}
