package dev.charles.gerador_etiquetas.bo;

import dev.charles.gerador_etiquetas.dao.EtiquetaCodigoOriginalDAO;
import dev.charles.gerador_etiquetas.model.EtiquetaCodigoOriginal;

import java.util.List;

public class EtiquetaCodigoOriginalBO {

    private final EtiquetaCodigoOriginalDAO codigoOriginalDAO;
    private final LogAcaoBO logAcaoBO;

    public EtiquetaCodigoOriginalBO() {
        this.codigoOriginalDAO = new EtiquetaCodigoOriginalDAO();
        this.logAcaoBO = new LogAcaoBO();
    }

    public EtiquetaCodigoOriginal cadastrarCodigoOriginal(EtiquetaCodigoOriginal codigoOriginal) {
        validar(codigoOriginal);

        codigoOriginal.setCodigoOriginal(codigoOriginal.getCodigoOriginal().trim());

        EtiquetaCodigoOriginal codigoSalvo = codigoOriginalDAO.salvar(codigoOriginal);

        logAcaoBO.registrar(
                "CADASTRAR",
                "CODIGO_ORIGINAL",
                codigoSalvo.getId(),
                "Cadastrou código original "
                        + codigoSalvo.getCodigoOriginal()
                        + " para etiqueta ID "
                        + codigoSalvo.getEtiquetaId()
        );

        return codigoSalvo;
    }

    public void atualizarCodigoOriginal(EtiquetaCodigoOriginal codigoOriginal) {
        if (codigoOriginal == null || codigoOriginal.getId() == null) {
            throw new RuntimeException("Código original inválido para atualização.");
        }

        validarCodigo(codigoOriginal.getCodigoOriginal());

        codigoOriginal.setCodigoOriginal(codigoOriginal.getCodigoOriginal().trim());

        codigoOriginalDAO.atualizar(codigoOriginal);

        logAcaoBO.registrar(
                "ALTERAR",
                "CODIGO_ORIGINAL",
                codigoOriginal.getId(),
                "Alterou código original para: " + codigoOriginal.getCodigoOriginal()
        );
    }

    public void excluirCodigoOriginal(Long id) {
        if (id == null) {
            throw new RuntimeException("ID do código original é obrigatório.");
        }

        EtiquetaCodigoOriginal codigoOriginal = codigoOriginalDAO.buscarPorId(id);

        codigoOriginalDAO.excluir(id);

        String codigo = codigoOriginal != null && codigoOriginal.getCodigoOriginal() != null
                ? codigoOriginal.getCodigoOriginal()
                : "ID " + id;

        logAcaoBO.registrar(
                "EXCLUIR",
                "CODIGO_ORIGINAL",
                id,
                "Excluiu código original: " + codigo
        );
    }

    public void excluirPorEtiqueta(Long etiquetaId) {
        if (etiquetaId == null) {
            throw new RuntimeException("ID da etiqueta é obrigatório.");
        }

        codigoOriginalDAO.excluirPorEtiqueta(etiquetaId);

        logAcaoBO.registrar(
                "EXCLUIR",
                "CODIGO_ORIGINAL",
                etiquetaId,
                "Excluiu todos os códigos originais da etiqueta ID: " + etiquetaId
        );
    }

    public List<EtiquetaCodigoOriginal> listarPorEtiqueta(Long etiquetaId) {
        if (etiquetaId == null) {
            throw new RuntimeException("ID da etiqueta é obrigatório.");
        }

        return codigoOriginalDAO.listarPorEtiqueta(etiquetaId);
    }

    public EtiquetaCodigoOriginal buscarPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("ID do código original é obrigatório.");
        }

        return codigoOriginalDAO.buscarPorId(id);
    }

    private void validar(EtiquetaCodigoOriginal codigoOriginal) {
        if (codigoOriginal == null) {
            throw new RuntimeException("Código original inválido.");
        }

        if (codigoOriginal.getEtiquetaId() == null) {
            throw new RuntimeException("A etiqueta é obrigatória.");
        }

        validarCodigo(codigoOriginal.getCodigoOriginal());
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new RuntimeException("O código original é obrigatório.");
        }

        String codigoTratado = codigo.trim();

        if (!codigoTratado.matches("\\d{1,12}")) {
            throw new RuntimeException("O código original deve ter somente números e no máximo 12 dígitos.");
        }
    }
}