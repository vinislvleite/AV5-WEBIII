package com.autobots.automanager.dtos;

import lombok.Data;
import java.util.Date;
import java.util.Set;

@Data
public class VendaDto {
    private Long id;
    private Long empresaId;
    private String identificacao;
    private Date cadastro;
    private Long clienteId;
    private String clienteNome;
    private Long funcionarioId;
    private String funcionarioNome;
    private Long veiculoId;
    private String veiculoModelo;
    private String veiculoPlaca;
    private Set<MercadoriaDto> mercadorias;
    private Set<ServicoDto> servicos;
}
