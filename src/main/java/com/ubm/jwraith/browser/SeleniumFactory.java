package com.ubm.jwraith.browser;

import com.ubm.jwraith.config.Configuration;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

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
    return getDriver(configuration.getDefaultBrowser());
  }
  
  public WebDriver getDriver(BrowserConfiguration browserConfiguration) {
    WebDriver driver = null;
    
    DesiredCapabilities dc = generateDesiredCapabilities(browserConfiguration);
    
    if (browserConfiguration.isRemote()) {
      try {
        URL remoteAddress = new URL(browserConfiguration.getRemoteAddress());
        WebDriver remoteDrive = new RemoteWebDriver(remoteAddress, dc);
        driver = new Augmenter().augment(remoteDrive);
      } catch (MalformedURLException ex) {
        Logger.getLogger(SeleniumFactory.class.getName()).log(Level.SEVERE, null, ex);
      }      
    } 
    else {
      switch (browserConfiguration.getName()) {
        case "chrome":
          driver = new ChromeDriver(dc);
          break;
        case "firefox":
          driver = new FirefoxDriver(dc);
          break;
        case "phantomjs":
          driver = new PhantomJSDriver(dc);
          break;
        default:
          driver = new HtmlUnitDriver();
      }
    }
    
    if (driver != null) {
      driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
    }
    
    return driver;
  }
  
  private DesiredCapabilities generateDesiredCapabilities(BrowserConfiguration browserConfiguration) {
    DesiredCapabilities dc = null;
    
    switch (browserConfiguration.getName()) {
      case "chrome":
        dc = DesiredCapabilities.chrome();
        break;
      case "firefox":
        dc = DesiredCapabilities.firefox();
        break;
      case "phantomjs":
        dc = DesiredCapabilities.phantomjs();
        dc.setCapability(
        PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
        browserConfiguration.getDriverExecutable());
        break;
      default:
        dc = new DesiredCapabilities();
    }
    
    dc.setJavascriptEnabled(true);                
    dc.setCapability("takesScreenshot", true);
    
    return dc;
  }

}
