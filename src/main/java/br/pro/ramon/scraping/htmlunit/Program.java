package br.pro.ramon.scraping.htmlunit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

public class Program {

    public static void main(String[] args) {
        String searchQuery = "Iphone 6s";

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            String searchUrl = "https://newyork.craigslist.org/search/sss?sort=rel&query=" + URLEncoder.encode(searchQuery, "UTF-8");
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlElement> items = page.getByXPath("//p[@class='result-info']");
            if (items.isEmpty()) {
                System.out.println("No items found !");
            } else {
                for (HtmlElement htmlItem : items) {
                    HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//a"));
                    HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@class='result-price']"));

                    // It is possible that an item doesn't have any price, we set the price to 0.0 in this case
                    String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();

                    Item item = new Item();
                    item.setTitle(itemAnchor.asText());
                    item.setUrl(itemAnchor.getHrefAttribute());
                    item.setPrice(new BigDecimal(itemPrice.replace("$", "")));

                    ObjectMapper mapper = new ObjectMapper();
                    String jsonString = mapper.writeValueAsString(item);
                    System.out.println(jsonString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
