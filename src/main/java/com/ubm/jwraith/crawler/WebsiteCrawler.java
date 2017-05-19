package com.ubm.jwraith.crawler;

import com.ubm.jwraith.config.Configuration;
import java.io.FileWriter;
import java.io.IOException;
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

  private final Configuration configuration = Configuration.getInstance();
  private final ConcurrentMap<String, String> checkedUrls = new ConcurrentHashMap<>();
  private final BlockingQueue<String> pendingUrls = new LinkedBlockingQueue<>();
  private final String domain;
  
  public WebsiteCrawler(String domain) {
    this.domain = domain;
  }
  
  public List<String> process() {
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
    
    List<String> urls = new ArrayList(checkedUrls.keySet());
    Collections.sort(urls);
    
    FileWriter fw = null;
    try {
      fw = new FileWriter(configuration.getSpiderFile());
      for (String url : urls) {
	fw.write(url + "\n");
      }
    } catch (IOException ex) {
      Logger.getLogger(WebsiteCrawler.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (fw != null) {
	try {
	  fw.close();
	} catch (IOException ex) {
	  Logger.getLogger(WebsiteCrawler.class.getName()).log(Level.SEVERE, null, ex);
	}
      }
    }
    return urls;
  }
  
}
