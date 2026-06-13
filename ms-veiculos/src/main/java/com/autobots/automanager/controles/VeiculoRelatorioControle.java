package com.autobots.automanager.controles;

import com.autobots.automanager.assembler.VeiculoAssembler;
import com.autobots.automanager.dtos.VeiculoDto;
import com.autobots.automanager.servicos.VeiculoRelatorioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/empresa/{empresaId}/veiculos")
public class VeiculoRelatorioControle {

    @Autowired private VeiculoRelatorioServico servico;
    @Autowired private VeiculoAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<VeiculoDto>>> listarVeiculos(
            @PathVariable Long empresaId) {

        List<EntityModel<VeiculoDto>> lista = servico.listarVeiculosAtendidos(empresaId)
                .stream().map(assembler::toModel).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(VeiculoRelatorioControle.class).listarVeiculos(empresaId)).withSelfRel()));
    }
}
