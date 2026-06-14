package com.autobots.automanager.assembler;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.dtos.ClienteDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClienteAssembler implements RepresentationModelAssembler<ClienteDto, EntityModel<ClienteDto>> {

    @Override
    public EntityModel<ClienteDto> toModel(ClienteDto dto) {
        Link selfLink = linkTo(
                methodOn(ClienteControle.class).listarClientes(dto.getEmpresaId()))
                .withSelfRel();

        Link listaLink = linkTo(
                methodOn(ClienteControle.class).listarClientes(dto.getEmpresaId()))
                .withRel("clientes-da-empresa");

        return EntityModel.of(dto, selfLink, listaLink);
    }
}
