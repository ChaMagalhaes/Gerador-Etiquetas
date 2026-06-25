package dev.charles.gerador_etiquetas.dao;

import dev.charles.gerador_etiquetas.model.EtiquetaCodigoOriginal;
import dev.charles.gerador_etiquetas.model.Fabricante;
import dev.charles.gerador_etiquetas.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtiquetaCodigoOriginalDAO {

    public EtiquetaCodigoOriginal salvar(EtiquetaCodigoOriginal codigoOriginal) {
        String sql = """
                INSERT INTO etiqueta_codigo_original 
                (etiqueta_id, codigo_original, fabricante_id)
                VALUES (?, ?, ?)
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setLong(1, codigoOriginal.getEtiquetaId());
            stmt.setString(2, codigoOriginal.getCodigoOriginal());

            if (codigoOriginal.getFabricante() != null && codigoOriginal.getFabricante().getId() != null) {
                stmt.setLong(3, codigoOriginal.getFabricante().getId());
            } else {
                stmt.setNull(3, Types.BIGINT);
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                codigoOriginal.setId(rs.getLong(1));
            }

            return codigoOriginal;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar código original da etiqueta.", e);
        }
    }

    public void salvarTodosPorEtiqueta(Long etiquetaId, List<EtiquetaCodigoOriginal> codigosOriginais) {
        if (codigosOriginais == null) {
            return;
        }

        for (EtiquetaCodigoOriginal codigoOriginal : codigosOriginais) {
            codigoOriginal.setEtiquetaId(etiquetaId);
            salvar(codigoOriginal);
        }
    }

    public void atualizar(EtiquetaCodigoOriginal codigoOriginal) {
        String sql = """
                UPDATE etiqueta_codigo_original
                SET codigo_original = ?, fabricante_id = ?
                WHERE id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, codigoOriginal.getCodigoOriginal());

            if (codigoOriginal.getFabricante() != null && codigoOriginal.getFabricante().getId() != null) {
                stmt.setLong(2, codigoOriginal.getFabricante().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }

            stmt.setLong(3, codigoOriginal.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar código original da etiqueta.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM etiqueta_codigo_original WHERE id = ?";

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir código original da etiqueta.", e);
        }
    }

    public void excluirPorEtiqueta(Long etiquetaId) {
        String sql = "DELETE FROM etiqueta_codigo_original WHERE etiqueta_id = ?";

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, etiquetaId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir códigos originais da etiqueta.", e);
        }
    }

    public List<EtiquetaCodigoOriginal> listarPorEtiqueta(Long etiquetaId) {
        String sql = """
                SELECT 
                    eco.id,
                    eco.etiqueta_id,
                    eco.codigo_original,
                    f.id AS fabricante_id,
                    f.nome AS fabricante_nome,
                    f.tipo AS fabricante_tipo,
                    f.ativo AS fabricante_ativo
                FROM etiqueta_codigo_original eco
                LEFT JOIN fabricante f ON eco.fabricante_id = f.id
                WHERE eco.etiqueta_id = ?
                ORDER BY eco.id
                """;

        List<EtiquetaCodigoOriginal> codigos = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, etiquetaId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                codigos.add(montarCodigoOriginal(rs));
            }

            return codigos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar códigos originais da etiqueta.", e);
        }
    }

    public EtiquetaCodigoOriginal buscarPorId(Long id) {
        String sql = """
                SELECT 
                    eco.id,
                    eco.etiqueta_id,
                    eco.codigo_original,
                    f.id AS fabricante_id,
                    f.nome AS fabricante_nome,
                    f.tipo AS fabricante_tipo,
                    f.ativo AS fabricante_ativo
                FROM etiqueta_codigo_original eco
                LEFT JOIN fabricante f ON eco.fabricante_id = f.id
                WHERE eco.id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarCodigoOriginal(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar código original da etiqueta.", e);
        }
    }

    private EtiquetaCodigoOriginal montarCodigoOriginal(ResultSet rs) throws SQLException {
        EtiquetaCodigoOriginal codigoOriginal = new EtiquetaCodigoOriginal();

        codigoOriginal.setId(rs.getLong("id"));
        codigoOriginal.setEtiquetaId(rs.getLong("etiqueta_id"));
        codigoOriginal.setCodigoOriginal(rs.getString("codigo_original"));

        Long fabricanteId = rs.getObject("fabricante_id", Long.class);

        if (fabricanteId != null) {
            Fabricante fabricante = new Fabricante();
            fabricante.setId(fabricanteId);
            fabricante.setNome(rs.getString("fabricante_nome"));
            fabricante.setTipo(rs.getString("fabricante_tipo"));
            fabricante.setAtivo(rs.getBoolean("fabricante_ativo"));

            codigoOriginal.setFabricante(fabricante);
        }

        return codigoOriginal;
    }
}