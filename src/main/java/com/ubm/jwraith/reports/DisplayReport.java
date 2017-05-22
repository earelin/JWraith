package com.ubm.jwraith.reports;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Xavier Carriba
 */
public class DisplayReport implements Comparable<DisplayReport> {
  
  private int width = 0;
  private Map<String, String> screenshots = new HashMap<>();
  private String diffImage;
  private float diffValue = 0;

  public DisplayReport() {}
  
  public DisplayReport(int width, Map<String, String> screenshots, String diffImage, float diffValue) {
    this.width = width;
    this.screenshots = screenshots;
    this.diffImage = diffImage;
    this.diffValue = diffValue;
  }
  
  public void putScreenshot(String type, String fileName) {
    screenshots.put(type, fileName);
  }
  
  public String getScreenshot(String type) {
    return screenshots.get(type);
  }
  
  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public Map<String, String> getScreenshots() {
    return screenshots;
  }

  public void setScreenshots(Map<String, String> screenshots) {
    this.screenshots = screenshots;
  }

  public String getDiffImage() {
    return diffImage;
  }

  public void setDiffImage(String diffImage) {
    this.diffImage = diffImage;
  }

  public float getDiffValue() {
    return diffValue;
  }

  public void setDiffValue(float diffValue) {
    this.diffValue = diffValue;
  }

  @Override
  public int compareTo(DisplayReport o) {
    return Comparators.WIDTH.compare(this, o);
  }

  @Override
  public boolean equals(Object obj) {
    return obj.getClass().equals(DisplayReport.class)
	    && ((DisplayReport)obj).getWidth() == this.width;
  }
  
  public static class Comparators {
    public static final Comparator<DisplayReport> WIDTH =
	  (DisplayReport o1, DisplayReport o2)
		  -> Integer.compare(o1.getWidth(), o2.getWidth());
  }
}
