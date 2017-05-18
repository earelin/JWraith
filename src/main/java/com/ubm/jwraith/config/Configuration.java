package com.ubm.jwraith.config;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Stores configuration.
 * @author Xavier Carriba
 */
public class Configuration {

  private static Configuration instance;

  private String baseDomain;
  private String compareDomain;
  private final List<Integer> screenWidths = new ArrayList<>();
  private int workers = 1;
  private String browser = "phantomjs";
  private String directory = "shots";
  private Map<String, String> paths;
  private int fuzz = 0;
  private String historyDir = "shots_history";
  private String mode;
  private List<String> spiderSkips;
  private String spiderFile;
  private int thumbnailWidth = 300;
  private int thumbnailHeight = 200;

  public static Configuration getInstance() {
    if (instance == null) {
      instance = new Configuration();
    }
    return instance;
  }
  
  private Configuration() {};

  public void read(String filePath) throws ConfigurationFileException, FileNotFoundException {
    YamlReader reader = new YamlReader(new FileReader(filePath));

    Object object;
    try {
      object = reader.read();
    } catch (YamlException ex) {
      throw new ConfigurationFileException("Systax error on configuration file: " + ex.getMessage());
    }

    Map configMap = (Map)object;

    if (configMap.containsKey("directory")) {
      directory = (String) configMap.get("directory");
    }

    if (configMap.containsKey("domains")) {
      Map domainsData = (Map) configMap.get("domains");
      
      if (domainsData.containsKey("base")) {
	baseDomain = (String) domainsData.get("base");
      } 
      else {
	throw new ConfigurationFileException("Missing base domain key on configuration file.");
      }
    
      if (domainsData.containsKey("compare")) {
	compareDomain = (String) domainsData.get("compare");
      }

    } else {
      throw new ConfigurationFileException("Missing domains key on configuration file.");
    }

    if (configMap.containsKey("workers")) {
      workers = Integer.parseInt((String) configMap.get("workers"));
    }

    if (configMap.containsKey("screen_widths")) {
      List configDisplays = (List) configMap.get("screen_widths");
      for (Object display : configDisplays) {
	screenWidths.add((Integer.parseInt((String) display)));
      }
    } else {
      throw new ConfigurationFileException("Missing displays key on configuration file.");
    }
    
    if (configMap.containsKey("spider_file")) {
      spiderFile = (String) configMap.get("spider_file");
    }
    
    if (configMap.containsKey("spider_skips")) {
      spiderSkips = (List) configMap.get("spider_skips");
    }
    
  }

  public String getBrowser() {
    return browser;
  }

  public String getDirectory() {
    return directory;
  }

  public int getFuzz() {
    return fuzz;
  }

  public String getHistoryDir() {
    return historyDir;
  }

  public int getWorkers() {
    return workers;
  }

  public List<Integer> getScreenWidths() {
    return screenWidths;
  }

  public String getBaseDomain() {
    return baseDomain;
  }

  public String getCompareDomain() {
    return compareDomain;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public List<String> getSpiderSkips() {
    return spiderSkips;
  }

  public String getSpiderFile() {
    return spiderFile;
  }

  public int getThumbnailWidth() {
    return thumbnailWidth;
  }

  public int getThumbnailHeight() {
    return thumbnailHeight;
  }

}
