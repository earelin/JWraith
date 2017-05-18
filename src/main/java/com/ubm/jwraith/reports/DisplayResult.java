package com.ubm.jwraith.reports;

import java.util.Comparator;

/**
 *
 * @author Xavier Carriba
 */
class DisplayResult implements Comparable<DisplayResult> {
  
  private int width = 0;
  private String screenshotWebsite1;
  private String screenshotWebsite2;
  private String diffImage;
  private float diffValue = 0;

  public DisplayResult() {}
  
  public DisplayResult(String screenshotWebsite1, String screenshotWebsite2, String diffImage) {
    this.screenshotWebsite1 = screenshotWebsite1;
    this.screenshotWebsite2 = screenshotWebsite2;
    this.diffImage = diffImage;
  }
  
  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public String getScreenshotWebsite1() {
    return screenshotWebsite1;
  }

  public void setScreenshotWebsite1(String screenshotWebsite1) {
    this.screenshotWebsite1 = screenshotWebsite1;
  }

  public String getScreenshotWebsite2() {
    return screenshotWebsite2;
  }

  public void setScreenshotWebsite2(String screenshotWebsite2) {
    this.screenshotWebsite2 = screenshotWebsite2;
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
  public int compareTo(DisplayResult o) {
    return Comparators.WIDTH.compare(this, o);
  }
  
  public static class Comparators {
    public static final Comparator<DisplayResult> WIDTH =
	  (DisplayResult o1, DisplayResult o2)
		  -> Integer.compare(o1.getWidth(), o2.getWidth());
  }
}
