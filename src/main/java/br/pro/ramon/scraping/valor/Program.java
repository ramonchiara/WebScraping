package br.pro.ramon.scraping.valor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Program {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        for (int page = 0; page < 5; page++) {
            String json = Jsoup.connect("http://www.valor.com.br/busca/gasolina?method=ajax&page=" + page).method(Connection.Method.POST).execute().body();
            JsonNode obj = mapper.readTree(json);
            String html = obj.get("result").asText();

            Document document = Jsoup.parse(html);
            Elements noticias = document.select(".search-result-item");
            for (Element noticia : noticias) {
                Elements titulo = noticia.select("h2");
                System.out.println(titulo.text());
            }
        }
    }

}
