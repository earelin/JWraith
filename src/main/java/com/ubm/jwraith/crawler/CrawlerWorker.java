package com.ubm.jwraith.crawler;

import static java.lang.Thread.sleep;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
public class CrawlerWorker implements Runnable {
  
  WebDriver driver = new HtmlUnitDriver();
  
  private BlockingQueue<String> checkedUrls;
  private BlockingQueue<String> pendingUrls;
  private String domain;
  private List<String> spiderSkips;

  public CrawlerWorker(String domain, List<String> spiderSkips, BlockingQueue<String> checkedUrls, BlockingQueue<String> pendingUrls) {
    this.domain = domain;
    this.spiderSkips = spiderSkips;
    this.checkedUrls = checkedUrls;
    this.pendingUrls = pendingUrls;
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
	sleep(100);
	waitingTime++;	
	System.out.println("Waiting");
      } else {
	String url = pendingUrls.take();
	System.out.println("Pending: " + pendingUrls.size());
	processUrl(url);
	if (i == 50) {
	  driver.close();
	  driver = new HtmlUnitDriver();
	  i = 0;
	}
	else {
	  i++;
	}
      }
    } while(waitingTime < 50);    
  }
  
  public void processUrl(String path) throws InterruptedException {
    driver.get(domain + path);
    List<WebElement> elements = driver.findElements(By.tagName("a"));
    
    System.out.println("Cheking: " + path);
    checkedUrls.put(path);    
    
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
	
	if (!(skip || checkedUrls.contains(url) || pendingUrls.contains(url))) {
	  pendingUrls.add(url);
	  System.out.println("Added: " + url);
	}
      }
    }
  }
  
}
