package com.autobots.automanager.assembler;

import com.autobots.automanager.controles.VendaRelatorioControle;
import com.autobots.automanager.dtos.VendaDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class VendaAssembler implements RepresentationModelAssembler<VendaDto, EntityModel<VendaDto>> {

    @Override
    public EntityModel<VendaDto> toModel(VendaDto dto) {
        Link selfLink = linkTo(VendaRelatorioControle.class)
                .slash("empresa")
                .slash(dto.getEmpresaId())
                .slash("vendas")
                .withSelfRel();

        Link listaLink = linkTo(VendaRelatorioControle.class)
                .slash("empresa")
                .slash(dto.getEmpresaId())
                .slash("vendas")
                .withRel("vendas-da-empresa");

        return EntityModel.of(dto, selfLink, listaLink);
    }
}
