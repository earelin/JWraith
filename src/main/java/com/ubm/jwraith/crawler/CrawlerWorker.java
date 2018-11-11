package com.ubm.jwraith.crawler;

import com.ubm.jwraith.browser.BrowserConfiguration;
import com.ubm.jwraith.browser.SeleniumFactory;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Xavier Carriba
 */
public class CrawlerWorker implements Runnable {
  
  private final SeleniumFactory seleniumFactory = SeleniumFactory.getInstance();
  private WebDriver driver;
  private final ConcurrentMap<String, String> checkedUrls;
  private final BlockingQueue<String> pendingUrls;
  private final String domain;
  private final List<String> spiderSkips;

  public CrawlerWorker(String domain, List<String> spiderSkips, ConcurrentMap<String, String> checkedUrls, BlockingQueue<String> pendingUrls) {
    this.domain = domain;
    this.spiderSkips = spiderSkips;
    this.checkedUrls = checkedUrls;
    this.pendingUrls = pendingUrls;
    this.driver = seleniumFactory.getDriver(new BrowserConfiguration());
  }
  
  @Override
  public void run() {
    try {
      crawl();
    } catch (InterruptedException ex) {
      Logger.getLogger(CrawlerWorker.class.getName()).log(Level.SEVERE, null, ex);
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
          driver = seleniumFactory.getDriver(new BrowserConfiguration());
          i = 0;
        }
        else {
          i++;
        }
      }
    } while(waitingTime < 30);
    driver.quit();
  }
  
  public void processUrl(String path) throws InterruptedException {
    
    driver.get(domain + path);
    List<WebElement> elements = driver.findElements(By.tagName("a"));
    
    System.out.println("Cheking: " + path);
    checkedUrls.putIfAbsent(path, path); 
    
    for (WebElement element : elements) {
      String url = element.getAttribute("href");
      if (url != null && url.startsWith(domain)) {
        int hashIndex = url.indexOf('#');
	
        if (hashIndex != -1) {
          url = url.substring(0, hashIndex);
        }

        url = url.substring(domain.length());

        boolean skip = false;
        for (String skipUrls : spiderSkips) {
          if (url.matches(skipUrls)) {
            skip = true;
            break;
          }
        }		

        if (!(skip || checkedUrls.containsKey(url) || pendingUrls.contains(url))) {
          pendingUrls.add(url);
          System.out.println("Added: " + url);
        }
      }
    }
  }
  
}
