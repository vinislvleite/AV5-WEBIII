package com.autobots.automanager.servicos;

import com.autobots.automanager.dtos.MercadoriaDto;
import com.autobots.automanager.dtos.ServicoDto;
import com.autobots.automanager.dtos.VendaDto;
import com.autobots.automanager.entitades.*;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaRelatorioServico {
    public VendaRelatorioServico(RepositorioEmpresa repositorioEmpresa) {
        this.repositorioEmpresa = repositorioEmpresa;
    }



    private final RepositorioEmpresa repositorioEmpresa;

    public List<VendaDto> listarVendasPorEmpresa(Long empresaId) {
        Empresa empresa = buscarEmpresa(empresaId);
        return empresa.getVendas().stream()
                .map(v -> toDto(v, empresaId))
                .collect(Collectors.toList());
    }

    public List<VendaDto> listarVendasPorPeriodo(Long empresaId, Date dataInicio, Date dataFim) {
        Empresa empresa = buscarEmpresa(empresaId);
        if (dataInicio == null || dataFim == null) {
            return listarVendasPorEmpresa(empresaId);
        }
        return empresa.getVendas().stream()
                .filter(v -> v.getCadastro() != null && !v.getCadastro().before(dataInicio) && !v.getCadastro().after(dataFim))
                .map(v -> toDto(v, empresaId))
                .collect(Collectors.toList());
    }

    public VendaDto obterVenda(Long empresaId, Long vendaId) {
        Venda venda = buscarVenda(empresaId, vendaId);
        return toDto(venda, empresaId);
    }

    public VendaDto criarVenda(Long empresaId, VendaDto dto) {
        Empresa empresa = buscarEmpresa(empresaId);
        Venda venda = toVenda(dto, empresa);
        if (venda.getCadastro() == null) {
            venda.setCadastro(new Date());
        }
        empresa.getVendas().add(venda);
        repositorioEmpresa.save(empresa);
        return toDto(venda, empresaId);
    }

    public VendaDto atualizarVenda(Long empresaId, Long vendaId, VendaDto dto) {
        Empresa empresa = buscarEmpresa(empresaId);
        Venda venda = buscarVenda(empresa, vendaId);
        atualizarVenda(venda, dto, empresa);
        repositorioEmpresa.save(empresa);
        return toDto(venda, empresaId);
    }

    public void excluirVenda(Long empresaId, Long vendaId) {
        Empresa empresa = buscarEmpresa(empresaId);
        empresa.getVendas().removeIf(v -> v.getId() != null && v.getId().equals(vendaId));
        repositorioEmpresa.save(empresa);
    }

    private Empresa buscarEmpresa(Long empresaId) {
        return repositorioEmpresa.findById(empresaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Empresa não encontrada com id: " + empresaId));
    }

    private Venda buscarVenda(Long empresaId, Long vendaId) {
        Empresa empresa = buscarEmpresa(empresaId);
        return buscarVenda(empresa, vendaId);
    }

    private Venda buscarVenda(Empresa empresa, Long vendaId) {
        return empresa.getVendas().stream()
                .filter(v -> v.getId() != null && v.getId().equals(vendaId))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Venda não encontrada com id: " + vendaId));
    }

    private Usuario buscarUsuario(Empresa empresa, Long usuarioId) {
        return empresa.getUsuarios().stream()
                .filter(u -> u.getId() != null && u.getId().equals(usuarioId))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Usuário não encontrado com id: " + usuarioId));
    }

    private Veiculo buscarVeiculo(Empresa empresa, Long veiculoId) {
        return empresa.getVeiculos().stream()
                .filter(v -> v.getId() != null && v.getId().equals(veiculoId))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Veículo não encontrado com id: " + veiculoId));
    }

    private VendaDto toDto(Venda venda, Long empresaId) {
        VendaDto dto = new VendaDto();
        dto.setId(venda.getId());
        dto.setEmpresaId(empresaId);
        dto.setIdentificacao(venda.getIdentificacao());
        dto.setCadastro(venda.getCadastro());
        if (venda.getCliente() != null) {
            dto.setClienteId(venda.getCliente().getId());
            dto.setClienteNome(venda.getCliente().getNome());
        }
        if (venda.getFuncionario() != null) {
            dto.setFuncionarioId(venda.getFuncionario().getId());
            dto.setFuncionarioNome(venda.getFuncionario().getNome());
        }
        if (venda.getVeiculo() != null) {
            dto.setVeiculoId(venda.getVeiculo().getId());
            dto.setVeiculoModelo(venda.getVeiculo().getModelo());
            dto.setVeiculoPlaca(venda.getVeiculo().getPlaca());
        }
        if (venda.getMercadorias() != null) {
            dto.setMercadorias(venda.getMercadorias().stream().map(this::toMercadoriaDto).collect(Collectors.toSet()));
        }
        if (venda.getServicos() != null) {
            dto.setServicos(venda.getServicos().stream().map(this::toServicoDto).collect(Collectors.toSet()));
        }
        return dto;
    }

    private Venda toVenda(VendaDto dto, Empresa empresa) {
        Venda venda = new Venda();
        venda.setId(dto.getId());
        venda.setIdentificacao(dto.getIdentificacao());
        venda.setCadastro(dto.getCadastro());
        if (dto.getClienteId() != null) {
            venda.setCliente(buscarUsuario(empresa, dto.getClienteId()));
        }
        if (dto.getFuncionarioId() != null) {
            venda.setFuncionario(buscarUsuario(empresa, dto.getFuncionarioId()));
        }
        if (dto.getVeiculoId() != null) {
            venda.setVeiculo(buscarVeiculo(empresa, dto.getVeiculoId()));
        }
        if (dto.getMercadorias() != null) {
            venda.setMercadorias(dto.getMercadorias().stream().map(this::toMercadoria).collect(Collectors.toSet()));
        }
        if (dto.getServicos() != null) {
            venda.setServicos(dto.getServicos().stream().map(this::toServico).collect(Collectors.toSet()));
        }
        return venda;
    }

    private void atualizarVenda(Venda venda, VendaDto dto, Empresa empresa) {
        venda.setIdentificacao(dto.getIdentificacao());
        if (dto.getCadastro() != null) {
            venda.setCadastro(dto.getCadastro());
        }
        if (dto.getClienteId() != null) {
            venda.setCliente(buscarUsuario(empresa, dto.getClienteId()));
        }
        if (dto.getFuncionarioId() != null) {
            venda.setFuncionario(buscarUsuario(empresa, dto.getFuncionarioId()));
        }
        if (dto.getVeiculoId() != null) {
            venda.setVeiculo(buscarVeiculo(empresa, dto.getVeiculoId()));
        }
        if (dto.getMercadorias() != null) {
            venda.getMercadorias().clear();
            venda.getMercadorias().addAll(dto.getMercadorias().stream().map(this::toMercadoria).collect(Collectors.toSet()));
        }
        if (dto.getServicos() != null) {
            venda.getServicos().clear();
            venda.getServicos().addAll(dto.getServicos().stream().map(this::toServico).collect(Collectors.toSet()));
        }
    }

    private Mercadoria toMercadoria(MercadoriaDto dto) {
        Mercadoria m = new Mercadoria();
        m.setId(dto.getId());
        m.setNome(dto.getNome());
        m.setDescricao(dto.getDescricao());
        m.setValor(dto.getValor());
        m.setQuantidade(dto.getQuantidade());
        m.setValidade(dto.getValidade());
        m.setFabricao(dto.getFabricao());
        m.setCadastro(dto.getCadastro());
        return m;
    }

    private Servico toServico(ServicoDto dto) {
        Servico s = new Servico();
        s.setId(dto.getId());
        s.setNome(dto.getNome());
        s.setDescricao(dto.getDescricao());
        s.setValor(dto.getValor());
        return s;
    }

    private MercadoriaDto toMercadoriaDto(Mercadoria m) {
        MercadoriaDto dto = new MercadoriaDto();
        dto.setId(m.getId());
        dto.setNome(m.getNome());
        dto.setDescricao(m.getDescricao());
        dto.setValor(m.getValor());
        dto.setQuantidade(m.getQuantidade());
        dto.setValidade(m.getValidade());
        dto.setFabricao(m.getFabricao());
        dto.setCadastro(m.getCadastro());
        return dto;
    }

    private ServicoDto toServicoDto(Servico s) {
        ServicoDto dto = new ServicoDto();
        dto.setId(s.getId());
        dto.setNome(s.getNome());
        dto.setDescricao(s.getDescricao());
        dto.setValor(s.getValor());
        return dto;
    }
}
