package br.pro.ramon.scraping.olx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Program {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase db = mongo.getDatabase("piv");
        MongoCollection<DBObject> table = db.getCollection("anuncios", DBObject.class);

        int novos = 0;
        int antigos = 0;
        int erros = 0;

        String ua = "Mozilla/5.0";
        for (int pagina = 1; pagina <= 100; pagina++) {
            Document document = Jsoup.connect("http://sp.olx.com.br/imoveis?o=" + pagina).userAgent(ua).get();
            Elements anuncios = document.select("#main-ad-list li.item");
            for (Element anuncio : anuncios) {
                String url = anuncio.select("a").attr("abs:href");
                if (url.trim().isEmpty()) {
                    continue;
                }

                System.out.print(url);
                DBObject d = table.find(new BsonDocument("url", new BsonString(url))).first();
                if (d != null) {
                    antigos++;
                    System.out.println("... found!");
                    continue;
                } else {
                    novos++;
                    System.out.println("... downloading...");
                }
                try {
                    Document apto = Jsoup.connect(url).userAgent(ua).get();
                    String breadcrumb = apto.select(".breadcrumb-list").text();
                    String titulo = apto.select("#ad_title").text();
                    String data = apto.select(".OLXad-date").text();
                    String preco = apto.select(".OLXad-price").text();
                    String user = apto.select(".section_OLXad-user-info .owner").text();
                    String descricao = apto.select(".OLXad-description").text();
                    Elements detalhes = apto.select(".OLXad-details ul.list li");
                    String cep = apto.select("[class*=OLXad-location] p:contains(CEP do imóvel)").first().child(1).text();
                    String codigo = apto.select(".OLXad-id .description").text();

                    Anuncio a = new Anuncio(url, breadcrumb, titulo, data, getValor(preco), user, descricao, cep, codigo);

                    for (Element detalhe : detalhes) {
                        String chave = detalhe.select(".term").text();
                        String valor = detalhe.select(".description").text();
                        a.put(chave, valor);
                    }

                    String json = mapper.writeValueAsString(a);
                    DBObject dado = BasicDBObject.parse(json);
                    table.insertOne(dado);
                } catch (UnknownHostException ex) {
                    novos--;
                    erros++;
                    System.err.println("A URL encontrada não é válida!");
                }
            }
        }

        System.out.println("Antigos: " + antigos);
        System.out.println("Novos..: " + novos);
        System.out.println("Erros..: " + erros);
    }

    private static Double getValor(String preco) {
        Double valor = null;

        try {
            if (preco != null && !preco.trim().isEmpty()) {
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")); // Locale.getDefault()
                valor = format.parse(preco.trim()).doubleValue();
            }
        } catch (ParseException ex) {
        }

        return valor;
    }

}
