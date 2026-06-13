package com.autobots.automanager.controles;

import com.autobots.automanager.assembler.FuncionarioAssembler;
import com.autobots.automanager.dtos.FuncionarioDto;
import com.autobots.automanager.servicos.FuncionarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/empresa/{empresaId}/funcionarios")
public class FuncionarioControle {

    @Autowired private FuncionarioServico servico;
    @Autowired private FuncionarioAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<FuncionarioDto>>> listarFuncionarios(
            @PathVariable Long empresaId) {

        List<EntityModel<FuncionarioDto>> lista = servico.listarFuncionariosPorEmpresa(empresaId)
                .stream().map(assembler::toModel).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(FuncionarioControle.class).listarFuncionarios(empresaId)).withSelfRel()));
    }
}
