package com.ubm.jwraith.diff;

import com.ubm.jwraith.config.Configuration;
import com.ubm.jwraith.reports.PageReport;
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
public class ScreenshotsDiff {

  private Configuration configuration = Configuration.getInstance();
  
  private static ScreenshotsDiff instance;
  
  public static ScreenshotsDiff getInstance() {
    if (instance == null) {
      instance = new ScreenshotsDiff();
    }
    return instance;
  }
  
  private ScreenshotsDiff() {}
  
  public List<PageReport> process(String baseFolder, String compareFolder, List<String> paths) {
    BlockingQueue<String> pendingPaths = new LinkedBlockingQueue<>(paths);
    BlockingQueue<PageReport> pageReports = new LinkedBlockingQueue<>();
    
    // Generate diff threads
    Thread[] workers = new Thread[configuration.getWorkers()];
    for (int i = 0; i < configuration.getWorkers(); i++) {
      workers[i] = new Thread(new DiffWorker(baseFolder, compareFolder, configuration.getScreenWidths(), pageReports, pendingPaths));
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
	    Logger.getLogger(ScreenshotsDiff.class.getName()).log(Level.SEVERE, null, ex);
	  }
	  break;
	}
      }
    } while (workersAlive);
    
    return new ArrayList<>(pageReports);
  }
  
}
