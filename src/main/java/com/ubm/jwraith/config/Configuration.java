package com.ubm.enso.edt.config;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.Dimension;

/**
 *
 * @author Xavier Carriba
 */
public class Configuration {

  private List<Website> websites = new ArrayList<>();
  private List<Dimension> displays = new ArrayList<>();
  private int workers = 1;
  private String pathsFile = null;
  
  public void read(String filePath) throws ConfigurationFileException, FileNotFoundException {
    YamlReader reader = new YamlReader(new FileReader(filePath));
    
    Object object;
    try {
      object = reader.read();
    } catch (YamlException ex) {
      throw new ConfigurationFileException("Systax error on configuration file: " + ex.getMessage());
    }
    
    Map configMap = (Map)object;
    
    if (configMap.containsKey("paths_file")) {
      pathsFile = (String) configMap.get("paths_file");
    }
    
    if (configMap.containsKey("workers")) {
      workers = Integer.parseInt((String) configMap.get("workers"));
    }
    
    if (configMap.containsKey("base_url")) {
      Map urls = (Map) configMap.get("base_url");
      for (Object label : urls.keySet()) {
	String url = (String) urls.get(label);
	websites.add(new Website((String) label, url));
      }
    } else {
      throw new ConfigurationFileException("Missing base_url key on configuration file.");
    }
    
    if (configMap.containsKey("displays")) {
      List configDisplays = (List) configMap.get("displays");
      for (Object display : configDisplays) {
	String[] dimensions = ((String) display).split("x");
	int width = Integer.parseInt(dimensions[0]);
	int height = Integer.parseInt(dimensions[1]);
	displays.add(new Dimension(width, height));
      }
    } else {
      throw new ConfigurationFileException("Missing displays key on configuration file.");
    }
    
  }

  public int getWorkers() {
    return workers;
  }

  public void setWorkers(int workers) {
    this.workers = workers;
  }

  public List<Website> getWebsites() {
    return websites;
  }

  public void setWebsites(List<Website> baseUrls) {
    this.websites = baseUrls;
  }

  public String getPathsFile() {
    return pathsFile;
  }

  public void setPathsFile(String pathsFile) {
    this.pathsFile = pathsFile;
  }

  public List<Dimension> getDisplays() {
    return displays;
  }

  public void setDisplays(List<Dimension> displays) {
    this.displays = displays;
  }
  
}
