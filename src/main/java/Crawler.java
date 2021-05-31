import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class Crawler {

    private static final int PAGE_SIZE = 100;

    private final FirefoxDriver driver;

    public Crawler(FirefoxDriver driver) {
        this.driver = driver;
    }

    public void crawl(String url) {
        driver.get(url);
        selectCountry();
        search();
        pagesize();
        var result = readTable();
        System.out.println(result);
    }

    private void pagesize() {
        var maxSelector = By.id("max");
        WebDriverWait waitBefore = new WebDriverWait(driver, 3);
        waitBefore.until(presenceOfElementLocated(maxSelector));
        var max = new Select(driver.findElement(maxSelector));
        max.selectByValue(Integer.toString(PAGE_SIZE));
        var waitAfter = new WebDriverWait(driver, 10);
        waitAfter.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//tbody/tr"), PAGE_SIZE));
    }

    private void selectCountry() {
        var countriesValue = "CountryAUT";
        var input = driver.findElement(By.xpath("//input[@id=\"countries\"][@value=\"" + countriesValue + "\"]"));
        input.click();
        var publicSearchForm = driver.findElement(By.id("publicSearchForm"));
        publicSearchForm.submit();
    }

    private void search() {
        var titleSelector = By.id("aidMeasureTitle");
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(presenceOfElementLocated(titleSelector));
        var measureTitle = driver.findElement(titleSelector);
        measureTitle.sendKeys("covid-19");
        var publicSearchForm = driver.findElement(By.id("publicSearchForm"));
        publicSearchForm.submit();
    }

    private List<List<String>> readTable() {
        var table = driver.findElement(By.id("resultsTable"));
        var thead = table.findElement(By.tagName("thead"));
        var headings = thead.findElements(By.tagName("th")).stream().map(WebElement::getText).collect(Collectors.toList());
        var tbody = table.findElement(By.tagName("tbody"));
        var rows = tbody.findElements(By.tagName("tr"));
        Function<WebElement, List<String>> rowText = row -> row.findElements(By.tagName("td")).stream().map(WebElement::getText).collect(Collectors.toList());
        var result = rows.stream().map(rowText).collect(Collectors.toList());
        result.add(0, headings);
        return result;

    }

    public void close() {
        driver.close();
    }
}
