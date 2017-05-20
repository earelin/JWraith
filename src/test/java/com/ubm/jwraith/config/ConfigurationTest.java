package com.ubm.jwraith.config;

import java.io.FileNotFoundException;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author xavier
 */
public class ConfigurationTest {
  
  @BeforeClass
  public static void loadConfigurationData() throws ConfigurationFileException, FileNotFoundException {
    Configuration configuration = Configuration.getInstance();
    configuration.read("src/test/resources/configuration.yml");
  }
  
  @Test
  public void loadBrowser() {
    Configuration configuration = Configuration.getInstance();
    assertEquals(configuration.getBrowser(), "phantomjs");
  }
  
  @Test
  public void loadDirectory() {
    Configuration configuration = Configuration.getInstance();
    assertEquals(configuration.getDirectory(), "shots");
  }
}
