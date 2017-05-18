package com.ubm.enso.edt.reports;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Xavier Carriba
 */
class PageResult implements Comparable<PageResult> {
  private String path;
  private List<DisplayResult> displays = new ArrayList<>();

  public PageResult(String path) {
    this.path = path;
  }
  
  public float getAverangeDiff() {
    float sumDiff = 0;
    int i;
    for (i = 0; i < displays.size(); i++) {
      DisplayResult displayResult = displays.get(i);
      sumDiff += displayResult.getDiffValue();
    }
    return i == 0 ? 0 : sumDiff / i;
  }
  
  public void addDisplayResult(DisplayResult result) {
    displays.add(result);    
  }

  public List<DisplayResult> getDisplays() {
    displays.sort(DisplayResult.Comparators.WIDTH);
    return displays;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public int compareTo(PageResult o) {
    return Comparators.DIFF.compare(this, o);
  }
  
  public static class Comparators {
    public static final Comparator<PageResult> DIFF =
	  (PageResult o1, PageResult o2)
		  -> Float.compare(o1.getAverangeDiff(), o2.getAverangeDiff());
  }
}
