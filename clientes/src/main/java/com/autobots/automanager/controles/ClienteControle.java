package com.autobots.automanager.controles;

import com.autobots.automanager.assembler.ClienteAssembler;
import com.autobots.automanager.dtos.ClienteDto;
import com.autobots.automanager.servicos.ClienteServico;
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
    public ClienteControle(ClienteServico servico, ClienteAssembler assembler) {
        this.servico = servico;
        this.assembler = assembler;
    }



    private final ClienteServico servico;
    private final ClienteAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ClienteDto>>> listarClientes(
            @PathVariable Long empresaId) {

        List<EntityModel<ClienteDto>> lista = servico.listarClientesPorEmpresa(empresaId)
                .stream().map(assembler::toModel).collect(Collectors.toList());

        CollectionModel<EntityModel<ClienteDto>> modelo = CollectionModel.of(lista,
                linkTo(methodOn(ClienteControle.class).listarClientes(empresaId)).withSelfRel());

        return ResponseEntity.ok(modelo);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<EntityModel<ClienteDto>> obterCliente(@PathVariable Long empresaId,
                                                                @PathVariable Long clienteId) {
        return ResponseEntity.ok(assembler.toModel(servico.obterClientePorEmpresa(empresaId, clienteId)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ClienteDto>> criarCliente(@PathVariable Long empresaId,
                                                                 @RequestBody ClienteDto dto) {
        return ResponseEntity.ok(assembler.toModel(servico.criarCliente(empresaId, dto)));
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<EntityModel<ClienteDto>> atualizarCliente(@PathVariable Long empresaId,
                                                                    @PathVariable Long clienteId,
                                                                    @RequestBody ClienteDto dto) {
        return ResponseEntity.ok(assembler.toModel(servico.atualizarCliente(empresaId, clienteId, dto)));
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long empresaId,
                                               @PathVariable Long clienteId) {
        servico.excluirCliente(empresaId, clienteId);
        return ResponseEntity.noContent().build();
    }
}
