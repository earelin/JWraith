package com.ubm.jwraith;

import com.ubm.jwraith.config.ConfigurationFileException;
import com.ubm.jwraith.config.Configuration;
import com.ubm.jwraith.crawler.WebsiteCrawler;
import com.ubm.jwraith.diff.ScreenshotsDiff;
import com.ubm.jwraith.reports.ReportData;
import com.ubm.jwraith.reports.PageReport;
import com.ubm.jwraith.reports.WebsiteThumbnails;
import com.ubm.jwraith.screenshots.WebsiteScreenshots;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author Xavier Carriba
 */
public class JWraith {
  
  public static void main(String[] args) {
    String configurationFilePath = "configuration.yml";
    String mode = "capture";
    List<String> paths;
    
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
      case "capture":
	paths = loadPaths();
	//launchScreenshots(configuration, configuration.getBaseDomain(), "base", paths);
	//launchScreenshots(configuration, configuration.getCompareDomain(), "compare", paths);
	ReportData report = launchDiffCalculationCapture(
		configuration.getDirectory(), configuration.getDirectory(), paths);
	lauchReportGenerator(report);
	break;
      case "history":
	paths = loadPaths();
	launchScreenshots(configuration, configuration.getBaseDomain(), "base", paths);
	break;
      case "latest":
	paths = loadPaths();
	launchScreenshots(configuration, configuration.getBaseDomain(), "compare", paths);
	break;
      default:
	System.out.println("Unsupported operation '" + args[0] + "'.");
    }
  }
  
  private static List<String> loadPaths() {
    List<String> paths = new ArrayList<>();
    try (Stream<String> stream = Files.lines(Paths.get("urls.txt"))) {
      String[] urls = stream.toArray(String[]::new);
      for (String url : urls) {
	paths.add(url);
      }
    } catch (IOException ex) {
      Logger.getLogger(JWraith.class.getName()).log(Level.SEVERE, null, ex);
    }
    return paths;
  }
  
  private static void launchSpider(Configuration configuration) {
    WebsiteCrawler crawler = new WebsiteCrawler(configuration.getBaseDomain());
    crawler.process();
  }

  private static void launchScreenshots(Configuration configuration, String domain, String domainLabel, List<String> paths) {
    WebsiteScreenshots screenshots = new WebsiteScreenshots(domain, domainLabel, configuration.getDirectory(), paths);
    screenshots.process();
  }
  
  private static ReportData launchDiffCalculationCapture(String baseFolder, String compareFolder, List<String> paths) {
    ScreenshotsDiff diffs = new ScreenshotsDiff(baseFolder, compareFolder, paths);
    List<PageReport> pageReports = diffs.process();
    ReportData report = new ReportData();
    report.setPages(pageReports);    
    return report;
  }
  
  private static void lauchReportGenerator(ReportData report) {
    WebsiteThumbnails websiteThumbnails = new WebsiteThumbnails(report);
    websiteThumbnails.process();
    //WebsiteReportGenerator reportGenerator = new WebsiteReportGenerator();
  }
}
