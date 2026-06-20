package dev.charles.gerador_etiquetas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Etiqueta {

    private Long id;
    private Prateleira prateleira;
    private String descricao;
    private String codigoVenda;
    private List<String> codigosOriginais;
    private LocalDateTime dataCriacao;
    private double larguraCm;
    private double alturaCm;

    public Etiqueta() {
        this.codigosOriginais = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
        this.larguraCm = 12.0;
        this.alturaCm = 6.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prateleira getPrateleira() {
        return prateleira;
    }

    public void setPrateleira(Prateleira prateleira) {
        this.prateleira = prateleira;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoVenda() {
        return codigoVenda;
    }

    public void setCodigoVenda(String codigoVenda) {
        this.codigoVenda = codigoVenda;
    }

    public List<String> getCodigosOriginais() {
        return codigosOriginais;
    }

    public void setCodigosOriginais(List<String> codigosOriginais) {
        this.codigosOriginais = codigosOriginais;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public double getLarguraCm() {
        return larguraCm;
    }

    public void setLarguraCm(double larguraCm) {
        this.larguraCm = larguraCm;
    }

    public double getAlturaCm() {
        return alturaCm;
    }

    public void setAlturaCm(double alturaCm) {
        this.alturaCm = alturaCm;
    }

    public void adicionarCodigoOriginal(String codigoOriginal) {
        this.codigosOriginais.add(codigoOriginal);
    }

    @Override
    public String toString() {
        return descricao + " | " + codigoVenda;
    }
}
