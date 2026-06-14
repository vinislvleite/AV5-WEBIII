package com.autobots.automanager.servicos;

import com.autobots.automanager.dtos.*;
import com.autobots.automanager.entitades.*;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FuncionarioServico {
    public FuncionarioServico(RepositorioEmpresa repositorioEmpresa) {
        this.repositorioEmpresa = repositorioEmpresa;
    }



    private final RepositorioEmpresa repositorioEmpresa;

    public List<FuncionarioDto> listarFuncionariosPorEmpresa(Long empresaId) {
        Empresa empresa = buscarEmpresa(empresaId);

        return empresa.getUsuarios().stream()
                .filter(u -> u.getPerfis().contains(PerfilUsuario.FUNCIONARIO))
                .map(u -> toDto(u, empresaId))
                .collect(Collectors.toList());
    }

    public FuncionarioDto obterFuncionarioPorEmpresa(Long empresaId, Long funcionarioId) {
        Empresa empresa = buscarEmpresa(empresaId);
        Usuario usuario = buscarFuncionario(empresa, funcionarioId);
        return toDto(usuario, empresaId);
    }

    public FuncionarioDto criarFuncionario(Long empresaId, FuncionarioDto dto) {
        Empresa empresa = buscarEmpresa(empresaId);
        Usuario usuario = toEntity(dto);
        empresa.getUsuarios().add(usuario);
        repositorioEmpresa.save(empresa);
        return toDto(usuario, empresaId);
    }

    public FuncionarioDto atualizarFuncionario(Long empresaId, Long funcionarioId, FuncionarioDto dto) {
        Empresa empresa = buscarEmpresa(empresaId);
        Usuario usuario = buscarFuncionario(empresa, funcionarioId);
        atualizarUsuario(usuario, dto);
        repositorioEmpresa.save(empresa);
        return toDto(usuario, empresaId);
    }

    public void excluirFuncionario(Long empresaId, Long funcionarioId) {
        Empresa empresa = buscarEmpresa(empresaId);
        Usuario usuario = buscarFuncionario(empresa, funcionarioId);
        empresa.getUsuarios().remove(usuario);
        repositorioEmpresa.save(empresa);
    }

    private Empresa buscarEmpresa(Long empresaId) {
        return repositorioEmpresa.findById(empresaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Empresa não encontrada com id: " + empresaId));
    }

    private Usuario buscarFuncionario(Empresa empresa, Long funcionarioId) {
        return empresa.getUsuarios().stream()
                .filter(u -> u.getId() != null && u.getId().equals(funcionarioId))
                .filter(u -> u.getPerfis().contains(PerfilUsuario.FUNCIONARIO))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Funcionário não encontrado com id: " + funcionarioId));
    }

    private Usuario toEntity(FuncionarioDto dto) {
        Usuario u = new Usuario();
        u.setNome(dto.getNome());
        u.setNomeSocial(dto.getNomeSocial());
        Set<PerfilUsuario> perfis = new HashSet<>();
        perfis.add(PerfilUsuario.FUNCIONARIO);
        if (dto.getPerfis() != null) {
            perfis.addAll(dto.getPerfis());
        }
        u.setPerfis(perfis);

        if (dto.getDocumentos() != null) {
            u.setDocumentos(dto.getDocumentos().stream().map(this::toDoc).collect(Collectors.toSet()));
        }
        if (dto.getTelefones() != null) {
            u.setTelefones(dto.getTelefones().stream().map(this::toTel).collect(Collectors.toSet()));
        }
        if (dto.getEmails() != null) {
            u.setEmails(dto.getEmails().stream().map(this::toEmail).collect(Collectors.toSet()));
        }
        if (dto.getEndereco() != null) {
            u.setEndereco(toEndereco(dto.getEndereco()));
        }
        return u;
    }

    private void atualizarUsuario(Usuario u, FuncionarioDto dto) {
        u.setNome(dto.getNome());
        u.setNomeSocial(dto.getNomeSocial());
        if (dto.getPerfis() != null) {
            Set<PerfilUsuario> perfis = new HashSet<>(dto.getPerfis());
            perfis.add(PerfilUsuario.FUNCIONARIO);
            u.setPerfis(perfis);
        }

        if (dto.getDocumentos() != null) {
            u.getDocumentos().clear();
            u.getDocumentos().addAll(dto.getDocumentos().stream().map(this::toDoc).collect(Collectors.toSet()));
        }
        if (dto.getTelefones() != null) {
            u.getTelefones().clear();
            u.getTelefones().addAll(dto.getTelefones().stream().map(this::toTel).collect(Collectors.toSet()));
        }
        if (dto.getEmails() != null) {
            u.getEmails().clear();
            u.getEmails().addAll(dto.getEmails().stream().map(this::toEmail).collect(Collectors.toSet()));
        }
        if (dto.getEndereco() != null) {
            u.setEndereco(toEndereco(dto.getEndereco()));
        }
    }

    private Documento toDoc(DocumentoDto d) {
        Documento dto = new Documento();
        dto.setId(d.getId());
        dto.setTipo(d.getTipo());
        dto.setNumero(d.getNumero());
        dto.setDataEmissao(d.getDataEmissao());
        return dto;
    }

    private Telefone toTel(TelefoneDto t) {
        Telefone dto = new Telefone();
        dto.setId(t.getId());
        dto.setDdd(t.getDdd());
        dto.setNumero(t.getNumero());
        return dto;
    }

    private Email toEmail(EmailDto e) {
        Email dto = new Email();
        dto.setId(e.getId());
        dto.setEndereco(e.getEndereco());
        return dto;
    }

    private Endereco toEndereco(EnderecoDto e) {
        Endereco dto = new Endereco();
        dto.setId(e.getId());
        dto.setEstado(e.getEstado());
        dto.setCidade(e.getCidade());
        dto.setBairro(e.getBairro());
        dto.setRua(e.getRua());
        dto.setNumero(e.getNumero());
        dto.setCodigoPostal(e.getCodigoPostal());
        dto.setInformacoesAdicionais(e.getInformacoesAdicionais());
        return dto;
    }

    private FuncionarioDto toDto(Usuario u, Long empresaId) {
        FuncionarioDto dto = new FuncionarioDto();
        dto.setId(u.getId());
        dto.setEmpresaId(empresaId);
        dto.setNome(u.getNome());
        dto.setNomeSocial(u.getNomeSocial());
        dto.setPerfis(u.getPerfis());

        if (u.getDocumentos() != null) {
            dto.setDocumentos(u.getDocumentos().stream().map(this::toDocDto).collect(Collectors.toSet()));
        }
        if (u.getTelefones() != null) {
            dto.setTelefones(u.getTelefones().stream().map(this::toTelDto).collect(Collectors.toSet()));
        }
        if (u.getEmails() != null) {
            dto.setEmails(u.getEmails().stream().map(this::toEmailDto).collect(Collectors.toSet()));
        }
        if (u.getEndereco() != null) {
            dto.setEndereco(toEndDto(u.getEndereco()));
        }
        return dto;
    }

    private DocumentoDto toDocDto(Documento d) {
        DocumentoDto dto = new DocumentoDto();
        dto.setId(d.getId());
        dto.setTipo(d.getTipo());
        dto.setNumero(d.getNumero());
        dto.setDataEmissao(d.getDataEmissao());
        return dto;
    }

    private TelefoneDto toTelDto(Telefone t) {
        TelefoneDto dto = new TelefoneDto();
        dto.setId(t.getId());
        dto.setDdd(t.getDdd());
        dto.setNumero(t.getNumero());
        return dto;
    }

    private EmailDto toEmailDto(Email e) {
        EmailDto dto = new EmailDto();
        dto.setId(e.getId());
        dto.setEndereco(e.getEndereco());
        return dto;
    }

    private EnderecoDto toEndDto(Endereco e) {
        EnderecoDto dto = new EnderecoDto();
        dto.setId(e.getId());
        dto.setEstado(e.getEstado());
        dto.setCidade(e.getCidade());
        dto.setBairro(e.getBairro());
        dto.setRua(e.getRua());
        dto.setNumero(e.getNumero());
        dto.setCodigoPostal(e.getCodigoPostal());
        dto.setInformacoesAdicionais(e.getInformacoesAdicionais());
        return dto;
    }
}
