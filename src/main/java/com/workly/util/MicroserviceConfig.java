/*
 * file: src/java/com/workly/util/MicroserviceConfig.java
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch
 * date: 12 August, 2022
 */

package com.workly.util;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MicroserviceConfig {
  @SerializedName("name") private String name;
  @SerializedName("host") private String host;
  @SerializedName("port") private String port;
  @SerializedName("extra_configs") private Map<String, String> extraConfigs;

  public MicroserviceConfig(String name, String host, String port, Map<String, String> extraConfigs) {
    this.name = name;
    this.host = host;
    this.port = port;
    this.extraConfigs = extraConfigs;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public Map<String, String> getExtraConfigs() {
    return extraConfigs;
  }

  public void setExtraConfigs(Map<String, String> extraConfigs) {
    this.extraConfigs = extraConfigs;
  }

  @Override
  public String toString() {
    return "MicroserviceConfig{" +
      "\nname='" + name + '\'' +
      "\n, host='" + host + '\'' +
      "\n, port='" + port + '\'' +
      "\n, extraConfigs=" + extraConfigs +
      "\n}";
  }
}
