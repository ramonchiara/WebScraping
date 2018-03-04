package br.pro.ramon.scraping.jsoup;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Program {

    public static void main(String[] args) throws IOException {
        String ua = "Mozilla/5.0";

        List<DBObject> dados = new ArrayList<>();

        Document document = Jsoup.connect("http://sp.olx.com.br/imoveis").userAgent(ua).get();
        Elements anuncios = document.select("#main-ad-list li.item");
        for (Element anuncio : anuncios) {
            String url = anuncio.select("a").attr("abs:href");
            if (url.trim().isEmpty()) {
                continue;
            }

            Document apto = Jsoup.connect(url).userAgent(ua).get();
            String id = apto.select(".OLXad-id .description").text();
            String titulo = apto.select("#ad_title").text();
            String preco = apto.select(".OLXad-price").text();
            String cep = apto.select("[class*=OLXad-location] p:contains(CEP do imóvel)").first().child(1).text();

            Anuncio a = new Anuncio(url, id, titulo, getValor(preco), cep);

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            String json = mapper.writeValueAsString(a);

            DBObject dado = (DBObject) JSON.parse(json);
            dados.add(dado);
        }

        MongoClient mongo = new MongoClient("localhost", 27017);
        DB db = mongo.getDB("piv");
        DBCollection table = db.getCollection("anuncios");
        table.insert(dados);
    }

    private static Double getValor(String preco) {
        Double valor = null;

        try {
            if (preco != null && !preco.trim().isEmpty()) {
                valor = NumberFormat.getCurrencyInstance(Locale.getDefault()).parse(preco.trim()).doubleValue(); // Locale.forLanguageTag("pt-BR")
            }
        } catch (ParseException ex) {
        }

        return valor;
    }

}
