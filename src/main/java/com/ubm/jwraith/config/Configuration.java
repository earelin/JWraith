package com.ubm.jwraith.config;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.ubm.jwraith.reports.ReportConfiguration;
import com.ubm.jwraith.selenium.BrowserConfiguration;
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

  private BrowserConfiguration defaultBrowser;
  private String baseDomain = null;
  private String compareDomain = null;
  private final List<Integer> screenWidths = new ArrayList<>();
  private int workers = 1;  
  private String directory = "shots";
  private String historyDirectory = "shots_history";
  private String mode;
  private List<String> spiderSkips = null;
  private String pathsFile = null;
  private List<BrowserConfiguration> multipleBrowsers = null;
  private ReportConfiguration report = new ReportConfiguration();

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

    Map<String, Object> configMap = (Map<String, Object>) object;
    
    defaultBrowser = parseBrowserConfiguration(configMap);

    if (configMap.containsKey("directory")) {
      directory = (String) configMap.get("directory");
    }
    
    if (configMap.containsKey("history_directory")) {
      historyDirectory = (String) configMap.get("history_directory");
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
    
    if (configMap.containsKey("multiple_browsers")) {
      List<Map<String, Object>> multipleBrowsersRaw = (List<Map<String, Object>>) configMap.get("multiple_browsers");
      if (multipleBrowsersRaw.size() > 0) {
	multipleBrowsers = new ArrayList<>(); 
      }
      for (Map<String, Object> browserConfig : multipleBrowsersRaw) {
	multipleBrowsers.add(parseBrowserConfiguration(browserConfig));
      }
    }
    
    if (configMap.containsKey("reports")) {
      parseReportConfiguration((Map<String, Object>) configMap.get("reports"));
    }
    
  }
  
  private void parseReportConfiguration(Map<String, Object> configMap) {
    if (configMap.containsKey("threshold")) {
      report.setThreshold(Integer.parseInt((String) configMap.get("threshold")));
    }
    
    if (configMap.containsKey("thumbnail_width")) {
      report.setThreshold(Integer.parseInt((String) configMap.get("thumbnail_width")));
    }
    
    if (configMap.containsKey("thumbnail_height")) {
      report.setThreshold(Integer.parseInt((String) configMap.get("thumbnail_height")));
    }
  }
  
  private BrowserConfiguration parseBrowserConfiguration(Map<String, Object> configMap) {
    BrowserConfiguration browser = new BrowserConfiguration();
    
    if (configMap.containsKey("browser_label")) {
      browser.setLabel((String) configMap.get("browser_label"));
    }
    
    if (configMap.containsKey("browser_name")) {
      browser.setName((String) configMap.get("browser_name"));
    }
            
    if (configMap.containsKey("driver_executable")) {
      browser.setDriverExecutable((String) configMap.get("driver_executable"));
    }
    
    if (configMap.containsKey("browser_version")) {
      browser.setDriverExecutable((String) configMap.get("browser_version"));
    }
    
    return browser;
  }

  public BrowserConfiguration getDefaultBrowser() {
    return defaultBrowser;
  }
  
  public String getDirectory() {
    return directory;
  }

  public String getHistoryDirectory() {
    return historyDirectory;
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

  public List<BrowserConfiguration> getMultipleBrowsers() {
    return multipleBrowsers;
  }

  public ReportConfiguration getReport() {
    return report;
  }

}
