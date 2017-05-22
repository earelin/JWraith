package com.ubm.jwraith.screenshots;

import com.ubm.jwraith.browser.BrowserConfiguration;
import com.ubm.jwraith.config.Configuration;
import com.ubm.jwraith.crawler.WebsiteCrawler;
import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xavier Carriba
 */
public class WebsiteScreenshots {
  
  private static WebsiteScreenshots instance;
  
  private final Configuration configuration = Configuration.getInstance();
  
  public static WebsiteScreenshots getInstance() {
    if (instance == null) {
      instance = new WebsiteScreenshots();
    }
    return instance;
  }
  
  private WebsiteScreenshots() {}
  
  public void process(BrowserConfiguration browserConfiguration, String domain, String domainLabel, String folder, List<String> paths) {
    // Check folder
    File f = new File(folder);
    if(!f.exists()) { 
      f.mkdir();
    }
    
    BlockingQueue<String> pendingUrls = new LinkedBlockingQueue<>(paths);
    
    // Generate screenshots threads
    Thread[] workers = new Thread[configuration.getWorkers()];
    for (int i = 0; i < configuration.getWorkers(); i++) {
      workers[i] = new Thread(new ScreenshotsWorker(browserConfiguration, domain, domainLabel, folder, configuration.getScreenWidths(), pendingUrls));
      workers[i].start();
    }
    
    boolean workersAlive;
    do {
      workersAlive = false;
      for (Thread worker : workers) {
	if (worker.isAlive()) {
	  workersAlive = true;
	  try {
	    worker.join(100);
	  } catch (InterruptedException ex) {
	    Logger.getLogger(WebsiteCrawler.class.getName()).log(Level.SEVERE, null, ex);
	  }
	  break;
	}
      }
    } while (workersAlive);
  }
  
  public static String generatePathFolderName(String path) {
    String folderName = "";
    if (path.equals("/")) {
      folderName = "home";
    }
    else {
      folderName = path.replace("/", "__").replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    return folderName;
  }
  
  public static String generateFileName(BrowserConfiguration bc, int screenWidth, String domainLabel) {
    return screenWidth + "_" + bc.getName() + "_" + domainLabel + ".png";
  }
  
  public static String generateFileName(int screenWidth, String domainLabel) {
    return screenWidth + "_" + domainLabel + ".png";
  }
  
}
