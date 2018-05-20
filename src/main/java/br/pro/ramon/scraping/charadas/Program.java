package br.pro.ramon.scraping.charadas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Program {

    public static void main(String[] args) throws IOException {
        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase db = mongo.getDatabase("piv");
        MongoCollection<DBObject> collection = db.getCollection("charadas", DBObject.class);

        ObjectMapper mapper = new ObjectMapper();

        String url = "https://www.osvigaristas.com.br/charadas/o-que-e-o-que-e/";
        while (true) {
            System.out.println("downloading " + url);
            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select(".item-index");
            for (Element item : items) {
                String pergunta = item.select(".question").text();
                String resposta = item.select(".answer").text();
                String score = item.select(".score").text();

                Charada c = new Charada(score, pergunta, resposta);
                System.out.println(c);

                String json = mapper.writeValueAsString(c);
                DBObject dado = BasicDBObject.parse(json);
                collection.insertOne(dado);
            }

            Elements nextPage = doc.select(".item-index-pager .next");
            if (nextPage.isEmpty()) {
                break;
            } else {
                url = nextPage.attr("abs:href");
            }
        }

    }

}

@XmlRootElement
class Charada implements Serializable {

    private Integer score;
    private String pergunta;
    private String resposta;

    protected Charada() {
    }

    public Charada(String score, String pergunta, String resposta) {
        this(Integer.parseInt(score), pergunta, resposta);
    }

    public Charada(Integer score, String pergunta, String resposta) {
        this.score = score;
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    @XmlElement
    public Integer getScore() {
        return score;
    }

    @XmlElement
    public String getPergunta() {
        return pergunta;
    }

    @XmlElement
    public String getResposta() {
        return resposta;
    }

    @Override
    public String toString() {
        return "Charada{" + "score=" + score + ", pergunta=" + pergunta + '}';
    }

}
