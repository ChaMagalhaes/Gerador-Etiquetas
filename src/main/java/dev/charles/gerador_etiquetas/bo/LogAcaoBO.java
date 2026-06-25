package dev.charles.gerador_etiquetas.bo;

import dev.charles.gerador_etiquetas.dao.LogAcaoDAO;
import dev.charles.gerador_etiquetas.model.LogAcao;
import dev.charles.gerador_etiquetas.model.Usuario;
import dev.charles.gerador_etiquetas.util.SessaoUsuario;

import java.util.List;
import java.util.stream.Collectors;

public class LogAcaoBO {

    private final LogAcaoDAO logAcaoDAO;

    public LogAcaoBO() {
        this.logAcaoDAO = new LogAcaoDAO();
    }

    public void registrar(String acao, String entidade, Long entidadeId, String detalhes) {
        Long usuarioId = SessaoUsuario.getUsuarioIdLogado();

        if (usuarioId == null) {
            System.err.println("Log não registrado: nenhum usuário logado.");
            return;
        }

        validarDados(acao, entidade);

        try {
            logAcaoDAO.salvar(usuarioId, acao.trim(), entidade.trim(), entidadeId, detalhes);
        } catch (RuntimeException e) {
            System.err.println("Falha ao registrar log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void registrar(String acao, String entidade, Long entidadeId) {
        registrar(acao, entidade, entidadeId, null);
    }

    public void registrar(Usuario usuario, String acao, String entidade, Long entidadeId) {
        registrar(usuario, acao, entidade, entidadeId, null);
    }

    public void registrar(Usuario usuario, String acao, String entidade, Long entidadeId, String detalhes) {
        if (usuario == null || usuario.getId() == null) {
            System.err.println("Log não registrado: usuário inválido.");
            return;
        }

        validarDados(acao, entidade);

        try {
            logAcaoDAO.salvar(usuario.getId(), acao.trim(), entidade.trim(), entidadeId, detalhes);
        } catch (RuntimeException e) {
            System.err.println("Falha ao registrar log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<LogAcao> listarLogs() {
        return logAcaoDAO.listar();
    }

    public List<LogAcao> listarTodos() {
        return listarLogs();
    }

    public List<LogAcao> listarPorUsuario(Long usuarioId) {
        if (usuarioId == null) {
            throw new RuntimeException("ID do usuário é obrigatório.");
        }

        return listarLogs()
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

        return listarLogs()
                .stream()
                .filter(log ->
                        log.getEntidade() != null &&
                                log.getEntidade().equalsIgnoreCase(entidadeTratada)
                )
                .collect(Collectors.toList());
    }

    private void validarDados(String acao, String entidade) {
        if (acao == null || acao.isBlank()) {
            throw new RuntimeException("A ação do log é obrigatória.");
        }

        if (entidade == null || entidade.isBlank()) {
            throw new RuntimeException("A entidade do log é obrigatória.");
        }
    }
}