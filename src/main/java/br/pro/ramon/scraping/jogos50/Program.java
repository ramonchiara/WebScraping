package br.pro.ramon.scraping.jogos50;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.Arrays;
import java.util.List;

public class Program {

    public static void main(String[] args) throws Exception {
        String site = "http://www.ibilce.unesp.br/#!/departamentos/matematica/extensao/lab-mat/jogos-no-ensino-de-matematica/";

        List<String> sites = Arrays.asList("http://www.ibilce.unesp.br/#!/departamentos/matematica/extensao/lab-mat/jogos-no-ensino-de-matematica/1-ao-5-ano/",
                "http://www.ibilce.unesp.br/#!/departamentos/matematica/extensao/lab-mat/jogos-no-ensino-de-matematica/6-ao-9-ano/",
                "http://www.ibilce.unesp.br/#!/departamentos/matematica/extensao/lab-mat/jogos-no-ensino-de-matematica/ensino-medio/");


        WebClient c = new WebClient();
        HtmlPage page = c.getPage(site);
        System.out.println(page.asXml());

        if (true) {
            return;
        }
        HtmlAnchor link = page.getAnchorByText("Jogos no Ens. Fund. I - 1º ao 5º ano");
        HtmlPage jogos1 = link.click();
        System.out.println(jogos1.asText());

    }

}
