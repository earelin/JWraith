package com.ubm.jwraith.reports;

import com.ubm.jwraith.config.Domain;
import com.ubm.jwraith.screenshots.WebsiteScreenshots;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Dimension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 *
 * @author Xavier Carriba
 */
public class WebsiteReportGenerator {
  
  private TemplateEngine templateEngine;
  private final List<Domain> websites;
  private final List<Dimension> displays;
  private final Queue<String> paths;

  public WebsiteReportGenerator(List<Domain> websites, List<Dimension> displays, Queue<String> paths) {
    this.websites = websites;
    this.displays = displays;
    this.paths = paths;
    
    // Configuring thymeleaf
    templateEngine = new TemplateEngine();
    final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setPrefix("templates/");
    resolver.setTemplateMode("HTML5");
    templateEngine.setTemplateResolver(resolver);
  }
  
  public void process() {
    ReportResult report = loadData();
    
    PrintWriter writer = null;
    try {
      final Context ctx = new Context();
      
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      ctx.setVariable("report_date", dateFormat.format(new Date()));      
      
      writer = new PrintWriter("shots/index.html");      
      templateEngine.process("main.html", ctx, writer);  
    } catch (FileNotFoundException ex) {
      Logger.getLogger(WebsiteReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      writer.close();
    }
  }
  
  private void generateThumbnails() {
    for (String path : paths) {      
      String folder = WebsiteScreenshots.generateFolderName(path);      
    }
  }
  
  private ReportResult loadData() {
    ReportResult result = new ReportResult();
    
    result.setWebsite1(websites.get(0));
    result.setWebsite2(websites.get(1));
    result.setDate(new Date());
    
    for (String path : paths) {
      PageResult pageResult = new PageResult(path);
      String folder = WebsiteScreenshots.generateFolderName(path);
      for (Dimension display : displays) {
	DisplayResult displayResult = new DisplayResult();
	displayResult.setWidth(0);
      }
    }
    
    return result;
  }
  
}
