package com.autobots.automanager.dtos;
import lombok.Data;
@Data
public class EnderecoDto {
    private Long id;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String codigoPostal;
    private String informacoesAdicionais;
}
