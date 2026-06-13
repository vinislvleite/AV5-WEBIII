package com.autobots.automanager.dtos;

import com.autobots.automanager.enumeracoes.TipoVeiculo;
import lombok.Data;

@Data
public class VeiculoDto {
    private Long id;
    private Long empresaId;
    private TipoVeiculo tipo;
    private String modelo;
    private String placa;
    private Long proprietarioId;
    private String proprietarioNome;
}
