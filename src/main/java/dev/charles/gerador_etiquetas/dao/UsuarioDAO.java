package dev.charles.gerador_etiquetas.dao;

import dev.charles.gerador_etiquetas.model.Usuario;
import dev.charles.gerador_etiquetas.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void salvar(Usuario usuario) {
        String sql = """
                INSERT INTO usuario (nome, login, senha, email, telefone)
                VALUES (?, ?, ?, ?, ?)
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário.", e);
        }
    }

    public Usuario autenticar(String email, String senha) {
        String sql = """
                SELECT id, nome, login, senha, email, telefone
                FROM usuario
                WHERE email = ? AND senha = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarUsuario(rs);
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar usuário.", e);
        }
    }

    public Usuario buscarPorLogin(String login) {
        String sql = """
                SELECT id, nome, login, senha, email, telefone
                FROM usuario
                WHERE login = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, login);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarUsuario(rs);
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por login.", e);
        }
    }

    public List<Usuario> listar() {
        String sql = """
                SELECT id, nome, login, senha, email, telefone
                FROM usuario
                ORDER BY id
                """;

        List<Usuario> usuarios = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                usuarios.add(montarUsuario(rs));
            }

            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários.", e);
        }
    }

    private Usuario montarUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setLogin(rs.getString("login"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTelefone(rs.getString("telefone"));
        return usuario;
    }
}
