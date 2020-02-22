package Utils;
import java.util.Properties;

import javax.servlet.ServletContext;

import java.io.File;
import java.io.InputStream;

public class LibraryUtilProp {
   static Properties prop = new Properties();

   public static void loadProperty(ServletContext servletContext) throws Exception {
      String filePath = "/WEB-INF/config.properties";
      InputStream is = servletContext.getResourceAsStream(filePath);

      System.out.println("[DBG] Loaded: " + new File(filePath).getAbsolutePath());
      //prop.load(is);
      prop.setProperty("url", "jdbc:mysql://ec2-18-216-47-68.us-east-2.compute.amazonaws.com:3306/LibraryDatabase");
      prop.setProperty("user", "ghoefer");
      prop.setProperty("password", "Snaredrummer1!");
   }

   public static String getProp(String key) {
      return prop.getProperty(key).trim();
   }
}