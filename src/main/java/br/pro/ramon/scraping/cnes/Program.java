package br.pro.ramon.scraping.cnes;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Program {

    public static void main(String[] args) throws IOException {
        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

        // http://svc.auto-pilot.cz/HMA/proxies.html
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        WebClientOptions options = webClient.getOptions();
        options.setTimeout(0);

        HtmlPage mantenedoraPage = webClient.getPage("http://cnes2.datasus.gov.br/Listar_Mantidas.asp?VCnpj=46392130000380");
        List<HtmlAnchor> mantidosLinks = mantenedoraPage.getAnchors();
        for (int i = 0; i < mantidosLinks.size(); i += 2) {
            HtmlAnchor mantidoLink = mantidosLinks.get(i);
            HtmlPage mantidoPage = mantidoLink.click();
            List<HtmlAnchor> modulosLinks = mantidoPage.getAnchors();
            HtmlAnchor moduloLink = findIn("Mod_Profissional", modulosLinks);
            HtmlPage profissionaisPage = moduloLink.click();
            HtmlTable profissionais = profissionaisPage.getHtmlElementById("example");
            System.out.println("rows = " + profissionais.getRows().size());
            break;
        }
    }

    private static HtmlAnchor findIn(String href, List<HtmlAnchor> links) {
        for (HtmlAnchor link : links) {
            if (link.getHrefAttribute().startsWith(href)) {
                return link;
            }
        }
        return null;
    }

}
