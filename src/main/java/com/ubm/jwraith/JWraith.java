package com.ubm.enso.edt;

import com.ubm.enso.edt.config.ConfigurationFileException;
import com.ubm.enso.edt.config.Configuration;
import com.ubm.enso.edt.reports.WebsiteReportGenerator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author Xavier Carriba
 */
public class EnsoDummyTester {
  
  public static void main(String[] args) {
    String configurationFilePath = "configuration.yml";
    String mode = "compare";
    
//    switch (args.length) {
//      case 2:
//	configurationFilePath = args[1];
//      case 1:
//	mode = args[0];
//	break;
//      default:
//	System.out.println(" Syntax: enso_dummy_tester [mode] [configuration file path]");
//	return;
//    }
    
    Configuration configuration = new Configuration();
    try {
      configuration.read(configurationFilePath);
    } catch (ConfigurationFileException ex) {
      Logger.getLogger(EnsoDummyTester.class.getName()).log(Level.SEVERE, null, ex);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(EnsoDummyTester.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    // Get link list
//    WebsiteCrawler crawler = new WebsiteCrawler(configuration.getBaseUrls().get(0));
//    Queue<String> paths = crawler.process();
    
    Queue<String> paths = new LinkedList<>();
    
    try (Stream<String> stream = Files.lines(Paths.get("urls.txt"))) {
      String[] urls = stream.toArray(String[]::new);
      for (String url : urls) {
	paths.add(url);
      }
    } catch (IOException ex) {
      Logger.getLogger(EnsoDummyTester.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    // Capture screenshots
//    WebsiteScreenshots screenshots = new WebsiteScreenshots(configuration.getWebsites().get(0), configuration.getDisplays(), paths);
//    screenshots.process();
//    
//    if (mode.equals("compare")) {
//      paths = new LinkedList<>();
//    
//      try (Stream<String> stream = Files.lines(Paths.get("urls.txt"))) {
//	String[] urls = stream.toArray(String[]::new);
//	for (String url : urls) {
//	  paths.add(url);
//	}
//      } catch (IOException ex) {
//	Logger.getLogger(EnsoDummyTester.class.getName()).log(Level.SEVERE, null, ex);
//      }
//      
//      screenshots.setWebsite(configuration.getWebsites().get(1));
//      screenshots.setPaths(paths);
//      screenshots.process();
//    }
	    
    // Process Diff files
//    if (mode.equals("compare")) {
//      ScreenshotsDiff diff = new ScreenshotsDiff(configuration.getWebsites(), configuration.getDisplays(), paths);
//      diff.process();
//    }
    
    WebsiteReportGenerator reportGenerator = new WebsiteReportGenerator(configuration.getWebsites(), configuration.getDisplays(), paths);
    reportGenerator.process();
  }
  
}
