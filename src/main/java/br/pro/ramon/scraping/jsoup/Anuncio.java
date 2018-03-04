package br.pro.ramon.scraping.jsoup;

public class Anuncio {

    private String url;
    private String id;
    private String titulo;
    private Double preco;
    private String cep;

    public Anuncio(String url, String id, String titulo, Double preco, String cep) {
        this.url = url;
        this.id = id;
        this.titulo = titulo;
        this.preco = preco;
        this.cep = cep;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Double getPreco() {
        return preco;
    }

    public String getPrecoAsString() {
        return preco == null ? "" : preco.toString();
    }

    public String getCep() {
        return cep;
    }

    public boolean isBaixo() {
        return preco != null && preco < 2000;
    }

}
