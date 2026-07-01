package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.EtiquetaBO;
import dev.charles.gerador_etiquetas.model.Etiqueta;
import dev.charles.gerador_etiquetas.model.Fabricante;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.Prateleira;
import dev.charles.gerador_etiquetas.model.SubGrupo;
import dev.charles.gerador_etiquetas.patterns.strategy.EtiquetaCompletaHtmlStrategy;
import dev.charles.gerador_etiquetas.patterns.strategy.EtiquetaHtmlStrategy;
import dev.charles.gerador_etiquetas.patterns.strategy.EtiquetaSimplesHtmlStrategy;
import dev.charles.gerador_etiquetas.dto.EtiquetaRequest;
import dev.charles.gerador_etiquetas.dto.ImprimirRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/etiquetas")
public class EtiquetaRestController {

    private final EtiquetaBO etiquetaBO = new EtiquetaBO();

    @GetMapping
    public List<Etiqueta> listar() {
        return etiquetaBO.listarEtiquetas();
    }

    @GetMapping("/buscar")
    public List<Etiqueta> buscar(@RequestParam String tipo, @RequestParam(required = false) String termo) {
        return etiquetaBO.pesquisarEtiquetasPorTipo(tipo, termo);
    }

    @GetMapping("/{id}")
    public Etiqueta buscarPorId(@PathVariable Long id) {
        return exigirEtiqueta(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Etiqueta criar(@RequestBody EtiquetaRequest request) {
        return etiquetaBO.cadastrarEtiqueta(
                request.descricao(),
                prateleiraEsqueleto(request.prateleiraId()),
                grupoEsqueleto(request.grupoId()),
                subGrupoEsqueleto(request.subGrupoId()),
                fabricanteEsqueleto(request.fabricanteId()),
                request.codigoVenda(),
                request.codigosOriginais(),
                request.larguraCm(),
                request.alturaCm()
        );
    }

    @PutMapping("/{id}")
    public Etiqueta atualizar(@PathVariable Long id, @RequestBody EtiquetaRequest request) {
        exigirEtiqueta(id);

        Fabricante fabricante = fabricanteEsqueleto(request.fabricanteId());

        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setId(id);
        etiqueta.setDescricao(request.descricao());
        etiqueta.setPrateleira(prateleiraEsqueleto(request.prateleiraId()));
        etiqueta.setGrupo(grupoEsqueleto(request.grupoId()));
        etiqueta.setSubGrupo(subGrupoEsqueleto(request.subGrupoId()));
        etiqueta.setCodigoVenda(request.codigoVenda());
        etiqueta.setCodigosOriginais(etiquetaBO.converterStringsParaCodigosOriginais(request.codigosOriginais(), fabricante));
        etiqueta.setLarguraCm(request.larguraCm());
        etiqueta.setAlturaCm(request.alturaCm());

        etiquetaBO.atualizarEtiqueta(etiqueta);

        return etiquetaBO.buscarEtiquetaPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        etiquetaBO.excluirEtiqueta(id);
    }

    @PostMapping("/imprimir")
    public Map<String, String> imprimir(@RequestBody ImprimirRequest request) {
        if (request.ids() == null || request.ids().isEmpty()) {
            throw new RuntimeException("Nenhuma etiqueta selecionada para impressão.");
        }

        List<Etiqueta> etiquetas = new ArrayList<>();

        for (Long id : request.ids()) {
            etiquetas.add(exigirEtiqueta(id));
        }

        EtiquetaHtmlStrategy strategy = "SIMPLES".equalsIgnoreCase(request.modelo())
                ? new EtiquetaSimplesHtmlStrategy()
                : new EtiquetaCompletaHtmlStrategy();

        String html = etiquetaBO.gerarPaginaA4Html(etiquetas, strategy);

        return Map.of("html", html);
    }

    private Etiqueta exigirEtiqueta(Long id) {
        Etiqueta etiqueta = etiquetaBO.buscarEtiquetaPorId(id);

        if (etiqueta == null) {
            throw new RecursoNaoEncontradoException("Etiqueta não encontrada: " + id);
        }

        return etiqueta;
    }

    private Prateleira prateleiraEsqueleto(Long id) {
        if (id == null) {
            return null;
        }

        Prateleira prateleira = new Prateleira();
        prateleira.setId(id);
        return prateleira;
    }

    private Grupo grupoEsqueleto(Long id) {
        if (id == null) {
            return null;
        }

        Grupo grupo = new Grupo();
        grupo.setId(id);
        return grupo;
    }

    private SubGrupo subGrupoEsqueleto(Long id) {
        if (id == null) {
            return null;
        }

        SubGrupo subGrupo = new SubGrupo();
        subGrupo.setId(id);
        return subGrupo;
    }

    private Fabricante fabricanteEsqueleto(Long id) {
        if (id == null) {
            return null;
        }

        Fabricante fabricante = new Fabricante();
        fabricante.setId(id);
        return fabricante;
    }
}
