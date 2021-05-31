import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

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

    private void pagesize() {
        var maxSelector = By.id("max");
        new WebDriverWait(driver, 3).until(presenceOfElementLocated(maxSelector));
        var max = new Select(driver.findElement(maxSelector));
        max.selectByValue(Integer.toString(PAGE_SIZE));
        new WebDriverWait(driver, 10).until(ExpectedConditions.numberOfElementsToBe(By.xpath("//tbody/tr"), PAGE_SIZE));
    }

    private List<List<String>> readTable() {
        var result = new ArrayList<List<String>>();
        result.add(getHeadings());
        do {
            result.addAll(getPageResult());
        } while (nextPage());
        return result;
    }

    private List<List<String>> getPageResult() {
        var tableSelector = By.id("resultsTable");
        new WebDriverWait(driver, 5).until(presenceOfElementLocated(tableSelector));
        var table = driver.findElement(tableSelector);
        var tbody = table.findElement(By.tagName("tbody"));
        var rows = tbody.findElements(By.tagName("tr"));
        Function<WebElement, List<String>> rowText = row -> row.findElements(By.tagName("td")).stream().map(WebElement::getText).collect(Collectors.toList());
        return rows.stream().map(rowText).collect(Collectors.toList());
    }

    private List<String> getHeadings() {
        var thead = driver.findElement(By.id("resultsTable")).findElement(By.tagName("thead"));
        return thead.findElements(By.tagName("th")).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private boolean nextPage() {
        var nextLink = driver.findElement(By.className("nextLink"));
        if (nextLink == null) {
            return false;
        }
        nextLink.click();
        var currentUrl = driver.getCurrentUrl();
        new WebDriverWait(driver, 20).until(not(urlToBe(currentUrl)));
        return true;
    }

    public void close() {
        driver.close();
    }
}
