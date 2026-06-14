package com.autobots.automanager.dtos;

import lombok.Data;
import java.util.Set;

@Data
public class ProdutosServicosDto {
    private Long empresaId;
    private String razaoSocial;
    private String nomeFantasia;
    private Set<MercadoriaDto> mercadorias;
    private Set<ServicoDto> servicos;
}
