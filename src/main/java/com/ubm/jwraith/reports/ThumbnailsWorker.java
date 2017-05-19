package com.ubm.jwraith.reports;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Xavier Carriba
 */
public class ThumbnailsWorker implements Runnable {
  
  private final BlockingQueue<PageReport> pendingPages;
  private final int thumbnailWidth;
  private final int thumbnailHeight;
  private final String basePath;

  public ThumbnailsWorker(BlockingQueue<PageReport> pendingPages, int thumbnailWidth, int thumbnailHeight, String basePath) {
    this.pendingPages = pendingPages;
    this.thumbnailWidth = thumbnailWidth;
    this.thumbnailHeight = thumbnailHeight;
    this.basePath = basePath;
  }
  
  @Override
  public void run() {
    while(!pendingPages.isEmpty()) {
      try {
	PageReport page = pendingPages.take();
	generatePageThumbnails(page);
      } catch (InterruptedException ex) {
	Logger.getLogger(ThumbnailsWorker.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  private void generatePageThumbnails(PageReport page) {
    for (DisplayReport display : page.getDisplays()) {
      generateThumbnail(display.getBaseDomainScreenshot());
      generateThumbnail(display.getCompareDomainScreenshot());
      generateThumbnail(display.getDiffImage());
    }
  }
  
  private void generateThumbnail(String imagePath) {
    try {
      String thumbnailPath = WebsiteThumbnails.calculateThumbnailPath(basePath, imagePath);
      File thumbnailFile = new File(thumbnailPath);
      
      // Check folder exits
      File thumbnailFileFolder = thumbnailFile.getParentFile();
      if (!thumbnailFileFolder.exists()) {
	thumbnailFileFolder.mkdirs();
      }
      
      BufferedImage image = ImageIO.read(new File(imagePath));
      
      // Crop image.
      int imageWidth = image.getWidth();      
      int croppedImageHeight = Math.round(imageWidth * ((float) thumbnailHeight / thumbnailWidth));
      BufferedImage croppedImage = image.getSubimage(0, 0, imageWidth, croppedImageHeight);
// 
      // Write and rezide on thumbnail.
      BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_ARGB);
 
      Graphics2D g = thumbnail.createGraphics();
      WebsiteThumbnails.setGraphics2dSettings(g);
//      g.drawImage(croppedImage, 0, 0, thumbnailWidth, croppedImageHeight, null);
      AffineTransform at = AffineTransform.getScaleInstance((float) thumbnailWidth / imageWidth,
	      (float) thumbnailHeight / croppedImageHeight);
      g.drawRenderedImage(croppedImage, at);
      g.dispose();
      
      ImageIO.write(thumbnail, "png", new File(thumbnailPath));
    } catch (IOException ex) {
      Logger.getLogger(ThumbnailsWorker.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
