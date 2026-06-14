package com.autobots.automanager.controles;

import com.autobots.automanager.assembler.ProdutosServicosAssembler;
import com.autobots.automanager.dtos.MercadoriaDto;
import com.autobots.automanager.dtos.ProdutosServicosDto;
import com.autobots.automanager.dtos.ServicoDto;
import com.autobots.automanager.servicos.ProdutosServicosServico;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/empresa/{empresaId}/produtos-servicos")
public class ProdutosServicosControle {
    public ProdutosServicosControle(ProdutosServicosServico servico, ProdutosServicosAssembler assembler) {
        this.servico = servico;
        this.assembler = assembler;
    }



    private final ProdutosServicosServico servico;
    private final ProdutosServicosAssembler assembler;

    @GetMapping
    public ResponseEntity<EntityModel<ProdutosServicosDto>> listar(@PathVariable Long empresaId) {
        return ResponseEntity.ok(assembler.toModel(servico.listar(empresaId)));
    }

    @GetMapping("/mercadorias/{mercadoriaId}")
    public ResponseEntity<MercadoriaDto> obterMercadoria(@PathVariable Long empresaId,
                                                         @PathVariable Long mercadoriaId) {
        return ResponseEntity.ok(servico.obterMercadoria(empresaId, mercadoriaId));
    }

    @PostMapping("/mercadorias")
    public ResponseEntity<MercadoriaDto> criarMercadoria(@PathVariable Long empresaId,
                                                          @RequestBody MercadoriaDto dto) {
        return ResponseEntity.ok(servico.criarMercadoria(empresaId, dto));
    }

    @PutMapping("/mercadorias/{mercadoriaId}")
    public ResponseEntity<MercadoriaDto> atualizarMercadoria(@PathVariable Long empresaId,
                                                             @PathVariable Long mercadoriaId,
                                                             @RequestBody MercadoriaDto dto) {
        return ResponseEntity.ok(servico.atualizarMercadoria(empresaId, mercadoriaId, dto));
    }

    @DeleteMapping("/mercadorias/{mercadoriaId}")
    public ResponseEntity<Void> excluirMercadoria(@PathVariable Long empresaId,
                                                   @PathVariable Long mercadoriaId) {
        servico.excluirMercadoria(empresaId, mercadoriaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/servicos/{servicoId}")
    public ResponseEntity<ServicoDto> obterServico(@PathVariable Long empresaId,
                                                   @PathVariable Long servicoId) {
        return ResponseEntity.ok(servico.obterServico(empresaId, servicoId));
    }

    @PostMapping("/servicos")
    public ResponseEntity<ServicoDto> criarServico(@PathVariable Long empresaId,
                                                   @RequestBody ServicoDto dto) {
        return ResponseEntity.ok(servico.criarServico(empresaId, dto));
    }

    @PutMapping("/servicos/{servicoId}")
    public ResponseEntity<ServicoDto> atualizarServico(@PathVariable Long empresaId,
                                                       @PathVariable Long servicoId,
                                                       @RequestBody ServicoDto dto) {
        return ResponseEntity.ok(servico.atualizarServico(empresaId, servicoId, dto));
    }

    @DeleteMapping("/servicos/{servicoId}")
    public ResponseEntity<Void> excluirServico(@PathVariable Long empresaId,
                                               @PathVariable Long servicoId) {
        servico.excluirServico(empresaId, servicoId);
        return ResponseEntity.noContent().build();
    }
}
