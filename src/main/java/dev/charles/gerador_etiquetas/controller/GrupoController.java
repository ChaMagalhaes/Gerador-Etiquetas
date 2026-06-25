package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.GrupoBO;
import dev.charles.gerador_etiquetas.model.Grupo;

import java.util.List;

public class GrupoController {

    private final GrupoBO grupoBO;

    public GrupoController() {
        this.grupoBO = new GrupoBO();
    }

    public Grupo cadastrarGrupo(String descricao) {
        return grupoBO.cadastrarGrupo(descricao);
    }

    public void atualizarGrupo(Grupo grupo) {
        grupoBO.atualizarGrupo(grupo);
    }

    public void excluirGrupo(Long id) {
        grupoBO.excluirGrupo(id);
    }

    public List<Grupo> listarGrupos() {
        return grupoBO.listarGrupos();
    }

    public Grupo buscarPorId(Long id) {
        return grupoBO.buscarPorId(id);
    }
}