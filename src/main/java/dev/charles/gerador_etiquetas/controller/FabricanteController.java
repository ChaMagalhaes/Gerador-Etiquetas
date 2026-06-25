package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.FabricanteBO;
import dev.charles.gerador_etiquetas.model.Fabricante;

import java.util.List;

public class FabricanteController {

    private final FabricanteBO fabricanteBO;

    public FabricanteController() {
        this.fabricanteBO = new FabricanteBO();
    }

    public Fabricante cadastrarFabricante(String nome, String tipo) {
        return fabricanteBO.cadastrarFabricante(nome, tipo);
    }

    public void atualizarFabricante(Fabricante fabricante) {
        fabricanteBO.atualizarFabricante(fabricante);
    }

    public void excluirFabricante(Long id) {
        fabricanteBO.excluirFabricante(id);
    }

    public void inativarFabricante(Long id) {
        fabricanteBO.inativarFabricante(id);
    }

    public List<Fabricante> listarFabricantes() {
        return fabricanteBO.listarFabricantes();
    }

    public List<Fabricante> listarFabricantesAtivos() {
        return fabricanteBO.listarFabricantesAtivos();
    }

    public Fabricante buscarPorId(Long id) {
        return fabricanteBO.buscarPorId(id);
    }

    public List<Fabricante> buscarPorNome(String nome) {
        return fabricanteBO.buscarPorNome(nome);
    }
}
