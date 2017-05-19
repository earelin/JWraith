package com.ubm.jwraith.screenshots;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 *
 * @author Xavier Carriba
 */
public class ScreenshotsWorker implements Runnable  {
  
  private final String domain;
  private final String domainLabel;
  private final String folder;
  private final BlockingQueue<String> pendingUrls;
  private final List<Integer> screenWidths;
  private final DesiredCapabilities caps;
  private WebDriver driver;
  
  public ScreenshotsWorker(String domain,
	  String domainLabel,
	  String folder,
	  List<Integer> screenWidths,
	  BlockingQueue<String> pendingUrls) {
    
    this.domain = domain;
    this.domainLabel = domainLabel;
    this.folder = folder;
    this.pendingUrls = pendingUrls;
    this.screenWidths = screenWidths;
    
    caps = DesiredCapabilities.firefox();
//    caps = new DesiredCapabilities();
//    caps.setJavascriptEnabled(true);                
//    caps.setCapability("takesScreenshot", true);  
//    caps.setCapability(
//      PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
//      "/usr/local/bin/phantomjs");
//    ArrayList<String> cliArgsCap = new ArrayList<String>();
//    cliArgsCap.add("--web-security=false");
//    cliArgsCap.add("--webdriver-loglevel=DEBUG");
//    caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
//    caps.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");
//    driver = new PhantomJSDriver(caps);
    driver = new FirefoxDriver(caps);
  }
  
  @Override
  public void run() {
    try {
      crawl();
    } catch (InterruptedException ex) {
      Logger.getLogger(ScreenshotsWorker.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void crawl() throws InterruptedException {
    int i = 0;
    while(!pendingUrls.isEmpty()) {      
      String url = pendingUrls.take();
      processUrl(url);
      if (i == 25) {
	driver.quit();
	driver = new FirefoxDriver(caps);
	i = 0;
      }
      else {
	i++;
      }
    }
    driver.quit();
  }
  
  public void processUrl(String path) {
    String pathFolder = WebsiteScreenshots.generatePathFolderName(path);
    File f = new File(folder + "/" + pathFolder);
    if(!f.exists()) { 
      f.mkdirs();
    }
    
    for (int screenWidth : screenWidths) {
      driver.manage().window().setSize(new Dimension(screenWidth, screenWidth / (16 / 9)));
      driver.get(domain + path);
      
      Long screenHeight = (Long) ((JavascriptExecutor) driver).executeScript("return Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
      driver.manage().window().setSize(new Dimension(screenWidth, screenHeight.intValue()));
      
      String fileName = WebsiteScreenshots.generateFileName(screenWidth, domainLabel);
      File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
      try {
	FileUtils.copyFile(scrFile, new File(folder + "/" + pathFolder + "/" + fileName));
      } catch (IOException ex) {
	Logger.getLogger(WebsiteScreenshots.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
}
