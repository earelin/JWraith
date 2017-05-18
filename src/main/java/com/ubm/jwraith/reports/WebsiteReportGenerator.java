package com.ubm.jwraith.reports;

import com.ubm.jwraith.config.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 *
 * @author Xavier Carriba
 */
public class WebsiteReportGenerator {
  
  private final Configuration configuration = Configuration.getInstance();
  private final TemplateEngine templateEngine;
  private final ReportData report;

  public WebsiteReportGenerator(ReportData report) {
    this.report = report;
    
    // Configuring thymeleaf
    templateEngine = new TemplateEngine();
    final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setPrefix("templates/");
    resolver.setTemplateMode("HTML5");
    templateEngine.setTemplateResolver(resolver);
  }
  
  public void process() {
//    ReportData report = loadData();
//    
//    PrintWriter writer = null;
//    try {
//      final Context ctx = new Context();
//      
//      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//      ctx.setVariable("report_date", dateFormat.format(new Date()));      
//      
//      writer = new PrintWriter("shots/index.html");      
//      templateEngine.process("main.html", ctx, writer);  
//    } catch (FileNotFoundException ex) {
//      Logger.getLogger(WebsiteReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
//    } finally {
//      writer.close();
//    }
  }
  
}
