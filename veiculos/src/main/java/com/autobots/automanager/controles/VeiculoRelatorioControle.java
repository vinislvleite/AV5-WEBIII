package com.autobots.automanager.controles;

import com.autobots.automanager.assembler.VeiculoAssembler;
import com.autobots.automanager.dtos.VeiculoDto;
import com.autobots.automanager.servicos.VeiculoRelatorioServico;
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
    public VeiculoRelatorioControle(VeiculoRelatorioServico servico, VeiculoAssembler assembler) {
        this.servico = servico;
        this.assembler = assembler;
    }



    private final VeiculoRelatorioServico servico;

    private final VeiculoAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<VeiculoDto>>> listarVeiculos(
            @PathVariable Long empresaId) {
        List<EntityModel<VeiculoDto>> lista = servico.listarVeiculosPorEmpresa(empresaId)
                .stream().map(assembler::toModel).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(VeiculoRelatorioControle.class).listarVeiculos(empresaId)).withSelfRel()));
    }

    @GetMapping("/atendidos")
    public ResponseEntity<CollectionModel<EntityModel<VeiculoDto>>> listarVeiculosAtendidos(
            @PathVariable Long empresaId) {
        List<EntityModel<VeiculoDto>> lista = servico.listarVeiculosAtendidos(empresaId)
                .stream().map(assembler::toModel).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(VeiculoRelatorioControle.class).listarVeiculosAtendidos(empresaId)).withSelfRel()));
    }

    @GetMapping("/{veiculoId}")
    public ResponseEntity<EntityModel<VeiculoDto>> obterVeiculo(@PathVariable Long empresaId,
                                                                @PathVariable Long veiculoId) {
        return ResponseEntity.ok(assembler.toModel(servico.obterVeiculo(empresaId, veiculoId)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<VeiculoDto>> criarVeiculo(@PathVariable Long empresaId,
                                                                 @RequestBody VeiculoDto dto) {
        return ResponseEntity.ok(assembler.toModel(servico.criarVeiculo(empresaId, dto)));
    }

    @PutMapping("/{veiculoId}")
    public ResponseEntity<EntityModel<VeiculoDto>> atualizarVeiculo(@PathVariable Long empresaId,
                                                                    @PathVariable Long veiculoId,
                                                                    @RequestBody VeiculoDto dto) {
        return ResponseEntity.ok(assembler.toModel(servico.atualizarVeiculo(empresaId, veiculoId, dto)));
    }

    @DeleteMapping("/{veiculoId}")
    public ResponseEntity<Void> excluirVeiculo(@PathVariable Long empresaId,
                                               @PathVariable Long veiculoId) {
        servico.excluirVeiculo(empresaId, veiculoId);
        return ResponseEntity.noContent().build();
    }
}
