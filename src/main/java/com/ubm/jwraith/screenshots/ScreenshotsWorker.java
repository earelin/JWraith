package com.ubm.jwraith.screenshots;

import com.ubm.jwraith.browser.BrowserConfiguration;
import com.ubm.jwraith.browser.SeleniumFactory;
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
  private final SeleniumFactory seleniumFactory = SeleniumFactory.getInstance();
  private final WebDriver driver;
  private final BrowserConfiguration browserConfiguration;
  
  public ScreenshotsWorker(BrowserConfiguration browserConfiguration,
	  String domain,
	  String domainLabel,
	  String folder,
	  List<Integer> screenWidths,
	  BlockingQueue<String> pendingUrls) {
    
    this.browserConfiguration = browserConfiguration;
    this.domain = domain;
    this.domainLabel = domainLabel;
    this.folder = folder;
    this.pendingUrls = pendingUrls;
    this.screenWidths = screenWidths;
    
    driver = seleniumFactory.getDriver(this.browserConfiguration);
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
    while(!pendingUrls.isEmpty()) {      
      String url = pendingUrls.take();
      processUrl(url);
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
      
      if (browserConfiguration.getName() != "phantomjs") {
        Long screenHeight = (Long) ((JavascriptExecutor) driver)
          .executeScript("return Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
        driver.manage().window().setSize(new Dimension(screenWidth, screenHeight.intValue()));
      }
      
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
