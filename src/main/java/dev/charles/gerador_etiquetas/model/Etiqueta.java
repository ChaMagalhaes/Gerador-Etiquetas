package dev.charles.gerador_etiquetas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Etiqueta {

    private Long id;
    private Prateleira prateleira;
    private Grupo grupo;
    private SubGrupo subGrupo;
    private String descricao;
    private String codigoVenda;
    private List<EtiquetaCodigoOriginal> codigosOriginais;
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

        if (this.codigosOriginais != null) {
            for (EtiquetaCodigoOriginal codigo : this.codigosOriginais) {
                codigo.setEtiquetaId(id);
            }
        }
    }

    public Prateleira getPrateleira() {
        return prateleira;
    }

    public void setPrateleira(Prateleira prateleira) {
        this.prateleira = prateleira;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public SubGrupo getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(SubGrupo subGrupo) {
        this.subGrupo = subGrupo;
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

    public List<EtiquetaCodigoOriginal> getCodigosOriginais() {
        return codigosOriginais;
    }

    public void setCodigosOriginais(List<EtiquetaCodigoOriginal> codigosOriginais) {
        this.codigosOriginais = codigosOriginais != null ? codigosOriginais : new ArrayList<>();

        if (this.id != null) {
            for (EtiquetaCodigoOriginal codigo : this.codigosOriginais) {
                codigo.setEtiquetaId(this.id);
            }
        }
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
        adicionarCodigoOriginal(codigoOriginal, null);
    }

    public void adicionarCodigoOriginal(String codigoOriginal, Fabricante fabricante) {
        EtiquetaCodigoOriginal codigo = new EtiquetaCodigoOriginal();
        codigo.setCodigoOriginal(codigoOriginal);
        codigo.setFabricante(fabricante);
        codigo.setEtiquetaId(this.id);

        this.codigosOriginais.add(codigo);
    }

    @Override
    public String toString() {
        return descricao + " | " + codigoVenda;
    }
}
