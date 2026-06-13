package com.autobots.automanager.controles;

import com.autobots.automanager.assembler.VendaAssembler;
import com.autobots.automanager.dtos.VendaDto;
import com.autobots.automanager.servicos.VendaRelatorioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/empresa/{empresaId}/vendas")
public class VendaRelatorioControle {

    @Autowired private VendaRelatorioServico servico;
    @Autowired private VendaAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<VendaDto>>> listarVendas(
            @PathVariable Long empresaId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim) {

        List<EntityModel<VendaDto>> lista = servico.listarVendasPorPeriodo(empresaId, dataInicio, dataFim)
                .stream().map(assembler::toModel).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(VendaRelatorioControle.class)
                        .slash("empresa").slash(empresaId).slash("vendas")
                        .withSelfRel()));
    }
}
