package com.ubm.jwraith.screenshots;

import com.ubm.jwraith.crawler.CrawlerWorker;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
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
  private List<Integer> screenWidths;
  private DesiredCapabilities caps;
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
    
    caps = new DesiredCapabilities();
    caps.setJavascriptEnabled(true);                
    caps.setCapability("takesScreenshot", true);  
    caps.setCapability(
      PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
      "/usr/local/bin/phantomjs"
    );
    driver = new PhantomJSDriver(caps);
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
    int waitingTime = 0;
    do {
      if (pendingUrls.isEmpty()) {	
	Thread.sleep(100);
	waitingTime++;	
      } else {
	String url = pendingUrls.take();
	processUrl(url);
	if (i == 50) {
	  driver.quit();
	  driver = new PhantomJSDriver(caps);
	  i = 0;
	}
	else {
	  i++;
	}
      }
    } while(waitingTime < 30);
    driver.quit();
  }
  
  public void processUrl(String path) {
    String pathFolder = WebsiteScreenshots.generatePathFolderName(path);
    File f = new File(folder + "/" + pathFolder);
    if(!f.exists()) { 
      f.mkdir();
    }
    
    for (int screenWidth : screenWidths) {
      driver.manage().window().setSize(new Dimension(screenWidth, (int) screenWidth * (16 / 9)));
      driver.get(domain + path);            
      
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
