package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.PrateleiraBO;
import dev.charles.gerador_etiquetas.model.Prateleira;

import java.util.List;

public class PrateleiraController {

    private final PrateleiraBO prateleiraBO;

    public PrateleiraController() {
        this.prateleiraBO = new PrateleiraBO();
    }

    public Prateleira cadastrarPrateleira(String localPrateleira, String descricaoGrupo) {
        return prateleiraBO.cadastrarPrateleira(localPrateleira, descricaoGrupo);
    }

    public void atualizarPrateleira(Prateleira prateleira) {
        prateleiraBO.atualizarPrateleira(prateleira);
    }

    public void excluirPrateleira(Long id) {
        prateleiraBO.excluirPrateleira(id);
    }

    public List<Prateleira> listarPrateleiras() {
        return prateleiraBO.listarPrateleiras();
    }

    public Prateleira buscarPorId(Long id) {
        return prateleiraBO.buscarPorId(id);
    }
}
