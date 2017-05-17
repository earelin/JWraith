package com.ubm.enso.edt.reports;

import com.ubm.enso.edt.config.Website;
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
  private Website website1;
  private Website website2;

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

  public Website getWebsite1() {
    return website1;
  }

  public void setWebsite1(Website website1) {
    this.website1 = website1;
  }

  public Website getWebsite2() {
    return website2;
  }

  public void setWebsite2(Website website2) {
    this.website2 = website2;
  }
  
}
