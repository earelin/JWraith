package com.ubm.jwraith.reports;

import com.ubm.jwraith.config.Configuration;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xavier Carriba
 */
public class WebsiteThumbnails {
  
  private static WebsiteThumbnails instance;
  
  private final Configuration configuration = Configuration.getInstance();

  private WebsiteThumbnails() {}
  
  public static WebsiteThumbnails getInstance() {
    if (instance == null) {
      instance = new WebsiteThumbnails();
    }
    return instance;
  }
  
  public void process(ReportData report) {
    ReportConfiguration reportConfiguration = configuration.getReport();
    BlockingQueue<PageReport> pendingPages = new LinkedBlockingQueue<>(report.getPageResult());
    
    // Generate diff threads
    Thread[] workers = new Thread[configuration.getWorkers()];
    for (int i = 0; i < configuration.getWorkers(); i++) {
      workers[i] = new Thread(new ThumbnailsWorker(pendingPages, reportConfiguration.getThumbnailWidth(), reportConfiguration.getThumbnailHeight(), configuration.getDirectory()));
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
	    Logger.getLogger(WebsiteThumbnails.class.getName()).log(Level.SEVERE, null, ex);
	  }
	  break;
	}
      }
    } while (workersAlive);
  }

  public static String calculateThumbnailPath(String basePath, String imagePath) {
    return basePath + "/thumbnails/" + imagePath;
  }
  
  public static void setGraphics2dSettings(Graphics2D g) {
    g.setRenderingHint(RenderingHints.KEY_RENDERING,
	    RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    RenderingHints.VALUE_ANTIALIAS_ON);        
    g.setRenderingHint(RenderingHints.KEY_DITHERING,
	    RenderingHints.VALUE_DITHER_ENABLE);
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
	    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
	    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
	    RenderingHints.VALUE_COLOR_RENDER_QUALITY);        
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
	    RenderingHints.VALUE_STROKE_NORMALIZE);
  }

}
