package dev.charles.gerador_etiquetas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static Conexao instancia;
    private Connection conn;

    private final String driver = "org.postgresql.Driver";
    private final String url = System.getenv("DB_URL");
    private final String usuario = System.getenv("DB_USER");
    private final String senha = System.getenv("DB_PASSWORD");

    private Conexao() {
        validarVariaveisAmbiente();

        try {
            Class.forName(driver);
            this.conn = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conectado ao banco com sucesso!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do PostgreSQL não encontrado.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados.", e);
        }
    }

    public static Conexao getInstance() {
        if (instancia == null) {
            instancia = new Conexao();
        }

        return instancia;
    }

    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, usuario, senha);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao recuperar conexão com o banco.", e);
        }

        return conn;
    }

    private void validarVariaveisAmbiente() {
        if (url == null || url.isBlank()) {
            throw new RuntimeException("Variável de ambiente DB_URL não configurada.");
        }

        if (usuario == null || usuario.isBlank()) {
            throw new RuntimeException("Variável de ambiente DB_USER não configurada.");
        }

        if (senha == null || senha.isBlank()) {
            throw new RuntimeException("Variável de ambiente DB_PASSWORD não configurada.");
        }
    }
}
