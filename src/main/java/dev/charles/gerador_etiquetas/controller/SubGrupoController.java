package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.SubGrupoBO;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.SubGrupo;

import java.util.List;

public class SubGrupoController {

    private final SubGrupoBO subgrupoBO;

    public SubGrupoController() {
        this.subgrupoBO = new SubGrupoBO();
    }

    public SubGrupo cadastrarSubgrupo(Grupo grupo, String descricao) {
        return subgrupoBO.cadastrarSubgrupo(grupo, descricao);
    }

    public void atualizarSubgrupo(SubGrupo subgrupo) {
        subgrupoBO.atualizarSubgrupo(subgrupo);
    }

    public void excluirSubgrupo(Long id) {
        subgrupoBO.excluirSubgrupo(id);
    }

    public List<SubGrupo> listarSubgrupos() {
        return subgrupoBO.listarSubgrupos();
    }

    public List<SubGrupo> listarPorGrupo(Long grupoId) {
        return subgrupoBO.listarPorGrupo(grupoId);
    }

    public SubGrupo buscarPorId(Long id) {
        return subgrupoBO.buscarPorId(id);
    }
}