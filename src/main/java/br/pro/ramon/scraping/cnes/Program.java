package br.pro.ramon.scraping.cnes;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import java.util.List;
import org.openqa.selenium.WebElement;

public class Program {

    public static void main(String[] args) {
        JBrowserDriver driver = new JBrowserDriver();

        // carrega a página principal
        String url = "http://cnes2.datasus.gov.br/Listar_Mantidas.asp?VCnpj=46392130000380";
        driver.get(url);

        // decobre os links dos mantidos
        List<WebElement> links = driver.findElementsByCssSelector("a");
        for (WebElement link : links) {
            // carrega a página de uma mantida
            url = link.getAttribute("href");
            driver.get(url);

            // descobre os botões de módulos
            List<WebElement> modulos = driver.findElementsByCssSelector("a");
            for (WebElement modulo : modulos) {
                url = modulo.getAttribute("href");

                // se for o botão Profissionais
                if (url.contains("Profissional")) {
                    // carreaga a página de profissionais
                    driver.get(url);

                    boolean fim = false;
                    do {
                        // mostra/usa os dados
                        WebElement dados = driver.findElementByCssSelector("table#example");
                        System.out.println(dados.getText());

                        // carrega os dados de paginação
                        WebElement infoElem = driver.findElementById("example_info");
                        String info = infoElem.getText();

                        // verifica se fim
                        if (info.matches("Mostrando de (\\d+) até (\\d+) de (\\2) registros")) {
                            fim = true;
                        } else {
                            // "clica" no botão next
                            WebElement next = driver.findElementByCssSelector("span.next");
                            next.click();
                        }
                    } while (!fim);

                    // não tirar esse break
                    break;
                }
            }

            // tirar esse break para processar todos os mantidos
            break;
        }

        driver.quit();
    }

}
