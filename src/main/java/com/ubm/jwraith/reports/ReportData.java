package com.ubm.jwraith.reports;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Xavier Carriba
 */
public class ReportData {
  
  private Date date;
  private List<PageReport> pages = new ArrayList<>();
  private String baseDomain;
  private String compareDomain;

  public void addPageResult(PageReport result) {
    pages.add(result);
  }
  
  public List<PageReport> getPageResult() {
    pages.sort(PageReport.Comparators.DIFF_DESC);
    return pages;
  }

  public List<PageReport> getPages() {
    return pages;
  }

  public void setPages(List<PageReport> pages) {
    this.pages = pages;
  }
  
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getBaseDomain() {
    return baseDomain;
  }

  public void setBaseDomain(String baseDomain) {
    this.baseDomain = baseDomain;
  }

  public String getCompareDomain() {
    return compareDomain;
  }

  public void setCompareDomain(String compareDomain) {
    this.compareDomain = compareDomain;
  }
  
}
