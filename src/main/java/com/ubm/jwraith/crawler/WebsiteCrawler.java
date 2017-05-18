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
    
    for (int i = 0; i < configuration.getWorkers(); i++) {
      Thread worker = new Thread(new CrawlerWorker(domain, configuration.getSpiderSkips(), checkedUrls, pendingUrls));
      worker.start();
    }
    
    int i = 0;
    do {
      try {
	sleep(100);
      } catch (InterruptedException ex) {
	Logger.getLogger(WebsiteCrawler.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      if (i == 20) {
	System.out.println("Pending queue size: " + pendingUrls.size());
	i = 0;
      }
      else {      
	i++;
      }
      
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
