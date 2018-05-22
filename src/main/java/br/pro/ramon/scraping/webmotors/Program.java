package br.pro.ramon.scraping.webmotors;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class Program {

    public static void main(String[] args) {
        Settings settings = Settings.builder().cache(true).build();
        JBrowserDriver driver = new JBrowserDriver(settings);

        String url = "https://www.webmotors.com.br/carros/estoque";
        driver.get(url);

        while (true) {
            try {
                List<WebElement> ads = driver.findElementsByCssSelector(".advert");
                for (WebElement ad : ads) {
                    WebElement title = ad.findElement(By.cssSelector("h2 .make-model-financiamento"));
                    WebElement price = ad.findElement(By.cssSelector("div[itemprop=price]"));
                    System.out.printf("%s - %s%n", title.getText(), price.getText());
                }
                WebElement next = driver.findElementByCssSelector(".paginationResult.next");
                String css = next.getAttribute("class");
                if (css.contains("brd-gray2")) {
                    break;
                } else {
                    System.out.println("----------------------------------------");
                    sleep();
                    next.click();
                }
            } catch (NoSuchElementException ex) {
                System.out.println("Não encontrei... " + ex.getMessage());
                sleep();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                sleep();
            }
        }

        driver.quit();
    }

    private static final Random RNG = new Random();

    private static void sleep() {
        try {
            long ms = RNG.nextInt(5) + 1;
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
        }
    }

}
