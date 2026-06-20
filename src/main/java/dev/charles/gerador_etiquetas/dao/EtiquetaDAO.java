package dev.charles.gerador_etiquetas.dao;

import dev.charles.gerador_etiquetas.model.Etiqueta;
import dev.charles.gerador_etiquetas.model.Prateleira;
import dev.charles.gerador_etiquetas.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtiquetaDAO {

    public void salvar(Etiqueta etiqueta) {
        String sqlEtiqueta = """
                INSERT INTO etiqueta (
                    prateleira_id,
                    descricao,
                    codigo_venda,
                    data_criacao,
                    largura_cm,
                    altura_cm
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String sqlCodigoOriginal = """
                INSERT INTO etiqueta_codigo_original (etiqueta_id, codigo_original)
                VALUES (?, ?)
                """;

        Connection conn = Conexao.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            PreparedStatement stmtEtiqueta = conn.prepareStatement(sqlEtiqueta, Statement.RETURN_GENERATED_KEYS);
            stmtEtiqueta.setLong(1, etiqueta.getPrateleira().getId());
            stmtEtiqueta.setString(2, etiqueta.getDescricao());
            stmtEtiqueta.setString(3, etiqueta.getCodigoVenda());
            stmtEtiqueta.setTimestamp(4, Timestamp.valueOf(etiqueta.getDataCriacao()));
            stmtEtiqueta.setDouble(5, etiqueta.getLarguraCm());
            stmtEtiqueta.setDouble(6, etiqueta.getAlturaCm());
            stmtEtiqueta.executeUpdate();

            ResultSet rs = stmtEtiqueta.getGeneratedKeys();

            if (rs.next()) {
                Long etiquetaId = rs.getLong(1);
                etiqueta.setId(etiquetaId);
                salvarCodigosOriginais(conn, etiquetaId, etiqueta.getCodigosOriginais(), sqlCodigoOriginal);
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            throw new RuntimeException("Erro ao salvar etiqueta.", e);
        } finally {
            restaurarAutoCommit(conn);
        }
    }

    public void atualizar(Etiqueta etiqueta) {
        String sqlPrateleira = """
                UPDATE prateleira
                SET local_prateleira = ?, descricao_grupo = ?
                WHERE id = ?
                """;

        String sqlEtiqueta = """
                UPDATE etiqueta
                SET descricao = ?, codigo_venda = ?, largura_cm = ?, altura_cm = ?
                WHERE id = ?
                """;

        String sqlExcluirCodigos = """
                DELETE FROM etiqueta_codigo_original
                WHERE etiqueta_id = ?
                """;

        String sqlInserirCodigo = """
                INSERT INTO etiqueta_codigo_original (etiqueta_id, codigo_original)
                VALUES (?, ?)
                """;

        Connection conn = Conexao.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            PreparedStatement stmtPrateleira = conn.prepareStatement(sqlPrateleira);
            stmtPrateleira.setString(1, etiqueta.getPrateleira().getLocalPrateleira());
            stmtPrateleira.setString(2, etiqueta.getPrateleira().getDescricaoGrupo());
            stmtPrateleira.setLong(3, etiqueta.getPrateleira().getId());
            stmtPrateleira.executeUpdate();

            PreparedStatement stmtEtiqueta = conn.prepareStatement(sqlEtiqueta);
            stmtEtiqueta.setString(1, etiqueta.getDescricao());
            stmtEtiqueta.setString(2, etiqueta.getCodigoVenda());
            stmtEtiqueta.setDouble(3, etiqueta.getLarguraCm());
            stmtEtiqueta.setDouble(4, etiqueta.getAlturaCm());
            stmtEtiqueta.setLong(5, etiqueta.getId());
            stmtEtiqueta.executeUpdate();

            PreparedStatement stmtExcluirCodigos = conn.prepareStatement(sqlExcluirCodigos);
            stmtExcluirCodigos.setLong(1, etiqueta.getId());
            stmtExcluirCodigos.executeUpdate();

            salvarCodigosOriginais(conn, etiqueta.getId(), etiqueta.getCodigosOriginais(), sqlInserirCodigo);

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            throw new RuntimeException("Erro ao atualizar etiqueta.", e);
        } finally {
            restaurarAutoCommit(conn);
        }
    }

    public void excluir(Long id) {
        String sql = """
                DELETE FROM etiqueta
                WHERE id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir etiqueta.", e);
        }
    }

    public List<Etiqueta> listar() {
        String sql = sqlBase() + " ORDER BY e.id";
        return executarConsultaLista(sql, null);
    }

    public Etiqueta buscarPorId(Long id) {
        String sql = sqlBase() + " WHERE e.id = ?";
        List<Etiqueta> etiquetas = executarConsultaLista(sql, id);
        return etiquetas.isEmpty() ? null : etiquetas.get(0);
    }

    public List<Etiqueta> pesquisarPorTipo(String tipoBusca, String termo) {
        String sql;

        switch (tipoBusca) {
            case "DESCRICAO":
                sql = sqlBase() + " WHERE LOWER(e.descricao) LIKE LOWER(?) ORDER BY e.id";
                break;
            case "CODIGO_VENDA":
                sql = sqlBase() + " WHERE LOWER(e.codigo_venda) LIKE LOWER(?) ORDER BY e.id";
                break;
            case "CODIGO_ORIGINAL":
                sql = sqlBase() + """
                         WHERE EXISTS (
                            SELECT 1
                            FROM etiqueta_codigo_original eco
                            WHERE eco.etiqueta_id = e.id
                            AND LOWER(eco.codigo_original) LIKE LOWER(?)
                         )
                         ORDER BY e.id
                        """;
                break;
            default:
                throw new RuntimeException("Tipo de busca inválido.");
        }

        return executarConsultaLista(sql, "%" + termo + "%");
    }

    private String sqlBase() {
        return """
                SELECT 
                    e.id AS etiqueta_id,
                    e.descricao,
                    e.codigo_venda,
                    e.data_criacao,
                    e.largura_cm,
                    e.altura_cm,
                    p.id AS prateleira_id,
                    p.local_prateleira,
                    p.descricao_grupo
                FROM etiqueta e
                INNER JOIN prateleira p ON e.prateleira_id = p.id
                """;
    }

    private List<Etiqueta> executarConsultaLista(String sql, Object parametro) {
        List<Etiqueta> etiquetas = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            if (parametro != null) {
                if (parametro instanceof Long) {
                    stmt.setLong(1, (Long) parametro);
                } else {
                    stmt.setString(1, parametro.toString());
                }
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                etiquetas.add(montarEtiqueta(rs));
            }

            return etiquetas;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar etiquetas.", e);
        }
    }

    private Etiqueta montarEtiqueta(ResultSet rs) throws SQLException {
        Prateleira prateleira = new Prateleira();
        prateleira.setId(rs.getLong("prateleira_id"));
        prateleira.setLocalPrateleira(rs.getString("local_prateleira"));
        prateleira.setDescricaoGrupo(rs.getString("descricao_grupo"));

        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setId(rs.getLong("etiqueta_id"));
        etiqueta.setDescricao(rs.getString("descricao"));
        etiqueta.setCodigoVenda(rs.getString("codigo_venda"));
        etiqueta.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        etiqueta.setLarguraCm(rs.getDouble("largura_cm"));
        etiqueta.setAlturaCm(rs.getDouble("altura_cm"));
        etiqueta.setPrateleira(prateleira);
        etiqueta.setCodigosOriginais(buscarCodigosOriginais(etiqueta.getId()));

        return etiqueta;
    }

    private List<String> buscarCodigosOriginais(Long etiquetaId) {
        String sql = """
                SELECT codigo_original
                FROM etiqueta_codigo_original
                WHERE etiqueta_id = ?
                ORDER BY id
                """;

        List<String> codigos = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, etiquetaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                codigos.add(rs.getString("codigo_original"));
            }

            return codigos;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar códigos originais da etiqueta.", e);
        }
    }

    private void salvarCodigosOriginais(Connection conn, Long etiquetaId, List<String> codigosOriginais, String sql) throws SQLException {
        if (codigosOriginais == null || codigosOriginais.isEmpty()) {
            return;
        }

        PreparedStatement stmtCodigo = conn.prepareStatement(sql);

        for (String codigoOriginal : codigosOriginais) {
            if (codigoOriginal != null && !codigoOriginal.isBlank()) {
                stmtCodigo.setLong(1, etiquetaId);
                stmtCodigo.setString(2, codigoOriginal.trim());
                stmtCodigo.addBatch();
            }
        }

        stmtCodigo.executeBatch();
    }

    private void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desfazer operação.", e);
        }
    }

    private void restaurarAutoCommit(Connection conn) {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao restaurar autoCommit.", e);
        }
    }
}
