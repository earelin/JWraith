package com.ubm.jwraith.reports;

import java.util.Comparator;

/**
 *
 * @author Xavier Carriba
 */
public class DisplayReport implements Comparable<DisplayReport> {
  
  private int width = 0;
  private String baseDomainScreenshot;
  private String compareDomainScreenshot;
  private String diffImage;
  private float diffValue = 0;

  public DisplayReport() {}
  
  public DisplayReport(int width, String baseDomainScreenshot, String compareDomainScreenshot, String diffImage, float diffValue) {
    this.width = width;
    this.baseDomainScreenshot = baseDomainScreenshot;
    this.compareDomainScreenshot = compareDomainScreenshot;
    this.diffImage = diffImage;
    this.diffValue = diffValue;
  }
  
  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public String getBaseDomainScreenshot() {
    return baseDomainScreenshot;
  }

  public void setBaseDomainScreenshot(String baseDomainScreenshot) {
    this.baseDomainScreenshot = baseDomainScreenshot;
  }

  public String getCompareDomainScreenshot() {
    return compareDomainScreenshot;
  }

  public void setCompareDomainScreenshot(String compareDomainScreenshot) {
    this.compareDomainScreenshot = compareDomainScreenshot;
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
  
  public static class Comparators {
    public static final Comparator<DisplayReport> WIDTH =
	  (DisplayReport o1, DisplayReport o2)
		  -> Integer.compare(o1.getWidth(), o2.getWidth());
  }
}
