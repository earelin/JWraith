package com.ubm.jwraith.reports;

import com.ubm.jwraith.config.Domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Xavier Carriba
 */
class ReportResult {
  
  private Date date;
  private List<PageResult> pages = new ArrayList<>();
  private Domain baseDomain;
  private Domain compareDomain;

  public void addPageResult(PageResult result) {
    pages.add(result);
  }
  
  public List<PageResult> getPageResult() {
    pages.sort(PageResult.Comparators.DIFF);
    return pages;
  }

  public List<PageResult> getPages() {
    return pages;
  }
  
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Domain getBaseDomain() {
    return baseDomain;
  }

  public void setBaseDomain(Domain baseDomain) {
    this.baseDomain = baseDomain;
  }

  public Domain getCompareDomain() {
    return compareDomain;
  }

  public void setCompareDomain(Domain compareDomain) {
    this.compareDomain = compareDomain;
  }
  
}
