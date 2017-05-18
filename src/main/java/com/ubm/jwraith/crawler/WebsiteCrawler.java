package com.ubm.jwraith.crawler;

import com.ubm.jwraith.config.Configuration;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xavier Carriba
 */
public class WebsiteCrawler {

  private Configuration configuration = Configuration.getInstance();
  private BlockingQueue<String> checkedUrls = new LinkedBlockingQueue<>();
  private BlockingQueue<String> pendingUrls = new LinkedBlockingQueue<>();
  private String domain;
  private Thread[] workers;
  
  public WebsiteCrawler(String domain) {
    this.domain = domain;
  }
  
  public List<String> process() {
    pendingUrls.add("/");
    
    workers = new Thread[configuration.getWorkers()];
    for (int i = 0; i < configuration.getWorkers(); i++) {
      workers[i] = new Thread(new CrawlerWorker(domain, configuration.getSpiderSkips(), checkedUrls, pendingUrls));
      workers[i].start();
    }
    
    boolean workersAlive = false;
    do {
      try {
	workers[0].join(1000);
      } catch (InterruptedException ex) {
	Logger.getLogger(WebsiteCrawler.class.getName()).log(Level.SEVERE, null, ex);
      }

      workersAlive = false;
      System.out.println("----- Checking workers status -----");
      for (Thread worker : workers) {
	if (worker.isAlive()) {
	  workersAlive = true;
	  break;
	}
      }
    } while (workersAlive);
    
    FileWriter fw = null;
    System.out.println("Writing " + pendingUrls.size() + " urls");
    try {
      fw = new FileWriter("urls.txt");
      for (String url : checkedUrls) {
	fw.write(url + "\n");
      }
      fw.close();
    } catch (IOException ex) {
      Logger.getLogger(WebsiteCrawler.class.getName()).log(Level.SEVERE, null, ex);
    }
    return new ArrayList(checkedUrls);
  }
  
//  public void processUrl(String path) {
//    driver.get(domain + path);
//    List<WebElement> elements = driver.findElements(By.tagName("a"));
//    
//    checkedUrls.add(path);    
//    
//    for (WebElement element : elements) {
//      String url = element.getAttribute("href");
//      if (url != null && url.startsWith(domain)) {
//	int hashIndex = url.indexOf('#');
//	
//	if (hashIndex != -1) {
//	  url = url.substring(0, hashIndex);
//	}
//	
//	url = url.substring(domain.length());
//	
//	if (!(checkedUrls.contains(url) || pendingUrls.contains(url))) {
//	  pendingUrls.add(url);
//	  System.out.println(url);
//	}
//      }
//    }
//  }
  
}
