package com.ubm.jwraith.selenium;

import com.ubm.jwraith.config.Configuration;
import org.openqa.selenium.WebDriver;

/**
 * 
 * @author xavier
 */
public class SeleniumFactory {
  
  private static SeleniumFactory instance;
  
  private final Configuration configuration = Configuration.getInstance();
  
  public static SeleniumFactory getInstance() {
    if (instance == null) {
      instance = new SeleniumFactory();
    }
    return instance;
  }
  
  private SeleniumFactory() {}
  
  public WebDriver getDriver() {
    WebDriver driver = null;
    
    return driver;
  }

}
