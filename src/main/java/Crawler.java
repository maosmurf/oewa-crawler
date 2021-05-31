import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Crawler {

    private final FirefoxDriver driver;

    public Crawler(FirefoxDriver driver) {
        this.driver = driver;
    }

    public void crawl(String url) {
        driver.get(url);
        selectCountry();
    }

    private void selectCountry() {
        var countriesValue = "CountryAUT";
        var input  = driver.findElement(By.xpath("//input[@id=\"countries\"][@value=\"" + countriesValue + "\"]"));
        input.click();
        var form = driver.findElement(By.id("publicSearchForm"));
        form.submit();
    }

    public void close() {
        driver.close();
    }
}
