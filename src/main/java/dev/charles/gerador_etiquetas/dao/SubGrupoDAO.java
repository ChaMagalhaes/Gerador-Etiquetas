package dev.charles.gerador_etiquetas.dao;

import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.SubGrupo;
import dev.charles.gerador_etiquetas.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubGrupoDAO {

    public SubGrupo salvar(SubGrupo subgrupo) {
        String sql = """
                INSERT INTO subgrupo (grupo_id, descricao)
                VALUES (?, ?)
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setLong(1, subgrupo.getGrupo().getId());
            stmt.setString(2, subgrupo.getDescricao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                subgrupo.setId(rs.getLong(1));
            }

            return subgrupo;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar subgrupo.", e);
        }
    }

    public void atualizar(SubGrupo subgrupo) {
        String sql = """
                UPDATE subgrupo
                SET grupo_id = ?, descricao = ?
                WHERE id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, subgrupo.getGrupo().getId());
            stmt.setString(2, subgrupo.getDescricao());
            stmt.setLong(3, subgrupo.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar subgrupo.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM subgrupo WHERE id = ?";

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir subgrupo.", e);
        }
    }

    public List<SubGrupo> listar() {
        String sql = """
                SELECT 
                    s.id,
                    s.descricao,
                    g.id AS grupo_id,
                    g.descricao AS grupo_descricao
                FROM subgrupo s
                INNER JOIN grupo g ON s.grupo_id = g.id
                ORDER BY g.descricao, s.descricao
                """;

        return executarConsulta(sql, null);
    }

    public List<SubGrupo> listarPorGrupo(Long grupoId) {
        String sql = """
                SELECT 
                    s.id,
                    s.descricao,
                    g.id AS grupo_id,
                    g.descricao AS grupo_descricao
                FROM subgrupo s
                INNER JOIN grupo g ON s.grupo_id = g.id
                WHERE s.grupo_id = ?
                ORDER BY s.descricao
                """;

        return executarConsulta(sql, grupoId);
    }

    public SubGrupo buscarPorId(Long id) {
        String sql = """
                SELECT 
                    s.id,
                    s.descricao,
                    g.id AS grupo_id,
                    g.descricao AS grupo_descricao
                FROM subgrupo s
                INNER JOIN grupo g ON s.grupo_id = g.id
                WHERE s.id = ?
                """;

        List<SubGrupo> resultado = executarConsulta(sql, id);

        if (resultado.isEmpty()) {
            return null;
        }

        return resultado.get(0);
    }

    private List<SubGrupo> executarConsulta(String sql, Long parametro) {
        List<SubGrupo> subgrupos = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            if (parametro != null) {
                stmt.setLong(1, parametro);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                subgrupos.add(montarSubgrupo(rs));
            }

            return subgrupos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar subgrupos.", e);
        }
    }

    private SubGrupo montarSubgrupo(ResultSet rs) throws SQLException {
        Grupo grupo = new Grupo();
        grupo.setId(rs.getLong("grupo_id"));
        grupo.setDescricao(rs.getString("grupo_descricao"));

        SubGrupo subgrupo = new SubGrupo();
        subgrupo.setId(rs.getLong("id"));
        subgrupo.setDescricao(rs.getString("descricao"));
        subgrupo.setGrupo(grupo);

        return subgrupo;
    }
}