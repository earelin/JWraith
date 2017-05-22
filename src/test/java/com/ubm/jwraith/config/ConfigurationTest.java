package com.ubm.jwraith.config;

import com.ubm.jwraith.browser.BrowserConfiguration;
import com.ubm.jwraith.reports.ReportConfiguration;
import java.io.FileNotFoundException;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author xavier
 */
public class ConfigurationTest {
  
  private static Configuration configuration;
  
  @BeforeClass
  public static void loadConfigurationData() throws ConfigurationFileException, FileNotFoundException {
    configuration = Configuration.getInstance();    
  }
  
  @Test(expected=FileNotFoundException.class)
  public void configurationFileNotFound() throws FileNotFoundException, ConfigurationFileException {    
    configuration.read("erroneus_filename");
  }

  @Test
  public void loadedBaseDomain() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals("file://src/test/resources/webs/website1", configuration.getBaseDomain());
  }
  
  @Test
  public void loadedCompareDomain() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals("file://src/test/resources/webs/website2", configuration.getCompareDomain());
  }
  
  @Test
  public void loadedDirectory() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals("custom_shots", configuration.getDirectory());
  }
  
  @Test
  public void loadedHistoryDirectory() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals("custom_history_shots", configuration.getHistoryDirectory());
  }
  
  @Test
  public void loadedWorkers() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals(4, configuration.getWorkers());
  }
  
  @Test
  public void loadedPathsFile() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals("paths_simple.txt", configuration.getPathsFile());
  }
  
  @Test
  public void loadedReportConfiguration() throws ConfigurationFileException, FileNotFoundException  {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    ReportConfiguration reportConfiguration = configuration.getReport();
    assertEquals(15, reportConfiguration.getThreshold());
    assertEquals(300, reportConfiguration.getThumbnailWidth());
    assertEquals(200, reportConfiguration.getThumbnailHeight());
    assertEquals("report.html", reportConfiguration.getTemplate());
  }
  
  @Test
  public void loadedMultipleBrowsersConfiguration() throws ConfigurationFileException, FileNotFoundException  {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    List<BrowserConfiguration> browsers = configuration.getMultipleBrowsers();
    assertEquals(2, browsers.size());
  }

}
