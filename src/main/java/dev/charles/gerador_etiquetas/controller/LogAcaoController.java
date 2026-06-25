package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.LogAcaoBO;
import dev.charles.gerador_etiquetas.model.LogAcao;

import java.util.List;
import java.util.stream.Collectors;

public class LogAcaoController {

    private final LogAcaoBO logAcaoBO;

    public LogAcaoController() {
        this.logAcaoBO = new LogAcaoBO();
    }

    public void registrar(String acao, String entidade, Long entidadeId, String detalhes) {
        logAcaoBO.registrar(acao, entidade, entidadeId, detalhes);
    }

    public void registrar(String acao, String entidade, Long entidadeId) {
        logAcaoBO.registrar(acao, entidade, entidadeId, null);
    }

    public List<LogAcao> listarTodos() {
        return logAcaoBO.listarLogs();
    }

    public List<LogAcao> listarPorUsuario(Long usuarioId) {
        if (usuarioId == null) {
            throw new RuntimeException("ID do usuário é obrigatório.");
        }

        return listarTodos()
                .stream()
                .filter(log ->
                        log.getUsuario() != null &&
                                log.getUsuario().getId() != null &&
                                log.getUsuario().getId().equals(usuarioId)
                )
                .collect(Collectors.toList());
    }

    public List<LogAcao> listarPorEntidade(String entidade) {
        if (entidade == null || entidade.isBlank()) {
            throw new RuntimeException("Entidade é obrigatória.");
        }

        String entidadeTratada = entidade.trim();

        return listarTodos()
                .stream()
                .filter(log ->
                        log.getEntidade() != null &&
                                log.getEntidade().equalsIgnoreCase(entidadeTratada)
                )
                .collect(Collectors.toList());
    }
}