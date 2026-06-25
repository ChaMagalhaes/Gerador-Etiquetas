package dev.charles.gerador_etiquetas.dao;

import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAO {

    public Grupo salvar(Grupo grupo) {
        String sql = """
                INSERT INTO grupo (descricao)
                VALUES (?)
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, grupo.getDescricao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                grupo.setId(rs.getLong(1));
            }

            return grupo;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar grupo.", e);
        }
    }

    public void atualizar(Grupo grupo) {
        String sql = """
                UPDATE grupo
                SET descricao = ?
                WHERE id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, grupo.getDescricao());
            stmt.setLong(2, grupo.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar grupo.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM grupo WHERE id = ?";

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir grupo.", e);
        }
    }

    public List<Grupo> listar() {
        String sql = "SELECT id, descricao FROM grupo ORDER BY descricao";

        List<Grupo> grupos = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                grupos.add(montarGrupo(rs));
            }

            return grupos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar grupos.", e);
        }
    }

    public Grupo buscarPorId(Long id) {
        String sql = "SELECT id, descricao FROM grupo WHERE id = ?";

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarGrupo(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar grupo.", e);
        }
    }

    private Grupo montarGrupo(ResultSet rs) throws SQLException {
        Grupo grupo = new Grupo();

        grupo.setId(rs.getLong("id"));
        grupo.setDescricao(rs.getString("descricao"));

        return grupo;
    }
}