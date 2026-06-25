package dev.charles.gerador_etiquetas.dao;

import dev.charles.gerador_etiquetas.model.Etiqueta;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.Prateleira;
import dev.charles.gerador_etiquetas.model.SubGrupo;
import dev.charles.gerador_etiquetas.util.Conexao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EtiquetaDAO {

    private final EtiquetaCodigoOriginalDAO codigoOriginalDAO;

    public EtiquetaDAO() {
        this.codigoOriginalDAO = new EtiquetaCodigoOriginalDAO();
    }

    public Etiqueta salvar(Etiqueta etiqueta) {
        String sql = """
                INSERT INTO etiqueta
                (prateleira_id, grupo_id, subgrupo_id, descricao, codigo_venda, data_criacao, largura_cm, altura_cm)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preencherIdsRelacionamentos(stmt, etiqueta);

            stmt.setString(4, etiqueta.getDescricao());
            stmt.setString(5, etiqueta.getCodigoVenda());

            LocalDateTime dataCriacao = etiqueta.getDataCriacao() != null
                    ? etiqueta.getDataCriacao()
                    : LocalDateTime.now();

            stmt.setTimestamp(6, Timestamp.valueOf(dataCriacao));
            stmt.setDouble(7, etiqueta.getLarguraCm());
            stmt.setDouble(8, etiqueta.getAlturaCm());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                etiqueta.setId(rs.getLong(1));
            }

            codigoOriginalDAO.salvarTodosPorEtiqueta(
                    etiqueta.getId(),
                    etiqueta.getCodigosOriginais()
            );

            return etiqueta;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar etiqueta: " + e.getMessage(), e);
        }
    }

    public void atualizar(Etiqueta etiqueta) {
        String sql = """
                UPDATE etiqueta
                SET prateleira_id = ?, 
                    grupo_id = ?, 
                    subgrupo_id = ?, 
                    descricao = ?, 
                    codigo_venda = ?, 
                    largura_cm = ?, 
                    altura_cm = ?
                WHERE id = ?
                """;

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            preencherIdsRelacionamentos(stmt, etiqueta);

            stmt.setString(4, etiqueta.getDescricao());
            stmt.setString(5, etiqueta.getCodigoVenda());
            stmt.setDouble(6, etiqueta.getLarguraCm());
            stmt.setDouble(7, etiqueta.getAlturaCm());
            stmt.setLong(8, etiqueta.getId());

            stmt.executeUpdate();

            codigoOriginalDAO.excluirPorEtiqueta(etiqueta.getId());

            codigoOriginalDAO.salvarTodosPorEtiqueta(
                    etiqueta.getId(),
                    etiqueta.getCodigosOriginais()
            );

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar etiqueta: " + e.getMessage(), e);
        }
    }

    private void preencherIdsRelacionamentos(PreparedStatement stmt, Etiqueta etiqueta) throws SQLException {
        if (etiqueta.getPrateleira() != null && etiqueta.getPrateleira().getId() != null) {
            stmt.setLong(1, etiqueta.getPrateleira().getId());
        } else {
            stmt.setNull(1, Types.BIGINT);
        }

        if (etiqueta.getGrupo() != null && etiqueta.getGrupo().getId() != null) {
            stmt.setLong(2, etiqueta.getGrupo().getId());
        } else {
            stmt.setNull(2, Types.BIGINT);
        }

        if (etiqueta.getSubGrupo() != null && etiqueta.getSubGrupo().getId() != null) {
            stmt.setLong(3, etiqueta.getSubGrupo().getId());
        } else {
            stmt.setNull(3, Types.BIGINT);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM etiqueta WHERE id = ?";

        try {
            codigoOriginalDAO.excluirPorEtiqueta(id);

            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir etiqueta: " + e.getMessage(), e);
        }
    }

    public List<Etiqueta> listar() {
        String sql = consultaBase() + """
            ORDER BY e.id ASC
            """;

        return executarConsultaSemParametro(sql);
    }

    public Etiqueta buscarPorId(Long id) {
        String sql = consultaBase() + """
                WHERE e.id = ?
                """;

        List<Etiqueta> resultado = executarConsultaLong(sql, id);

        return resultado.isEmpty() ? null : resultado.get(0);
    }

    public List<Etiqueta> buscarPorDescricao(String descricao) {
        String sql = consultaBase() + """
                WHERE LOWER(e.descricao) LIKE LOWER(?)
                ORDER BY e.descricao
                """;

        return executarConsultaTexto(sql, "%" + descricao + "%");
    }

    public List<Etiqueta> buscarPorCodigoVenda(String codigoVenda) {
        String sql = consultaBase() + """
                WHERE LOWER(e.codigo_venda) LIKE LOWER(?)
                ORDER BY e.descricao
                """;

        return executarConsultaTexto(sql, "%" + codigoVenda + "%");
    }

    public List<Etiqueta> buscarPorCodigoOriginal(String codigoOriginal) {
        String sql = consultaBase() + """
                INNER JOIN etiqueta_codigo_original eco ON eco.etiqueta_id = e.id
                WHERE LOWER(eco.codigo_original) LIKE LOWER(?)
                ORDER BY e.descricao
                """;

        return executarConsultaTexto(sql, "%" + codigoOriginal + "%");
    }

    public List<Etiqueta> buscarPorFabricante(String fabricante) {
        String sql = consultaBase() + """
                INNER JOIN etiqueta_codigo_original eco ON eco.etiqueta_id = e.id
                INNER JOIN fabricante f ON eco.fabricante_id = f.id
                WHERE LOWER(f.nome) LIKE LOWER(?)
                ORDER BY e.descricao
                """;

        return executarConsultaTexto(sql, "%" + fabricante + "%");
    }

    public List<Etiqueta> buscarPorGrupo(Long grupoId) {
        String sql = consultaBase() + """
                WHERE e.grupo_id = ?
                ORDER BY e.descricao
                """;

        return executarConsultaLong(sql, grupoId);
    }

    public List<Etiqueta> buscarPorSubGrupo(Long subgrupoId) {
        String sql = consultaBase() + """
                WHERE e.subgrupo_id = ?
                ORDER BY e.descricao
                """;

        return executarConsultaLong(sql, subgrupoId);
    }

    public List<Etiqueta> pesquisarPorTipo(String tipoBusca, String termo) {
        if (tipoBusca == null || tipoBusca.isBlank()) {
            throw new RuntimeException("Tipo de busca obrigatório.");
        }

        if (termo == null || termo.isBlank()) {
            return listar();
        }

        String tipo = tipoBusca.trim().toLowerCase();
        String termoTratado = termo.trim();

        return switch (tipo) {
            case "descricao", "descrição", "produto" -> buscarPorDescricao(termoTratado);

            case "codigo_venda", "codigo venda", "código venda", "codigo de venda", "código de venda", "venda" ->
                    buscarPorCodigoVenda(termoTratado);

            case "codigo_original", "codigo original", "código original", "original" ->
                    buscarPorCodigoOriginal(termoTratado);

            case "fabricante", "marca" ->
                    buscarPorFabricante(termoTratado);

            default -> throw new RuntimeException("Tipo de busca inválido: " + tipoBusca);
        };
    }

    private String consultaBase() {
        return """
                SELECT DISTINCT
                    e.id,
                    e.prateleira_id,
                    e.grupo_id,
                    e.subgrupo_id,
                    e.descricao,
                    e.codigo_venda,
                    e.data_criacao,
                    e.largura_cm,
                    e.altura_cm,

                    p.local_prateleira AS prateleira_local,
                    p.descricao_grupo AS prateleira_descricao_grupo,

                    g.descricao AS grupo_descricao,

                    sg.descricao AS subgrupo_descricao

                FROM etiqueta e
                LEFT JOIN prateleira p ON e.prateleira_id = p.id
                LEFT JOIN grupo g ON e.grupo_id = g.id
                LEFT JOIN subgrupo sg ON e.subgrupo_id = sg.id
                """;
    }

    private List<Etiqueta> executarConsultaSemParametro(String sql) {
        List<Etiqueta> etiquetas = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                etiquetas.add(montarEtiqueta(rs));
            }

            return etiquetas;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar etiquetas: " + e.getMessage(), e);
        }
    }

    private List<Etiqueta> executarConsultaLong(String sql, Long parametro) {
        List<Etiqueta> etiquetas = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, parametro);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                etiquetas.add(montarEtiqueta(rs));
            }

            return etiquetas;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar etiquetas: " + e.getMessage(), e);
        }
    }

    private List<Etiqueta> executarConsultaTexto(String sql, String parametro) {
        List<Etiqueta> etiquetas = new ArrayList<>();

        try {
            Connection conn = Conexao.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, parametro);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                etiquetas.add(montarEtiqueta(rs));
            }

            return etiquetas;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar etiquetas: " + e.getMessage(), e);
        }
    }

    private Etiqueta montarEtiqueta(ResultSet rs) throws SQLException {
        Etiqueta etiqueta = new Etiqueta();

        etiqueta.setId(rs.getLong("id"));
        etiqueta.setDescricao(rs.getString("descricao"));
        etiqueta.setCodigoVenda(rs.getString("codigo_venda"));

        Long prateleiraId = getLongOuNull(rs, "prateleira_id");

        if (prateleiraId != null) {
            Prateleira prateleira = new Prateleira();

            prateleira.setId(prateleiraId);
            prateleira.setLocalPrateleira(rs.getString("prateleira_local"));
            prateleira.setDescricaoGrupo(rs.getString("prateleira_descricao_grupo"));

            etiqueta.setPrateleira(prateleira);
        }

        Long grupoId = getLongOuNull(rs, "grupo_id");

        if (grupoId != null) {
            Grupo grupo = new Grupo();

            grupo.setId(grupoId);
            grupo.setDescricao(rs.getString("grupo_descricao"));

            etiqueta.setGrupo(grupo);
        }

        Long subgrupoId = getLongOuNull(rs, "subgrupo_id");

        if (subgrupoId != null) {
            SubGrupo subGrupo = new SubGrupo();

            subGrupo.setId(subgrupoId);
            subGrupo.setDescricao(rs.getString("subgrupo_descricao"));
            subGrupo.setGrupo(etiqueta.getGrupo());

            etiqueta.setSubGrupo(subGrupo);
        }

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");

        if (dataCriacao != null) {
            etiqueta.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        etiqueta.setLarguraCm(rs.getDouble("largura_cm"));
        etiqueta.setAlturaCm(rs.getDouble("altura_cm"));

        etiqueta.setCodigosOriginais(
                codigoOriginalDAO.listarPorEtiqueta(etiqueta.getId())
        );

        return etiqueta;
    }

    private Long getLongOuNull(ResultSet rs, String coluna) throws SQLException {
        Number valor = (Number) rs.getObject(coluna);
        return valor != null ? valor.longValue() : null;
    }
}