import org.openqa.selenium.firefox.FirefoxDriver;

public class Crawler {

    private final FirefoxDriver driver;

    public Crawler(FirefoxDriver driver) {
        this.driver = driver;
    }

    public void crawl(String url) {
        driver.get(url);
    }

    public void close() {
        driver.close();
    }
}
