import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;

public class App {

    public static void main(String[] args) throws ConfigurationException {
        var config = new Configurations()
                .properties(new File("crawler.properties"));

        var driver = new FirefoxDriver();
        var crawler = new Crawler(driver);

        crawler.crawl(config.getString("url"));

        if (config.getBoolean("close")) {
            crawler.close();
        }
    }
}
