package com.ubm.jwraith.crawler;

import com.ubm.jwraith.config.Configuration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xavier Carriba
 */
public class WebsiteCrawler {
  
  private static WebsiteCrawler instance;

  private final Configuration configuration = Configuration.getInstance();
  private final ConcurrentMap<String, String> checkedUrls = new ConcurrentHashMap<>();
  private final BlockingQueue<String> pendingUrls = new LinkedBlockingQueue<>();
  
  public static WebsiteCrawler getInstance() {
    if (instance == null) {
      instance = new WebsiteCrawler();
    }
    return instance;
  }
  
  private WebsiteCrawler() {}
  
  public List<String> crawl() {
    String domain = configuration.getBaseDomain();
    
    pendingUrls.add("/");
    
    Thread[] workers = new Thread[configuration.getWorkers()];
    for (int i = 0; i < configuration.getWorkers(); i++) {
      workers[i] = new Thread(new CrawlerWorker(domain, configuration.getSpiderSkips(), checkedUrls, pendingUrls));
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
    
    List<String> urls = new ArrayList<>(checkedUrls.keySet());
    Collections.sort(urls);
    
    return urls;
  }
  
}
