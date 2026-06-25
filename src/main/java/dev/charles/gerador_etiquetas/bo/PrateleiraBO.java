package dev.charles.gerador_etiquetas.bo;

import dev.charles.gerador_etiquetas.dao.PrateleiraDAO;
import dev.charles.gerador_etiquetas.model.Prateleira;

import java.util.List;

public class PrateleiraBO {

    private final PrateleiraDAO prateleiraDAO;
    private final LogAcaoBO logAcaoBO;

    public PrateleiraBO() {
        this.prateleiraDAO = new PrateleiraDAO();
        this.logAcaoBO = new LogAcaoBO();
    }

    public Prateleira cadastrarPrateleira(String localPrateleira, String descricaoGrupo) {
        validarDados(localPrateleira);

        Prateleira prateleira = new Prateleira();
        prateleira.setLocalPrateleira(localPrateleira.trim());
        prateleira.setDescricaoGrupo(descricaoGrupo != null ? descricaoGrupo.trim() : null);

        Prateleira prateleiraSalva = prateleiraDAO.salvar(prateleira);

        logAcaoBO.registrar(
                "CADASTRAR",
                "PRATELEIRA",
                prateleiraSalva.getId(),
                "Cadastrou prateleira: " + prateleiraSalva.getLocalPrateleira()
        );

        return prateleiraSalva;
    }

    public void atualizarPrateleira(Prateleira prateleira) {
        if (prateleira == null || prateleira.getId() == null) {
            throw new RuntimeException("Prateleira inválida para atualização.");
        }

        validarDados(prateleira.getLocalPrateleira());

        prateleira.setLocalPrateleira(prateleira.getLocalPrateleira().trim());

        if (prateleira.getDescricaoGrupo() != null) {
            prateleira.setDescricaoGrupo(prateleira.getDescricaoGrupo().trim());
        }

        prateleiraDAO.atualizar(prateleira);

        logAcaoBO.registrar(
                "ALTERAR",
                "PRATELEIRA",
                prateleira.getId(),
                "Alterou prateleira: " + prateleira.getLocalPrateleira()
        );
    }

    public void excluirPrateleira(Long id) {
        if (id == null) {
            throw new RuntimeException("ID da prateleira é obrigatório.");
        }

        Prateleira prateleira = prateleiraDAO.buscarPorId(id);

        prateleiraDAO.excluir(id);

        String localPrateleira = prateleira != null && prateleira.getLocalPrateleira() != null
                ? prateleira.getLocalPrateleira()
                : "ID " + id;

        logAcaoBO.registrar(
                "EXCLUIR",
                "PRATELEIRA",
                id,
                "Excluiu prateleira: " + localPrateleira
        );
    }

    public List<Prateleira> listarPrateleiras() {
        return prateleiraDAO.listar();
    }

    public Prateleira buscarPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("ID da prateleira é obrigatório.");
        }

        return prateleiraDAO.buscarPorId(id);
    }

    private void validarDados(String localPrateleira) {
        if (localPrateleira == null || localPrateleira.isBlank()) {
            throw new RuntimeException("O local da prateleira é obrigatório.");
        }
    }
}