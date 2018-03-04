package br.pro.ramon.scraping.jsoup;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Teste {

    public static void main(String[] args) throws JsonProcessingException {
        Anuncio a = new Anuncio("url", "id", "titulo", null, "cep");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        String json = mapper.writeValueAsString(a);

        System.out.println(json);
    }

}
