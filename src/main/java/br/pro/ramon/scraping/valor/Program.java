package br.pro.ramon.scraping.valor;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Program {

    public static void main(String[] args) throws IOException {
        for (int page = 0; page < 5; page++) {
            String json = Jsoup.connect("http://www.valor.com.br/busca/gasolina?method=ajax&page=" + page).method(Connection.Method.POST).execute().body();
            DBObject obj = BasicDBObject.parse(json);
            String html = (String) obj.get("result");

            Document document = Jsoup.parse(html);
            Elements noticias = document.select(".search-result-item");
            for (Element noticia : noticias) {
                Elements titulo = noticia.select("h2");
                System.out.println(titulo.text());
            }
        }
    }

}
