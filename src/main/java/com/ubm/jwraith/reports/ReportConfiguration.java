package com.ubm.jwraith.reports;

/**
 *
 * @author xavier
 */
public class ReportConfiguration {
  
  private int threshold = 15;
  private int thumbnailWidth = 300;
  private int thumbnailHeight = 300;
  private String template = null;
  
  public boolean isCustomTemplated() {
    return template != null;
  }

  public int getThreshold() {
    return threshold;
  }

  public void setThreshold(int threshold) {
    this.threshold = threshold;
  }

  public int getThumbnailWidth() {
    return thumbnailWidth;
  }

  public void setThumbnailWidth(int thumbnailWidth) {
    this.thumbnailWidth = thumbnailWidth;
  }

  public int getThumbnailHeight() {
    return thumbnailHeight;
  }

  public void setThumbnailHeight(int thumbnailHeight) {
    this.thumbnailHeight = thumbnailHeight;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }
  
}
