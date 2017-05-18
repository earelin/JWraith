package com.ubm.jwraith.screenshots;

import com.ubm.jwraith.config.Domain;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
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
public class WebsiteScreenshots {
  
  private Domain website;
  private List<Dimension> displays;
  private Queue<String> paths;
  private WebDriver driver;
  
  public WebsiteScreenshots(Domain website, List<Dimension> displays, Queue<String> paths) {
    this.website = website;
    this.paths = paths;
    this.displays = displays;
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setJavascriptEnabled(true);                
    caps.setCapability("takesScreenshot", true);  
    caps.setCapability(
                        PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                        "/usr/local/bin/phantomjs"
                    );
    driver = new PhantomJSDriver(caps);
  }
  
  public void process() {
    // Check folder
    File f = new File("shots");
    if(!f.exists()) { 
      f.mkdir();
    }
    
    // Generate screenshots
    do {
      String path = paths.poll();
      processUrl(path);
    } while (!paths.isEmpty());    
  }
  
  public void processUrl(String path) {
    String folderName = generateFolderName(path);
    File f = new File("shots/" + folderName);
    if(!f.exists()) { 
      f.mkdir();
    }
    
    for (Dimension dimension : displays) {
      driver.manage().window().setSize(dimension);
      driver.get(website.getUrl() + path);            
      
      String fileName = dimension.width + "x" + dimension.height + "_" + website.getLabel() + ".png";
      File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
      try {
	FileUtils.copyFile(scrFile, new File("shots/" + folderName + "/" + fileName));
      } catch (IOException ex) {
	Logger.getLogger(WebsiteScreenshots.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  public static String generateFolderName(String path) {
    String folderName = "";
    if (path.equals("/")) {
      folderName = "home";
    }
    else {
      folderName = path.replace("/", "__");
    }
    return folderName;
  }

  public void setWebsite(Domain website) {
    this.website = website;
  }

  public void setPaths(Queue<String> paths) {
    this.paths = paths;
  }
  
}
