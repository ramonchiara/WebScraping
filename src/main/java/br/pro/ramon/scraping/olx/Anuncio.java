package br.pro.ramon.scraping.olx;

import java.util.HashMap;
import java.util.Map;

public class Anuncio {

    private String url;
    private String breadcrumb;
    private String titulo;
    private String data;
    private Double preco;
    private String user;
    private String descricao;
    private Map<String, String> detalhes;
    private String cep;
    private String codigo;

    public Anuncio(String url, String breadcrumb, String titulo, String data, Double preco, String user, String descricao, String cep, String codigo) {
        this.url = url;
        this.breadcrumb = breadcrumb;
        this.titulo = titulo;
        this.data = data;
        this.preco = preco;
        this.user = user;
        this.descricao = descricao;
        this.detalhes = new HashMap<>();
        this.cep = cep;
        this.codigo = codigo;
    }

    public String getUrl() {
        return url;
    }

    public String getBreadcrumb() {
        return breadcrumb;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getData() {
        return data;
    }

    public Double getPreco() {
        return preco;
    }

    public String getUser() {
        return user;
    }

    public String getDescricao() {
        return descricao;
    }

    public String put(String key, String value) {
        return detalhes.put(key, value);
    }

    public Map<String, String> getDetalhes() {
        return detalhes;
    }

    public String getCep() {
        return cep;
    }

    public String getCodigo() {
        return codigo;
    }

}
