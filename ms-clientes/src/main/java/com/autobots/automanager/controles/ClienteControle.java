package com.autobots.automanager.controles;

import com.autobots.automanager.assembler.ClienteAssembler;
import com.autobots.automanager.dtos.ClienteDto;
import com.autobots.automanager.servicos.ClienteServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/empresa/{empresaId}/clientes")
public class ClienteControle {

    @Autowired private ClienteServico servico;
    @Autowired private ClienteAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ClienteDto>>> listarClientes(
            @PathVariable Long empresaId) {

        List<EntityModel<ClienteDto>> lista = servico.listarClientesPorEmpresa(empresaId)
                .stream().map(assembler::toModel).collect(Collectors.toList());

        CollectionModel<EntityModel<ClienteDto>> modelo = CollectionModel.of(lista,
                linkTo(methodOn(ClienteControle.class).listarClientes(empresaId)).withSelfRel());

        return ResponseEntity.ok(modelo);
    }
}
