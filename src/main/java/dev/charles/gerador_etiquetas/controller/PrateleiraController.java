package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.PrateleiraBO;
import dev.charles.gerador_etiquetas.model.Prateleira;

import java.util.List;

public class PrateleiraController {

    private PrateleiraBO prateleiraBO;

    public PrateleiraController() {
        this.prateleiraBO = new PrateleiraBO();
    }

    public Prateleira cadastrarPrateleira(String localPrateleira, String descricaoGrupo) {
        return prateleiraBO.cadastrarPrateleira(localPrateleira, descricaoGrupo);
    }

    public List<Prateleira> listarPrateleiras() {
        return prateleiraBO.listarPrateleiras();
    }
}