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
    
    boolean workersAlive = false;
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
    
    FileWriter fw = null;
    System.out.println("Writing " + checkedUrls.size() + " urls");
    try {
      fw = new FileWriter(configuration.getSpiderFile());
      for (String url : checkedUrls) {
	fw.write(url + "\n");
      }
      fw.close();
    } catch (IOException ex) {
      Logger.getLogger(WebsiteCrawler.class.getName()).log(Level.SEVERE, null, ex);
    }
    return new ArrayList(checkedUrls);
  }
  
}
