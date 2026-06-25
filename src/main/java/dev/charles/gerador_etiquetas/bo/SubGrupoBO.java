package dev.charles.gerador_etiquetas.bo;

import dev.charles.gerador_etiquetas.dao.SubGrupoDAO;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.SubGrupo;

import java.util.List;

public class SubGrupoBO {

    private final SubGrupoDAO subgrupoDAO;
    private final LogAcaoBO logAcaoBO;

    public SubGrupoBO() {
        this.subgrupoDAO = new SubGrupoDAO();
        this.logAcaoBO = new LogAcaoBO();
    }

    public SubGrupo cadastrarSubgrupo(Grupo grupo, String descricao) {
        if (grupo == null || grupo.getId() == null) {
            throw new RuntimeException("O grupo do subgrupo é obrigatório.");
        }

        validar(descricao);

        SubGrupo subgrupo = new SubGrupo();
        subgrupo.setGrupo(grupo);
        subgrupo.setDescricao(descricao.trim());

        SubGrupo subgrupoSalvo = subgrupoDAO.salvar(subgrupo);

        String nomeGrupo = grupo.getDescricao() != null ? grupo.getDescricao() : "ID " + grupo.getId();

        logAcaoBO.registrar(
                "CADASTRAR",
                "SUBGRUPO",
                subgrupoSalvo.getId(),
                "Cadastrou subgrupo: " + subgrupoSalvo.getDescricao() + " no grupo: " + nomeGrupo
        );

        return subgrupoSalvo;
    }

    public void atualizarSubgrupo(SubGrupo subgrupo) {
        if (subgrupo == null || subgrupo.getId() == null) {
            throw new RuntimeException("Subgrupo inválido para atualização.");
        }

        if (subgrupo.getGrupo() == null || subgrupo.getGrupo().getId() == null) {
            throw new RuntimeException("O grupo do subgrupo é obrigatório.");
        }

        validar(subgrupo.getDescricao());

        subgrupo.setDescricao(subgrupo.getDescricao().trim());

        subgrupoDAO.atualizar(subgrupo);

        String nomeGrupo = subgrupo.getGrupo().getDescricao() != null
                ? subgrupo.getGrupo().getDescricao()
                : "ID " + subgrupo.getGrupo().getId();

        logAcaoBO.registrar(
                "ALTERAR",
                "SUBGRUPO",
                subgrupo.getId(),
                "Alterou subgrupo: " + subgrupo.getDescricao() + " do grupo: " + nomeGrupo
        );
    }

    public void excluirSubgrupo(Long id) {
        if (id == null) {
            throw new RuntimeException("ID do subgrupo é obrigatório.");
        }

        SubGrupo subgrupo = subgrupoDAO.buscarPorId(id);

        subgrupoDAO.excluir(id);

        String descricaoSubgrupo = subgrupo != null && subgrupo.getDescricao() != null
                ? subgrupo.getDescricao()
                : "ID " + id;

        logAcaoBO.registrar(
                "EXCLUIR",
                "SUBGRUPO",
                id,
                "Excluiu subgrupo: " + descricaoSubgrupo
        );
    }

    public List<SubGrupo> listarSubgrupos() {
        return subgrupoDAO.listar();
    }

    public List<SubGrupo> listarPorGrupo(Long grupoId) {
        if (grupoId == null) {
            throw new RuntimeException("ID do grupo é obrigatório.");
        }

        return subgrupoDAO.listarPorGrupo(grupoId);
    }

    public SubGrupo buscarPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("ID do subgrupo é obrigatório.");
        }

        return subgrupoDAO.buscarPorId(id);
    }

    private void validar(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new RuntimeException("A descrição do subgrupo é obrigatória.");
        }
    }
}