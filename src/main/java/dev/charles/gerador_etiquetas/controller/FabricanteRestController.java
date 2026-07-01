package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.FabricanteBO;
import dev.charles.gerador_etiquetas.model.Fabricante;
import dev.charles.gerador_etiquetas.dto.FabricanteRequest;
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
@RequestMapping("/api/fabricantes")
public class FabricanteRestController {

    private final FabricanteBO fabricanteBO = new FabricanteBO();

    @GetMapping
    public List<Fabricante> listar(@RequestParam(defaultValue = "true") boolean ativos) {
        return ativos ? fabricanteBO.listarFabricantesAtivos() : fabricanteBO.listarFabricantes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fabricante criar(@RequestBody FabricanteRequest request) {
        return fabricanteBO.cadastrarFabricante(request.nome(), request.tipo());
    }
}
