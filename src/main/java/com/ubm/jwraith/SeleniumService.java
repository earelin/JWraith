package com.ubm.jwraith;

/**
 * 
 * @author xavier
 */
public class SeleniumService {
  
  private static SeleniumService instance;
  
  public static SeleniumService getInstance() {
    if (instance == null) {
      instance = new SeleniumService();
    }
    return instance;
  }
  
  private SeleniumService() {}
  
  
  
}
