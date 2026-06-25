package dev.charles.gerador_etiquetas.dao;

import dev.charles.gerador_etiquetas.model.Prateleira;
import dev.charles.gerador_etiquetas.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrateleiraDAO {

    public Prateleira salvar(Prateleira prateleira) {
        String sql = """
                INSERT INTO prateleira (local_prateleira, descricao_grupo)
                VALUES (?, ?)
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, prateleira.getLocalPrateleira());
            stmt.setString(2, prateleira.getDescricaoGrupo());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                prateleira.setId(rs.getLong(1));
            }

            return prateleira;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar prateleira.", e);
        }
    }

    public void atualizar(Prateleira prateleira) {
        String sql = """
                UPDATE prateleira
                SET local_prateleira = ?, descricao_grupo = ?
                WHERE id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, prateleira.getLocalPrateleira());
            stmt.setString(2, prateleira.getDescricaoGrupo());
            stmt.setLong(3, prateleira.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar prateleira.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM prateleira WHERE id = ?";

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir prateleira. Verifique se ela está sendo usada em alguma etiqueta.", e);
        }
    }

    public List<Prateleira> listar() {
        String sql = """
                SELECT id, local_prateleira, descricao_grupo
                FROM prateleira
                ORDER BY local_prateleira
                """;

        List<Prateleira> prateleiras = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                prateleiras.add(montarPrateleira(rs));
            }

            return prateleiras;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar prateleiras.", e);
        }
    }

    public Prateleira buscarPorId(Long id) {
        String sql = """
                SELECT id, local_prateleira, descricao_grupo
                FROM prateleira
                WHERE id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarPrateleira(rs);
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar prateleira.", e);
        }
    }

    private Prateleira montarPrateleira(ResultSet rs) throws SQLException {
        Prateleira prateleira = new Prateleira();
        prateleira.setId(rs.getLong("id"));
        prateleira.setLocalPrateleira(rs.getString("local_prateleira"));
        prateleira.setDescricaoGrupo(rs.getString("descricao_grupo"));
        return prateleira;
    }
}
