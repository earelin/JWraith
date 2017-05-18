package com.ubm.enso.edt.crawler;

import java.util.Collection;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 *
 * @author Xavier Carriba
 */
public class CrawlerWorker implements Runnable {
  
  private Collection<String> checkedUrls;
  private Collection<String> urls;
  private String baseUrl;
  private String path;
  
  public CrawlerWorker(String path, String baseUrl, Collection<String> urls,  Collection<String> checkedUrls) {
    this.path = path;
    this.baseUrl = baseUrl;
    this.urls = urls;
    this.checkedUrls = checkedUrls;
  }

  @Override
  public void run() {
    crawl();
  }
  
  public void crawl() {
    WebDriver driver = new HtmlUnitDriver();
    driver.get(baseUrl + path);
    List<WebElement> elements = driver.findElements(By.tagName("a"));
    
    for (WebElement element : elements) {
      String url = element.getAttribute("href");
      if (url != null && url.startsWith(baseUrl)) {
	System.out.println(url);
      }
    }
    
    driver.quit();		   
  }
  
}
