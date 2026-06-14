package com.autobots.automanager.dtos;
import com.autobots.automanager.enumeracoes.TipoDocumento;
import lombok.Data;
import java.util.Date;
@Data
public class DocumentoDto {
    private Long id;
    private TipoDocumento tipo;
    private String numero;
    private Date dataEmissao;
}
