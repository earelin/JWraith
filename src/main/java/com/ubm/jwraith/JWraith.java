package com.ubm.jwraith;

import com.ubm.jwraith.config.ConfigurationFileException;
import com.ubm.jwraith.config.Configuration;
import com.ubm.jwraith.crawler.WebsiteCrawler;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xavier Carriba
 */
public class JWraith {
  
  public static void main(String[] args) {
    String configurationFilePath = "configuration.yml";
    String mode = "compare";
    
    if (args.length == 2) {
      mode = args[0];
      configurationFilePath = args[1];
    }
    else {
      System.out.println("Parameters error. Usage: enso_dummy_tester [mode] [configuration file path]");
      return;
    }
    
    Configuration configuration = Configuration.getInstance();
    configuration.setMode(mode);
    try {
      configuration.read(configurationFilePath);
    } catch (ConfigurationFileException | FileNotFoundException ex) {
      Logger.getLogger(JWraith.class.getName()).log(Level.SEVERE, null, ex);
      return;
    }
    
    switch (args[0]) {
      case "spider":
	launchSpider(configuration);
	break;
      case "compare":
	launchScreenshots(configuration, configuration.getBaseDomain());
	launchScreenshots(configuration, configuration.getCompareDomain());
	break;
      case "history":
	launchScreenshots(configuration, configuration.getBaseDomain());
	break;
      case "latest":
	launchScreenshots(configuration, configuration.getBaseDomain());
	break;
      default:
	System.out.println("Unsupported operation '" + args[0] + "'.");
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
    
//    WebsiteReportGenerator reportGenerator = new WebsiteReportGenerator(configuration.getWebsites(), configuration.getDisplays(), paths);
//    reportGenerator.process();
    
    // Get link list
//    WebsiteCrawler crawler = new WebsiteCrawler(configuration.getBaseUrls().get(0));
//    Queue<String> paths = crawler.process();
    
    
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
    
//    WebsiteReportGenerator reportGenerator = new WebsiteReportGenerator(configuration.getWebsites(), configuration.getDisplays(), paths);
//    reportGenerator.process();
  }
  
  private static void launchSpider(Configuration configuration) {
    WebsiteCrawler crawler = new WebsiteCrawler(configuration.getBaseDomain());
    crawler.process();
  }

  private static void launchScreenshots(Configuration configuration, String baseDomain) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
