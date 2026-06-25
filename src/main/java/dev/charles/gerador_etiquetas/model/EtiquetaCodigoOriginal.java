package dev.charles.gerador_etiquetas.model;

public class EtiquetaCodigoOriginal {

    private Long id;
    private Long etiquetaId;
    private String codigoOriginal;
    private Fabricante fabricante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEtiquetaId() {
        return etiquetaId;
    }

    public void setEtiquetaId(Long etiquetaId) {
        this.etiquetaId = etiquetaId;
    }

    public String getCodigoOriginal() {
        return codigoOriginal;
    }

    public void setCodigoOriginal(String codigoOriginal) {
        this.codigoOriginal = codigoOriginal;
    }

    public Fabricante getFabricante() {
        return fabricante;
    }

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }

    @Override
    public String toString() {
        if (fabricante != null && fabricante.getNome() != null) {
            return codigoOriginal + " - " + fabricante.getNome();
        }

        return codigoOriginal;
    }
}