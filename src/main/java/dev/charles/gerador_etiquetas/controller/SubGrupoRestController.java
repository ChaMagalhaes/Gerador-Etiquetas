package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.GrupoBO;
import dev.charles.gerador_etiquetas.bo.SubGrupoBO;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.SubGrupo;
import dev.charles.gerador_etiquetas.dto.SubGrupoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subgrupos")
public class SubGrupoRestController {

    private final SubGrupoBO subGrupoBO = new SubGrupoBO();
    private final GrupoBO grupoBO = new GrupoBO();

    @GetMapping
    public List<SubGrupo> listar(@RequestParam(required = false) Long grupoId) {
        if (grupoId != null) {
            return subGrupoBO.listarPorGrupo(grupoId);
        }

        return subGrupoBO.listarSubgrupos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubGrupo criar(@RequestBody SubGrupoRequest request) {
        Grupo grupo = grupoBO.buscarPorId(request.grupoId());

        if (grupo == null) {
            throw new RecursoNaoEncontradoException("Grupo não encontrado: " + request.grupoId());
        }

        return subGrupoBO.cadastrarSubgrupo(grupo, request.descricao());
    }
}
