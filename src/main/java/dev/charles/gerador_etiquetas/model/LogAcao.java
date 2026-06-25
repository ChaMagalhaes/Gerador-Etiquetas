package dev.charles.gerador_etiquetas.model;

import java.time.LocalDateTime;

public class LogAcao {

    private Long id;
    private Usuario usuario;
    private String acao;
    private String entidade;
    private Long entidadeId;
    private String detalhes;
    private LocalDateTime dataHora;

    public LogAcao() {
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public Long getEntidadeId() {
        return entidadeId;
    }

    public void setEntidadeId(Long entidadeId) {
        this.entidadeId = entidadeId;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        String nomeUsuario = usuario != null ? usuario.getNome() : "?";
        String idEntidade = entidadeId != null ? "#" + entidadeId : "";

        return "[" + dataHora + "] "
                + acao
                + " em "
                + entidade
                + " "
                + idEntidade
                + " por "
                + nomeUsuario
                + (detalhes != null && !detalhes.isBlank() ? " - " + detalhes : "");
    }
}