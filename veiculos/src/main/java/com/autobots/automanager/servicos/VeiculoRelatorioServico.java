package com.autobots.automanager.servicos;

import com.autobots.automanager.dtos.VeiculoDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VeiculoRelatorioServico {
    public VeiculoRelatorioServico(RepositorioEmpresa repositorioEmpresa) {
        this.repositorioEmpresa = repositorioEmpresa;
    }



    private final RepositorioEmpresa repositorioEmpresa;

    public List<VeiculoDto> listarVeiculosPorEmpresa(Long empresaId) {
        Empresa empresa = buscarEmpresa(empresaId);
        return empresa.getVeiculos().stream()
                .filter(Objects::nonNull)
                .map(v -> toDto(v, empresaId))
                .collect(Collectors.toList());
    }

    public List<VeiculoDto> listarVeiculosAtendidos(Long empresaId) {
        Empresa empresa = buscarEmpresa(empresaId);
        return empresa.getVendas().stream()
                .filter(v -> v.getVeiculo() != null)
                .map(Venda::getVeiculo)
                .distinct()
                .map(v -> toDto(v, empresaId))
                .collect(Collectors.toList());
    }

    public VeiculoDto obterVeiculo(Long empresaId, Long veiculoId) {
        Empresa empresa = buscarEmpresa(empresaId);
        Veiculo veiculo = buscarVeiculo(empresa, veiculoId);
        return toDto(veiculo, empresaId);
    }

    public VeiculoDto criarVeiculo(Long empresaId, VeiculoDto dto) {
        Empresa empresa = buscarEmpresa(empresaId);
        Veiculo veiculo = toVeiculo(dto);
        if (dto.getProprietarioId() != null) {
            veiculo.setProprietario(buscarUsuario(empresa, dto.getProprietarioId()));
        }
        empresa.getVeiculos().add(veiculo);
        repositorioEmpresa.save(empresa);
        return toDto(veiculo, empresaId);
    }

    public VeiculoDto atualizarVeiculo(Long empresaId, Long veiculoId, VeiculoDto dto) {
        Empresa empresa = buscarEmpresa(empresaId);
        Veiculo veiculo = buscarVeiculo(empresa, veiculoId);
        atualizarVeiculo(veiculo, dto);
        if (dto.getProprietarioId() != null) {
            veiculo.setProprietario(buscarUsuario(empresa, dto.getProprietarioId()));
        } else {
            veiculo.setProprietario(null);
        }
        repositorioEmpresa.save(empresa);
        return toDto(veiculo, empresaId);
    }

    public void excluirVeiculo(Long empresaId, Long veiculoId) {
        Empresa empresa = buscarEmpresa(empresaId);
        empresa.getVeiculos().removeIf(v -> v.getId() != null && v.getId().equals(veiculoId));
        repositorioEmpresa.save(empresa);
    }

    private Empresa buscarEmpresa(Long empresaId) {
        return repositorioEmpresa.findById(empresaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Empresa não encontrada com id: " + empresaId));
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

    private VeiculoDto toDto(Veiculo v, Long empresaId) {
        VeiculoDto dto = new VeiculoDto();
        dto.setId(v.getId());
        dto.setEmpresaId(empresaId);
        dto.setTipo(v.getTipo());
        dto.setModelo(v.getModelo());
        dto.setPlaca(v.getPlaca());
        if (v.getProprietario() != null) {
            dto.setProprietarioId(v.getProprietario().getId());
            dto.setProprietarioNome(v.getProprietario().getNome());
        }
        return dto;
    }

    private Veiculo toVeiculo(VeiculoDto dto) {
        Veiculo entidade = new Veiculo();
        entidade.setId(dto.getId());
        entidade.setTipo(dto.getTipo());
        entidade.setModelo(dto.getModelo());
        entidade.setPlaca(dto.getPlaca());
        return entidade;
    }

    private void atualizarVeiculo(Veiculo entidade, VeiculoDto dto) {
        entidade.setTipo(dto.getTipo());
        entidade.setModelo(dto.getModelo());
        entidade.setPlaca(dto.getPlaca());
    }
}
