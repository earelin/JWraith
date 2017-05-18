package com.ubm.jwraith.reports;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Xavier Carriba
 */
public class PageReport implements Comparable<PageReport> {
  private String title;
  private String path;
  private List<DisplayReport> displays = new ArrayList<>();

  public PageReport(String path) {
    this.path = path;
  }
  
  public float getAverangeDiff() {
    float sumDiff = 0;
    int i;
    for (i = 0; i < displays.size(); i++) {
      DisplayReport displayResult = displays.get(i);
      sumDiff += displayResult.getDiffValue();
    }
    return i == 0 ? 0 : sumDiff / i;
  }
  
  public void addDisplayResult(DisplayReport result) {
    displays.add(result);    
  }

  public List<DisplayReport> getDisplays() {
    displays.sort(DisplayReport.Comparators.WIDTH);
    return displays;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public int compareTo(PageReport o) {
    return Comparators.DIFF.compare(this, o);
  }
  
  public static class Comparators {
    public static final Comparator<PageReport> DIFF =
	  (PageReport o1, PageReport o2)
		  -> Float.compare(o1.getAverangeDiff(), o2.getAverangeDiff());
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
}
