package com.autobots.automanager.assembler;

import com.autobots.automanager.controles.FuncionarioControle;
import com.autobots.automanager.dtos.FuncionarioDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class FuncionarioAssembler implements RepresentationModelAssembler<FuncionarioDto, EntityModel<FuncionarioDto>> {

    @Override
    public EntityModel<FuncionarioDto> toModel(FuncionarioDto dto) {
        Link selfLink = linkTo(
                methodOn(FuncionarioControle.class).listarFuncionarios(dto.getEmpresaId()))
                .withSelfRel();

        Link listaLink = linkTo(
                methodOn(FuncionarioControle.class).listarFuncionarios(dto.getEmpresaId()))
                .withRel("funcionarios-da-empresa");

        return EntityModel.of(dto, selfLink, listaLink);
    }
}
