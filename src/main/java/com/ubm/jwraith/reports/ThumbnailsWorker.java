package com.ubm.jwraith.reports;

import java.awt.Graphics2D;
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

  public ThumbnailsWorker(BlockingQueue<PageReport> pendingPages, int thumbnailWidth, int thumbnailHeight) {
    this.pendingPages = pendingPages;
    this.thumbnailWidth = thumbnailWidth;
    this.thumbnailHeight = thumbnailHeight;
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
      String thumbnailPath = WebsiteThumbnails.calculateThumbnailPath(imagePath);
      File thumbnailFile = new File(thumbnailPath);
      
      // Check folder exits
      File thumbnailFileFolder = thumbnailFile.getParentFile();
      if (!thumbnailFileFolder.exists()) {
	thumbnailFileFolder.mkdirs();
      }
      
      BufferedImage image = ImageIO.read(new File(imagePath));
      
      // Crop image.
      float thumbnailAspectRatio = (float) thumbnailWidth / thumbnailHeight;
      int imageWidth = image.getWidth();      
      int croppedImageHeight = Math.round(imageWidth / thumbnailAspectRatio);
      BufferedImage croppedImage = image.getSubimage(0, 0, imageWidth, croppedImageHeight);
 
      // Write and rezide on thumbnail.
      float imageAspectRation = (float) imageWidth / image.getHeight();
      int subImageHeight = Math.round(thumbnailWidth / imageAspectRation);
      BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_ARGB);
 
      Graphics2D g = thumbnail.createGraphics();
      WebsiteThumbnails.setGraphics2dSettings(g);
      g.drawImage(croppedImage, 0, 0, thumbnailWidth, subImageHeight, null);
      g.dispose();
      
      ImageIO.write(thumbnail, "png", new File(thumbnailPath));
    } catch (IOException ex) {
      Logger.getLogger(ThumbnailsWorker.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
