package com.autobots.automanager.assembler;

import com.autobots.automanager.controles.VeiculoRelatorioControle;
import com.autobots.automanager.dtos.VeiculoDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class VeiculoAssembler implements RepresentationModelAssembler<VeiculoDto, EntityModel<VeiculoDto>> {

    @Override
    public EntityModel<VeiculoDto> toModel(VeiculoDto dto) {
        Link selfLink = linkTo(
                methodOn(VeiculoRelatorioControle.class).listarVeiculos(dto.getEmpresaId()))
                .withSelfRel();

        Link listaLink = linkTo(
                methodOn(VeiculoRelatorioControle.class).listarVeiculos(dto.getEmpresaId()))
                .withRel("veiculos-atendidos");

        return EntityModel.of(dto, selfLink, listaLink);
    }
}
