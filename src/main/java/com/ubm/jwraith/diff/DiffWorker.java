package com.ubm.jwraith.diff;

import com.ubm.jwraith.reports.DisplayReport;
import com.ubm.jwraith.reports.PageReport;
import com.ubm.jwraith.screenshots.WebsiteScreenshots;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Xavier Carriba
 */
public class DiffWorker implements Runnable {
  
  private final String baseFolder;
  private final String compareFolder;
  private final List<Integer> screenWidths;
  private final BlockingQueue<String> pendingPaths;
  private final BlockingQueue<PageReport> pageReports;

  public DiffWorker(
	  String baseFolder,
	  String compareFolder,
	  List<Integer> screenWidths,
	  BlockingQueue<PageReport> pageReports,
	  BlockingQueue<String> pendingPaths) {
    
    this.baseFolder = baseFolder;
    this.compareFolder = compareFolder;
    this.screenWidths = screenWidths;
    this.pendingPaths = pendingPaths;
    this.pageReports = pageReports;
  }
  
  @Override
  public void run() {
    while(!pendingPaths.isEmpty()) {
      try {
	String path = pendingPaths.take();
	processImagesDiff(path);
      } catch (InterruptedException ex) {
	Logger.getLogger(DiffWorker.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  public void processImagesDiff(String path) throws InterruptedException {
    String pathBaseFolder = baseFolder + "/" + WebsiteScreenshots.generatePathFolderName(path);
    String pathCompareFolder = compareFolder + "/" + WebsiteScreenshots.generatePathFolderName(path);
    
    PageReport pageReport = new PageReport(path);
    
    for (int screenWidth : screenWidths) {
      try {
	DisplayReport displayReport = new DisplayReport();
	displayReport.setWidth(screenWidth);
	
	String baseFile = pathBaseFolder + "/" + WebsiteScreenshots.generateFileName(screenWidth, "base");	
	BufferedImage baseImage = ImageIO.read(new File(baseFile));
	displayReport.setBaseDomainScreenshot(baseFile);
	
	String compareFile = pathCompareFolder + "/" + WebsiteScreenshots.generateFileName(screenWidth, "compare");	
	BufferedImage compareImage = ImageIO.read(new File(compareFile));
	displayReport.setCompareDomainScreenshot(compareFile);
	
	final int highlight = Color.BLUE.getRGB();
	final int width = baseImage.getWidth() >= compareImage.getWidth()
		? baseImage.getWidth() : compareImage.getWidth();	
	final int height = baseImage.getHeight() >= compareImage.getHeight()
		? baseImage.getHeight() : compareImage.getHeight();
	final int[] p1 = baseImage.getRGB(0, 0, baseImage.getWidth(), baseImage.getHeight(), null, 0, baseImage.getWidth());
	final int[] p2 = compareImage.getRGB(0, 0, compareImage.getWidth(), compareImage.getHeight(), null, 0, compareImage.getWidth());
	
	BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	
	int[] diffP = new int[width * height]; 

	// compare img1 to img2, pixel by pixel. If different, highlight out pixel...
	int diffCount = 0;
	for (int i = 0; i < height; i++) {
	  for (int j = 0; j < width; j++) {
	    if (baseImage.getWidth() <= j || compareImage.getWidth() <= j
		    || baseImage.getHeight() <= i || compareImage.getHeight() <= i
		    || p1[i * baseImage.getWidth() + j] != p2[i * compareImage.getWidth() + j]) {
	      diffP[i * width + j] = highlight;
	      ++diffCount;
	    } 
	    else {
	      Color color = new Color(p1[i * baseImage.getWidth() + j]);
	      Color transparent = new Color(color.getRed(), color.getGreen(), color.getBlue(), 175);
	      diffP[i * width + j] = transparent.getRGB();
	    }
	  }
	}

	diffImage.setRGB(0, 0, width, height, diffP, 0, width);
	float diffRatio = (float) diffCount / (width * height);
	displayReport.setDiffValue(diffRatio);
	
	String diffFileName = pathCompareFolder + "/" + WebsiteScreenshots.generateFileName(screenWidth, "diff");	
	ImageIO.write(diffImage, "png", new File(diffFileName));
	displayReport.setDiffImage(diffFileName);
	pageReport.addDisplayResult(displayReport);
      } catch (Exception ex) {
	Logger.getLogger(WebsiteScreenshots.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    pageReports.put(pageReport);
  }

}
