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
  private Domain website1;
  private Domain website2;

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

  public Domain getWebsite1() {
    return website1;
  }

  public void setWebsite1(Domain website1) {
    this.website1 = website1;
  }

  public Domain getWebsite2() {
    return website2;
  }

  public void setWebsite2(Domain website2) {
    this.website2 = website2;
  }
  
}
