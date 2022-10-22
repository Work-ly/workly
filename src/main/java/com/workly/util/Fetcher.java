/*
 * file: src/main/java/com/workly/util/Fetcher.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: 26 September, 2022
 */

package com.workly.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.eclipse.jetty.http.HttpStatus;

public class Fetcher {
  private String host;
  
  public Fetcher(String host) {
    this.host = ("".equals(host)) ? "http://0.0.0.0:8008" : host;
  }
  
  public Message fetch(String method, String uri, String reqBody) {
    String resp;
    try {
      URL url = new URL(this.host + uri);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod(method);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");

      if (method == "POST") {
        OutputStream reqOutStream = conn.getOutputStream();
        OutputStreamWriter reqOutWriter = new OutputStreamWriter(reqOutStream, "UTF-8");
        reqOutWriter.write(reqBody);
        reqOutWriter.flush();
        reqOutWriter.close();
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
      resp = bufOut.toString();

      if (conn.getResponseCode() != HttpStatus.OK_200) {
        return new Message("ERROR", resp);
      }
    } catch (Exception e) {
      return new Message("ERROR", e.getMessage());
    }

    return new Message("INFO", resp); 
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }
}
