package dev.charles.gerador_etiquetas.bo;

import dev.charles.gerador_etiquetas.dao.GrupoDAO;
import dev.charles.gerador_etiquetas.model.Grupo;

import java.util.List;

public class GrupoBO {

    private final GrupoDAO grupoDAO;
    private final LogAcaoBO logAcaoBO;

    public GrupoBO() {
        this.grupoDAO = new GrupoDAO();
        this.logAcaoBO = new LogAcaoBO();
    }

    public Grupo cadastrarGrupo(String descricao) {
        validar(descricao);

        Grupo grupo = new Grupo();
        grupo.setDescricao(descricao.trim());

        Grupo grupoSalvo = grupoDAO.salvar(grupo);

        logAcaoBO.registrar(
                "CADASTRAR",
                "GRUPO",
                grupoSalvo.getId(),
                "Cadastrou grupo: " + grupoSalvo.getDescricao()
        );

        return grupoSalvo;
    }

    public void atualizarGrupo(Grupo grupo) {
        if (grupo == null || grupo.getId() == null) {
            throw new RuntimeException("Grupo inválido para atualização.");
        }

        validar(grupo.getDescricao());

        grupo.setDescricao(grupo.getDescricao().trim());

        grupoDAO.atualizar(grupo);

        logAcaoBO.registrar(
                "ALTERAR",
                "GRUPO",
                grupo.getId(),
                "Alterou grupo: " + grupo.getDescricao()
        );
    }

    public void excluirGrupo(Long id) {
        if (id == null) {
            throw new RuntimeException("ID do grupo é obrigatório.");
        }

        Grupo grupo = grupoDAO.buscarPorId(id);

        grupoDAO.excluir(id);

        String descricaoGrupo = grupo != null && grupo.getDescricao() != null
                ? grupo.getDescricao()
                : "ID " + id;

        logAcaoBO.registrar(
                "EXCLUIR",
                "GRUPO",
                id,
                "Excluiu grupo: " + descricaoGrupo
        );
    }

    public List<Grupo> listarGrupos() {
        return grupoDAO.listar();
    }

    public Grupo buscarPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("ID do grupo é obrigatório.");
        }

        return grupoDAO.buscarPorId(id);
    }

    private void validar(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new RuntimeException("A descrição do grupo é obrigatória.");
        }
    }
}