package com.ubm.jwraith.reports;

import com.ubm.jwraith.config.Configuration;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 *
 * @author Xavier Carriba
 */
public class WebsiteReportGenerator {
  
  private static WebsiteReportGenerator instance;
  
  private final Configuration configuration = Configuration.getInstance();
  private final TemplateEngine templateEngine;

  public static WebsiteReportGenerator getInstance() {
    if (instance == null) {
      instance = new WebsiteReportGenerator();
    }
    return instance;
  }
  
  private WebsiteReportGenerator() { 
    // Configuring thymeleaf
    templateEngine = new TemplateEngine();
    final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setPrefix("templates/");
    resolver.setTemplateMode("HTML");
    templateEngine.setTemplateResolver(resolver);
  }
  
  public void process(ReportData report) {
    PrintWriter writer = null;
    try {
      final Context ctx = new Context();

      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      ctx.setVariable("report_date", dateFormat.format(new Date()));
      ctx.setVariable("pages", report.getPages());
      ctx.setVariable("threshold", configuration.getReport().getThreshold());
      
      writer = new PrintWriter(configuration.getDirectory() + "/index.html");   
      templateEngine.process("main.html", ctx, writer);  
    } catch (FileNotFoundException ex) {
      Logger.getLogger(WebsiteReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (writer != null) {
	writer.close();
      }
    }
  }
  
}
