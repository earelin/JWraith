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
    assertEquals(configuration.getBaseDomain(), "file://src/test/resources/webs/website1");
  }
  
  @Test
  public void loadedCompareDomain() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals(configuration.getCompareDomain(), "file://src/test/resources/webs/website2");
  }
  
  @Test
  public void loadedDirectory() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals(configuration.getDirectory(), "custom_shots");
  }
  
  @Test
  public void loadedHistoryDirectory() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals(configuration.getHistoryDirectory(), "custom_history_shots");
  }
  
  @Test
  public void loadedWorkers() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals(configuration.getWorkers(), 4);
  }
  
  @Test
  public void loadedPathsFile() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals(configuration.getPathsFile(), "paths_simple.txt");
  }

}
