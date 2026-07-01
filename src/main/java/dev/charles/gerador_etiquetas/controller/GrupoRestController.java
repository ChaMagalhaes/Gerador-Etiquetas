package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.GrupoBO;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.dto.GrupoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoRestController {

    private final GrupoBO grupoBO = new GrupoBO();

    @GetMapping
    public List<Grupo> listar() {
        return grupoBO.listarGrupos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Grupo criar(@RequestBody GrupoRequest request) {
        return grupoBO.cadastrarGrupo(request.descricao());
    }
}
