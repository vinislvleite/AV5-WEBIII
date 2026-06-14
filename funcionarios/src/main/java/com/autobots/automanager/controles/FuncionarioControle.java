package com.autobots.automanager.controles;

import com.autobots.automanager.assembler.FuncionarioAssembler;
import com.autobots.automanager.dtos.FuncionarioDto;
import com.autobots.automanager.servicos.FuncionarioServico;
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
    public FuncionarioControle(FuncionarioServico servico, FuncionarioAssembler assembler) {
        this.servico = servico;
        this.assembler = assembler;
    }



    private final FuncionarioServico servico;
    private final FuncionarioAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<FuncionarioDto>>> listarFuncionarios(
            @PathVariable Long empresaId) {

        List<EntityModel<FuncionarioDto>> lista = servico.listarFuncionariosPorEmpresa(empresaId)
                .stream().map(assembler::toModel).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(FuncionarioControle.class).listarFuncionarios(empresaId)).withSelfRel()));
    }

    @GetMapping("/{funcionarioId}")
    public ResponseEntity<EntityModel<FuncionarioDto>> obterFuncionario(@PathVariable Long empresaId,
                                                                        @PathVariable Long funcionarioId) {
        return ResponseEntity.ok(assembler.toModel(servico.obterFuncionarioPorEmpresa(empresaId, funcionarioId)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<FuncionarioDto>> criarFuncionario(@PathVariable Long empresaId,
                                                                         @RequestBody FuncionarioDto dto) {
        return ResponseEntity.ok(assembler.toModel(servico.criarFuncionario(empresaId, dto)));
    }

    @PutMapping("/{funcionarioId}")
    public ResponseEntity<EntityModel<FuncionarioDto>> atualizarFuncionario(@PathVariable Long empresaId,
                                                                            @PathVariable Long funcionarioId,
                                                                            @RequestBody FuncionarioDto dto) {
        return ResponseEntity.ok(assembler.toModel(servico.atualizarFuncionario(empresaId, funcionarioId, dto)));
    }

    @DeleteMapping("/{funcionarioId}")
    public ResponseEntity<Void> excluirFuncionario(@PathVariable Long empresaId,
                                                   @PathVariable Long funcionarioId) {
        servico.excluirFuncionario(empresaId, funcionarioId);
        return ResponseEntity.noContent().build();
    }
}
