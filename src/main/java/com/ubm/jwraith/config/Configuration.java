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
  private int fuzz = 0;
  private String historyDir = "shots_history";
  private String mode;
  private List<String> spiderSkips;
  private String pathsFile;
  private int thumbnailWidth = 300;
  private int thumbnailHeight = 300;
  private int threshold = 15;

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

    Map<String, Object> configMap = (Map) object;

    if (configMap.containsKey("directory")) {
      directory = (String) configMap.get("directory");
    }

    if (configMap.containsKey("domains")) {
      Map<String, String> domainsData = (Map) configMap.get("domains");
      
      if (domainsData.containsKey("base")) {
	baseDomain = domainsData.get("base");
      } 
      else {
	throw new ConfigurationFileException("Missing base domain key on configuration file.");
      }
    
      if (domainsData.containsKey("compare")) {
	compareDomain = domainsData.get("compare");
      }

    } else {
      throw new ConfigurationFileException("Missing domains key on configuration file.");
    }

    if (configMap.containsKey("workers")) {
      workers = Integer.parseInt((String) configMap.get("workers"));
    }

    if (configMap.containsKey("screen_widths")) {
      List<String> configDisplays = (List<String>) configMap.get("screen_widths");
      for (String display : configDisplays) {
	screenWidths.add((Integer.parseInt(display)));
      }
    } else {
      throw new ConfigurationFileException("Missing displays key on configuration file.");
    }
    
    if (configMap.containsKey("paths_file")) {
      pathsFile = (String) configMap.get("paths_file");
    }
    
    if (configMap.containsKey("spider_skips")) {
      spiderSkips = (List<String>) configMap.get("spider_skips");
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

  public String getPathsFile() {
    return pathsFile;
  }

  public int getThumbnailWidth() {
    return thumbnailWidth;
  }

  public int getThumbnailHeight() {
    return thumbnailHeight;
  }

  public int getThreshold() {
    return threshold;
  }

}
