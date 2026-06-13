package com.autobots.automanager.dtos;

import com.autobots.automanager.enumeracoes.PerfilUsuario;
import lombok.Data;
import java.util.Set;

@Data
public class FuncionarioDto {
    private Long id;
    private Long empresaId;
    private String nome;
    private String nomeSocial;
    private Set<PerfilUsuario> perfis;
    private Set<DocumentoDto> documentos;
    private Set<TelefoneDto> telefones;
    private Set<EmailDto> emails;
    private EnderecoDto endereco;
}
