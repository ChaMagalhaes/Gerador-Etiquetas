package dev.charles.gerador_etiquetas.bo;

import dev.charles.gerador_etiquetas.dao.FabricanteDAO;
import dev.charles.gerador_etiquetas.model.Fabricante;

import java.util.List;

public class FabricanteBO {

    private final FabricanteDAO fabricanteDAO;
    private final LogAcaoBO logAcaoBO;

    public FabricanteBO() {
        this.fabricanteDAO = new FabricanteDAO();
        this.logAcaoBO = new LogAcaoBO();
    }

    public Fabricante cadastrarFabricante(String nome, String tipo) {
        validarNome(nome);

        Fabricante fabricante = new Fabricante();
        fabricante.setNome(nome.trim());
        fabricante.setTipo(tipo != null ? tipo.trim() : null);
        fabricante.setAtivo(true);

        Fabricante fabricanteSalvo = fabricanteDAO.salvar(fabricante);

        logAcaoBO.registrar(
                "CADASTRAR",
                "FABRICANTE",
                fabricanteSalvo.getId(),
                "Cadastrou fabricante: " + fabricanteSalvo.getNome()
        );

        return fabricanteSalvo;
    }

    public void atualizarFabricante(Fabricante fabricante) {
        if (fabricante == null || fabricante.getId() == null) {
            throw new RuntimeException("Fabricante inválido para atualização.");
        }

        validarNome(fabricante.getNome());

        fabricante.setNome(fabricante.getNome().trim());

        if (fabricante.getTipo() != null) {
            fabricante.setTipo(fabricante.getTipo().trim());
        }

        fabricanteDAO.atualizar(fabricante);

        logAcaoBO.registrar(
                "ALTERAR",
                "FABRICANTE",
                fabricante.getId(),
                "Alterou fabricante: " + fabricante.getNome()
        );
    }

    public void excluirFabricante(Long id) {
        if (id == null) {
            throw new RuntimeException("ID do fabricante é obrigatório.");
        }

        Fabricante fabricante = fabricanteDAO.buscarPorId(id);

        fabricanteDAO.excluir(id);

        String nomeFabricante = fabricante != null && fabricante.getNome() != null
                ? fabricante.getNome()
                : "ID " + id;

        logAcaoBO.registrar(
                "EXCLUIR",
                "FABRICANTE",
                id,
                "Excluiu fabricante: " + nomeFabricante
        );
    }

    public void inativarFabricante(Long id) {
        if (id == null) {
            throw new RuntimeException("ID do fabricante é obrigatório.");
        }

        Fabricante fabricante = fabricanteDAO.buscarPorId(id);

        fabricanteDAO.inativar(id);

        String nomeFabricante = fabricante != null && fabricante.getNome() != null
                ? fabricante.getNome()
                : "ID " + id;

        logAcaoBO.registrar(
                "INATIVAR",
                "FABRICANTE",
                id,
                "Inativou fabricante: " + nomeFabricante
        );
    }

    public List<Fabricante> listarFabricantes() {
        return fabricanteDAO.listar();
    }

    public List<Fabricante> listarFabricantesAtivos() {
        return fabricanteDAO.listarAtivos();
    }

    public Fabricante buscarPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("ID do fabricante é obrigatório.");
        }

        return fabricanteDAO.buscarPorId(id);
    }

    public List<Fabricante> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("O nome para busca é obrigatório.");
        }

        return fabricanteDAO.buscarPorNome(nome.trim());
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("O nome do fabricante é obrigatório.");
        }
    }
}