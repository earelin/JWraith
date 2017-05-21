package com.ubm.jwraith.config;

import java.io.FileNotFoundException;
import java.net.URL;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author xavier
 */
public class ConfigurationTest {
  
  public static Configuration configuration;
  
  @BeforeClass
  public static void loadConfigurationData() throws ConfigurationFileException, FileNotFoundException {
    configuration = Configuration.getInstance();    
  }
  
  @Test(expected=FileNotFoundException.class)
  public void configurationFileNotFound() throws FileNotFoundException, ConfigurationFileException {    
    configuration.read("erroneus_filename");
  }
  
  @Test
  public void loadedDirectory() throws ConfigurationFileException, FileNotFoundException {
    String configurationFile = getClass().getClassLoader().getResource("configuration.yml").getPath();
    configuration.read(configurationFile);
    assertEquals(configuration.getDirectory(), "custom_shots");
  }
  
  
}
