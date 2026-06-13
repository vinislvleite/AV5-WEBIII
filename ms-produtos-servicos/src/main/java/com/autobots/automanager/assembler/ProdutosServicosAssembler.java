package com.autobots.automanager.assembler;

import com.autobots.automanager.controles.ProdutosServicosControle;
import com.autobots.automanager.dtos.ProdutosServicosDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProdutosServicosAssembler
        implements RepresentationModelAssembler<ProdutosServicosDto, EntityModel<ProdutosServicosDto>> {

    @Override
    public EntityModel<ProdutosServicosDto> toModel(ProdutosServicosDto dto) {
        Link selfLink = linkTo(
                methodOn(ProdutosServicosControle.class).listar(dto.getEmpresaId()))
                .withSelfRel();

        Link produtosLink = linkTo(
                methodOn(ProdutosServicosControle.class).listar(dto.getEmpresaId()))
                .withRel("produtos-servicos-empresa");

        return EntityModel.of(dto, selfLink, produtosLink);
    }
}
