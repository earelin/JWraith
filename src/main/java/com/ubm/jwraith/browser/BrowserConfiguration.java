/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubm.jwraith.browser;

/**
 *
 * @author xavier
 */
public class BrowserConfiguration {
  
  private String label = null;
  private String name = "htmlunit";
  private String driverExecutable = null;
  private String version = null;
  private String remoteAddress = null;
  
  public boolean isRemote() {
    return remoteAddress != null;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDriverExecutable() {
    return driverExecutable;
  }

  public void setDriverExecutable(String driverExecutable) {
    this.driverExecutable = driverExecutable;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getRemoteAddress() {
    return remoteAddress;
  }

  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }
  
}
