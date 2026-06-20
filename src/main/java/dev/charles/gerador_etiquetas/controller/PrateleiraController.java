package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.dao.PrateleiraDAO;
import dev.charles.gerador_etiquetas.model.Prateleira;

import java.util.List;

public class PrateleiraController {

    private PrateleiraDAO prateleiraDAO;

    public PrateleiraController() {
        this.prateleiraDAO = new PrateleiraDAO();
    }

    public Prateleira cadastrarPrateleira(String localPrateleira, String descricaoGrupo) {
        if (localPrateleira == null || localPrateleira.isBlank()) {
            throw new RuntimeException("O local da prateleira é obrigatório.");
        }

        Prateleira prateleira = new Prateleira();
        prateleira.setLocalPrateleira(localPrateleira.trim());
        prateleira.setDescricaoGrupo(descricaoGrupo);

        return prateleiraDAO.salvar(prateleira);
    }

    public List<Prateleira> listarPrateleiras() {
        return prateleiraDAO.listar();
    }
}
