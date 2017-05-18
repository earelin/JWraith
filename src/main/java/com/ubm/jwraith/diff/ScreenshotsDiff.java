package com.ubm.jwraith.diff;

import com.ubm.jwraith.config.Domain;
import com.ubm.jwraith.screenshots.WebsiteScreenshots;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.openqa.selenium.Dimension;

/**
 *
 * @author Xavier Carriba
 */
public class ScreenshotsDiff {
    
  private final List<Domain> websites;
  private final List<Dimension> dimensions;
  private Queue<String> paths;
  
  public ScreenshotsDiff(List<Domain> websites, List<Dimension> dimensions, Queue<String> paths) {
    this.websites = websites;
    this.dimensions = dimensions;
    this.paths = paths;
  }
  
  public void process() {
    // Generate screenshots
    do {
      String path = paths.poll();
      processImages(path);
    } while (!paths.isEmpty()); 
  }
  
  public void processImages(String path) {
    String folder = "shots/" + WebsiteScreenshots.generateFolderName(path);
    
    for (Dimension dimension : dimensions) {
      try {
	String fileName1 = folder + "/" + dimension.width + "x" + dimension.height + "_" + websites.get(0).getLabel() + ".png";
	BufferedImage img1 = ImageIO.read(new File(fileName1));
	String fileName2 = folder + "/" + dimension.width + "x" + dimension.height + "_" + websites.get(1).getLabel() + ".png";
	BufferedImage img2 = ImageIO.read(new File(fileName2));			
	
	final int highlight = Color.BLUE.getRGB();
	final int width = img1.getWidth() >= img2.getWidth()
		? img1.getWidth() : img2.getWidth();	
	final int height = img1.getHeight() >= img2.getHeight()
		? img1.getHeight() : img2.getHeight();
	final int[] p1 = img1.getRGB(0, 0, img1.getWidth(), img1.getHeight(), null, 0, img1.getWidth());
	final int[] p2 = img2.getRGB(0, 0, img2.getWidth(), img2.getHeight(), null, 0, img2.getWidth());
	
	BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	
	int[] diffP = new int[width * height]; 

	// compare img1 to img2, pixel by pixel. If different, highlight out pixel...
	int diffCount = 0;
	for (int i = 0; i < height; i++) {
	  for (int j = 0; j < width; j++) {
	    if (img1.getWidth() <= j || img2.getWidth() <= j
		    || img1.getHeight() <= i || img2.getHeight() <= i
		    || p1[i * img1.getWidth() + j] != p2[i * img2.getWidth() + j]) {
	      diffP[i * width + j] = highlight;
	      ++diffCount;
	    } 
	    else {
	      Color color = new Color(p1[i * img1.getWidth() + j]);
	      Color transparent = new Color(color.getRed(), color.getGreen(), color.getBlue(), 51);
	      diffP[i * width + j] = transparent.getRGB();
	    }
	  }
	}

	diffImage.setRGB(0, 0, width, height, diffP, 0, width);
	float diffRatio = (float) diffCount / (width * height);
	
	String diffFileName = folder + "/" + dimension.width + "x" + dimension.height + "_diff.png";
	ImageIO.write(diffImage, "png", new File(diffFileName));
	
	String diffDataFileName = folder + "/" + dimension.width + "x" + dimension.height + "_diff_ratio.txt";
	PrintWriter writer = new PrintWriter(diffDataFileName);
	writer.println(diffRatio);
	writer.close();
	
	
      } catch (Exception ex) {
	Logger.getLogger(WebsiteScreenshots.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
