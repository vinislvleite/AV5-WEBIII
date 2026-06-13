package com.autobots.automanager.dtos;
import lombok.Data;
import java.util.Date;
@Data
public class MercadoriaDto {
    private Long id;
    private String nome;
    private String descricao;
    private double valor;
    private long quantidade;
    private Date validade;
    private Date fabricao;
    private Date cadastro;
}
