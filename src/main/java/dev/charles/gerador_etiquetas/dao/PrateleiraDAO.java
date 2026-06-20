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

    public List<Prateleira> listar() {
        String sql = """
                SELECT id, local_prateleira, descricao_grupo
                FROM prateleira
                ORDER BY id
                """;

        List<Prateleira> prateleiras = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Prateleira prateleira = new Prateleira();
                prateleira.setId(rs.getLong("id"));
                prateleira.setLocalPrateleira(rs.getString("local_prateleira"));
                prateleira.setDescricaoGrupo(rs.getString("descricao_grupo"));
                prateleiras.add(prateleira);
            }

            return prateleiras;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar prateleiras.", e);
        }
    }
}
