package com.ubm.jwraith.browser;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author xavier
 */
public class BrowserConfigurationTest {
  
  @Test
  public void defaultBrowserPhantomJs() {
    BrowserConfiguration bc = new BrowserConfiguration();
    assertEquals("htmlunit", bc.getName());
  }
  
  @Test
  public void isRemote() {
    BrowserConfiguration bc = new BrowserConfiguration();
    bc.setRemoteAddress("http://www.example.com");
    assertTrue(bc.isRemote());
  }
  
  @Test
  public void isNotRemote() {
    BrowserConfiguration bc = new BrowserConfiguration();    
    assertFalse(bc.isRemote());
  }
  
}
