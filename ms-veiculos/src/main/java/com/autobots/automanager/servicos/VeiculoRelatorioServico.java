package com.autobots.automanager.servicos;

import com.autobots.automanager.dtos.VeiculoDto;
import com.autobots.automanager.entitades.*;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeiculoRelatorioServico {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    public List<VeiculoDto> listarVeiculosAtendidos(Long empresaId) {
        Empresa empresa = repositorioEmpresa.findById(empresaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Empresa não encontrada com id: " + empresaId));

        return empresa.getVendas().stream()
                .filter(v -> v.getVeiculo() != null)
                .map(Venda::getVeiculo)
                .distinct()
                .map(v -> toDto(v, empresaId))
                .collect(Collectors.toList());
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
}
