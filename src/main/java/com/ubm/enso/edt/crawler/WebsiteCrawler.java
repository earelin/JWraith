package com.ubm.enso.edt.crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 *
 * @author Xavier Carriba
 */
public class WebsiteCrawler {

  private Queue<String> checkedUrls = new LinkedList<>();
  private Queue<String> pendingUrls = new LinkedList<>();
  private String basePath;
  WebDriver driver = new HtmlUnitDriver();
  
  public WebsiteCrawler(String basePath) {
    this.basePath = basePath;
  }
  
  public Queue<String> process() {
    pendingUrls.add("/");
    do {
      String path = pendingUrls.poll();
      processUrl(path);
    } while (!pendingUrls.isEmpty());
    
    FileWriter fw = null;
    try {
      fw = new FileWriter("urls.txt");
      for (String url : checkedUrls) {
	fw.write(url + "\n");
      }
      fw.close();
    } catch (IOException ex) {
      Logger.getLogger(WebsiteCrawler.class.getName()).log(Level.SEVERE, null, ex);
    }
    return checkedUrls;
  }
  
  public void processUrl(String path) {
    driver.get(basePath + path);
    List<WebElement> elements = driver.findElements(By.tagName("a"));
    
    checkedUrls.add(path);    
    
    for (WebElement element : elements) {
      String url = element.getAttribute("href");
      if (url != null && url.startsWith(basePath)) {
	int hashIndex = url.indexOf('#');
	
	if (hashIndex != -1) {
	  url = url.substring(0, hashIndex);
	}
	
	url = url.substring(basePath.length());
	
	if (!(checkedUrls.contains(url) || pendingUrls.contains(url))) {
	  pendingUrls.add(url);
	  System.out.println(url);
	}
      }
    }
  }
  
}
