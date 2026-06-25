package dev.charles.gerador_etiquetas.dao;

import dev.charles.gerador_etiquetas.model.Fabricante;
import dev.charles.gerador_etiquetas.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FabricanteDAO {

    public Fabricante salvar(Fabricante fabricante) {
        String sql = """
                INSERT INTO fabricante (nome, tipo, ativo)
                VALUES (?, ?, ?)
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, fabricante.getNome());
            stmt.setString(2, fabricante.getTipo());
            stmt.setBoolean(3, fabricante.getAtivo() != null ? fabricante.getAtivo() : true);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                fabricante.setId(rs.getLong(1));
            }

            return fabricante;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar fabricante.", e);
        }
    }

    public void atualizar(Fabricante fabricante) {
        String sql = """
                UPDATE fabricante
                SET nome = ?, tipo = ?, ativo = ?
                WHERE id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, fabricante.getNome());
            stmt.setString(2, fabricante.getTipo());
            stmt.setBoolean(3, fabricante.getAtivo() != null ? fabricante.getAtivo() : true);
            stmt.setLong(4, fabricante.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar fabricante.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM fabricante WHERE id = ?";

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir fabricante.", e);
        }
    }

    public void inativar(Long id) {
        String sql = """
                UPDATE fabricante
                SET ativo = false
                WHERE id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inativar fabricante.", e);
        }
    }

    public List<Fabricante> listar() {
        String sql = """
                SELECT id, nome, tipo, ativo
                FROM fabricante
                ORDER BY nome
                """;

        List<Fabricante> fabricantes = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                fabricantes.add(montarFabricante(rs));
            }

            return fabricantes;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar fabricantes.", e);
        }
    }

    public List<Fabricante> listarAtivos() {
        String sql = """
                SELECT id, nome, tipo, ativo
                FROM fabricante
                WHERE ativo = true
                ORDER BY nome
                """;

        List<Fabricante> fabricantes = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                fabricantes.add(montarFabricante(rs));
            }

            return fabricantes;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar fabricantes ativos.", e);
        }
    }

    public Fabricante buscarPorId(Long id) {
        String sql = """
                SELECT id, nome, tipo, ativo
                FROM fabricante
                WHERE id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarFabricante(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar fabricante.", e);
        }
    }

    public List<Fabricante> buscarPorNome(String nome) {
        String sql = """
                SELECT id, nome, tipo, ativo
                FROM fabricante
                WHERE LOWER(nome) LIKE LOWER(?)
                ORDER BY nome
                """;

        List<Fabricante> fabricantes = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, "%" + nome + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                fabricantes.add(montarFabricante(rs));
            }

            return fabricantes;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar fabricante por nome.", e);
        }
    }

    private Fabricante montarFabricante(ResultSet rs) throws SQLException {
        Fabricante fabricante = new Fabricante();

        fabricante.setId(rs.getLong("id"));
        fabricante.setNome(rs.getString("nome"));
        fabricante.setTipo(rs.getString("tipo"));
        fabricante.setAtivo(rs.getBoolean("ativo"));

        return fabricante;
    }
}