package dev.charles.gerador_etiquetas.dao;

import dev.charles.gerador_etiquetas.model.LogAcao;
import dev.charles.gerador_etiquetas.model.Usuario;
import dev.charles.gerador_etiquetas.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogAcaoDAO {

    public void salvar(Long usuarioId, String acao, String entidade, Long entidadeId, String detalhes) {
        String sql = """
                INSERT INTO log_acao 
                (usuario_id, acao, entidade, entidade_id, detalhes, data_hora)
                VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, usuarioId);
            stmt.setString(2, acao);
            stmt.setString(3, entidade);

            if (entidadeId != null) {
                stmt.setLong(4, entidadeId);
            } else {
                stmt.setNull(4, Types.BIGINT);
            }

            stmt.setString(5, detalhes);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar log de ação: " + e.getMessage(), e);
        }
    }

    public List<LogAcao> listar() {
        String sql = """
                SELECT
                    l.id,
                    l.usuario_id,
                    u.nome AS usuario_nome,
                    u.login AS usuario_login,
                    l.acao,
                    l.entidade,
                    l.entidade_id,
                    l.detalhes,
                    l.data_hora
                FROM log_acao l
                INNER JOIN usuario u ON l.usuario_id = u.id
                ORDER BY l.data_hora DESC
                """;

        List<LogAcao> logs = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LogAcao log = new LogAcao();

                log.setId(rs.getLong("id"));

                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("usuario_id"));
                usuario.setNome(rs.getString("usuario_nome"));
                usuario.setLogin(rs.getString("usuario_login"));

                log.setUsuario(usuario);
                log.setAcao(rs.getString("acao"));
                log.setEntidade(rs.getString("entidade"));

                Number entidadeIdBanco = (Number) rs.getObject("entidade_id");
                if (entidadeIdBanco != null) {
                    log.setEntidadeId(entidadeIdBanco.longValue());
                }

                log.setDetalhes(rs.getString("detalhes"));

                Timestamp dataHora = rs.getTimestamp("data_hora");
                if (dataHora != null) {
                    log.setDataHora(dataHora.toLocalDateTime());
                }

                logs.add(log);
            }

            return logs;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar logs: " + e.getMessage(), e);
        }
    }
}