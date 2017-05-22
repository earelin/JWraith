package com.ubm.jwraith;

import com.ubm.jwraith.browser.SeleniumFactory;
import com.ubm.jwraith.config.ConfigurationFileException;
import com.ubm.jwraith.config.Configuration;
import com.ubm.jwraith.crawler.WebsiteCrawler;
import com.ubm.jwraith.diff.ScreenshotsDiff;
import com.ubm.jwraith.reports.ReportData;
import com.ubm.jwraith.reports.PageReport;
import com.ubm.jwraith.reports.WebsiteReportGenerator;
import com.ubm.jwraith.reports.WebsiteThumbnails;
import com.ubm.jwraith.screenshots.WebsiteScreenshots;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
  
  private static Configuration configuration = Configuration.getInstance();
  private static WebsiteCrawler crawler = WebsiteCrawler.getInstance();
  private static SeleniumFactory seleniumFactory = SeleniumFactory.getInstance();
  private static ScreenshotsDiff screenshotsDiff = ScreenshotsDiff.getInstance();
  private static WebsiteThumbnails websiteThumbnails = WebsiteThumbnails.getInstance();
  private static WebsiteReportGenerator reportGenerator = WebsiteReportGenerator.getInstance();
  
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
    
    configuration.setMode(mode);
    try {
      configuration.read(configurationFilePath);
    } catch (ConfigurationFileException | FileNotFoundException ex) {
      Logger.getLogger(JWraith.class.getName()).log(Level.SEVERE, null, ex);
      return;
    }
    
    switch (args[0]) {
      case "spider":
	paths = crawler.crawl();
	savePaths(configuration.getPathsFile(), paths);
	break;
      case "browsers":
	paths = loadPaths(configuration.getPathsFile());
	
      case "capture":
	paths = loadPaths(configuration.getPathsFile());
	launchScreenshots(configuration.getBaseDomain(), "base", paths);
	launchScreenshots(configuration.getCompareDomain(), "compare", paths);
	ReportData report = launchDiffCalculationCapture(
		configuration.getDirectory(), configuration.getDirectory(), paths);
	lauchReportGenerator(report);
	break;
      case "history":
	paths = loadPaths(configuration.getPathsFile());
	launchScreenshots(configuration.getBaseDomain(), "base", paths);
	break;
      case "latest":
	paths = loadPaths(configuration.getPathsFile());
	launchScreenshots(configuration.getBaseDomain(), "compare", paths);
	break;
      default:
	System.out.println("Unsupported operation '" + args[0] + "'.");
    }
  }
  
  private static List<String> loadPaths(String filePath) {
    List<String> paths = new ArrayList<>();
    try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
      String[] urls = stream.toArray(String[]::new);
      for (String url : urls) {
	paths.add(url);
      }
    } catch (IOException ex) {
      Logger.getLogger(JWraith.class.getName()).log(Level.SEVERE, null, ex);
    }
    return paths;
  }
  
  private static void savePaths(String filePath, List<String> paths) {
    Path path = Paths.get(filePath);
    try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
      for (String url : paths) {
	writer.write(url);
      }
    } catch(IOException ex) {
      Logger.getLogger(JWraith.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static void launchScreenshots(String domain, String domainLabel, List<String> paths) {
    WebsiteScreenshots screenshots = new WebsiteScreenshots(domain, domainLabel, configuration.getDirectory(), paths);
    screenshots.process();
  }
  
  private static ReportData launchDiffCalculationCapture(String baseFolder, String compareFolder, List<String> paths) {
    List<PageReport> pageReports = screenshotsDiff.process(baseFolder, compareFolder, paths);
    ReportData report = new ReportData();
    report.setPages(pageReports);    
    return report;
  }
  
  private static void lauchReportGenerator(ReportData report) {
    websiteThumbnails.process(report);
    reportGenerator.process(report);
  }
}
