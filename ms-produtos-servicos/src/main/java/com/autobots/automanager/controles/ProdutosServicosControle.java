package com.autobots.automanager.controles;

import com.autobots.automanager.assembler.ProdutosServicosAssembler;
import com.autobots.automanager.dtos.ProdutosServicosDto;
import com.autobots.automanager.servicos.ProdutosServicosServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/empresa/{empresaId}/produtos-servicos")
public class ProdutosServicosControle {

    @Autowired private ProdutosServicosServico servico;
    @Autowired private ProdutosServicosAssembler assembler;

    @GetMapping
    public ResponseEntity<EntityModel<ProdutosServicosDto>> listar(@PathVariable Long empresaId) {
        return ResponseEntity.ok(assembler.toModel(servico.listar(empresaId)));
    }
}
