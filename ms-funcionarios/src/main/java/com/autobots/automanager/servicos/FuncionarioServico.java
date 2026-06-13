package com.autobots.automanager.servicos;

import com.autobots.automanager.dtos.*;
import com.autobots.automanager.entitades.*;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionarioServico {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    public List<FuncionarioDto> listarFuncionariosPorEmpresa(Long empresaId) {
        Empresa empresa = repositorioEmpresa.findById(empresaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Empresa não encontrada com id: " + empresaId));

        return empresa.getUsuarios().stream()
                .filter(u -> u.getPerfis().contains(PerfilUsuario.FUNCIONARIO))
                .map(u -> toDto(u, empresaId))
                .collect(Collectors.toList());
    }

    private FuncionarioDto toDto(Usuario u, Long empresaId) {
        FuncionarioDto dto = new FuncionarioDto();
        dto.setId(u.getId());
        dto.setEmpresaId(empresaId);
        dto.setNome(u.getNome());
        dto.setNomeSocial(u.getNomeSocial());
        dto.setPerfis(u.getPerfis());

        if (u.getDocumentos() != null)
            dto.setDocumentos(u.getDocumentos().stream().map(this::toDocDto).collect(Collectors.toSet()));
        if (u.getTelefones() != null)
            dto.setTelefones(u.getTelefones().stream().map(this::toTelDto).collect(Collectors.toSet()));
        if (u.getEmails() != null)
            dto.setEmails(u.getEmails().stream().map(this::toEmailDto).collect(Collectors.toSet()));
        if (u.getEndereco() != null)
            dto.setEndereco(toEndDto(u.getEndereco()));

        return dto;
    }

    private DocumentoDto toDocDto(Documento d) {
        DocumentoDto dto = new DocumentoDto();
        dto.setId(d.getId()); dto.setTipo(d.getTipo());
        dto.setNumero(d.getNumero()); dto.setDataEmissao(d.getDataEmissao());
        return dto;
    }
    private TelefoneDto toTelDto(Telefone t) {
        TelefoneDto dto = new TelefoneDto();
        dto.setId(t.getId()); dto.setDdd(t.getDdd()); dto.setNumero(t.getNumero());
        return dto;
    }
    private EmailDto toEmailDto(Email e) {
        EmailDto dto = new EmailDto();
        dto.setId(e.getId()); dto.setEndereco(e.getEndereco());
        return dto;
    }
    private EnderecoDto toEndDto(Endereco e) {
        EnderecoDto dto = new EnderecoDto();
        dto.setId(e.getId()); dto.setEstado(e.getEstado()); dto.setCidade(e.getCidade());
        dto.setBairro(e.getBairro()); dto.setRua(e.getRua()); dto.setNumero(e.getNumero());
        dto.setCodigoPostal(e.getCodigoPostal());
        dto.setInformacoesAdicionais(e.getInformacoesAdicionais());
        return dto;
    }
}
