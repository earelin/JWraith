package com.ubm.jwraith.reports;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Xavier Carriba
 */
public class ReportConfigurationTest {
  
  @Test
  public void isNotCustomTemplate() {
    ReportConfiguration rc = new ReportConfiguration();
    assertFalse(rc.isCustomTemplated());
  }
  
  @Test
  public void isCustomTemplate() {
    ReportConfiguration rc = new ReportConfiguration();
    rc.setTemplate("custom_template.html");
    assertTrue(rc.isCustomTemplated());
  }
  
}
