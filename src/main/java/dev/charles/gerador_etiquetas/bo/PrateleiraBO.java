package dev.charles.gerador_etiquetas.bo;

import dev.charles.gerador_etiquetas.dao.PrateleiraDAO;
import dev.charles.gerador_etiquetas.model.Prateleira;

import java.util.List;

public class PrateleiraBO {

    private PrateleiraDAO prateleiraDAO;

    public PrateleiraBO() {
        this.prateleiraDAO = new PrateleiraDAO();
    }

    public Prateleira cadastrarPrateleira(String localPrateleira, String descricaoGrupo) {
        validarDados(localPrateleira);

        Prateleira prateleira = new Prateleira();
        prateleira.setLocalPrateleira(localPrateleira.trim());
        prateleira.setDescricaoGrupo(descricaoGrupo);

        return prateleiraDAO.salvar(prateleira);
    }

    public List<Prateleira> listarPrateleiras() {
        return prateleiraDAO.listar();
    }

    private void validarDados(String localPrateleira) {
        if (localPrateleira == null || localPrateleira.isBlank()) {
            throw new RuntimeException("O local da prateleira é obrigatório.");
        }
    }
}