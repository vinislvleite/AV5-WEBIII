package com.autobots.automanager.servicos;

import com.autobots.automanager.dtos.*;
import com.autobots.automanager.entitades.*;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProdutosServicosServico {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    public ProdutosServicosDto listar(Long empresaId) {
        Empresa empresa = repositorioEmpresa.findById(empresaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Empresa não encontrada com id: " + empresaId));

        ProdutosServicosDto dto = new ProdutosServicosDto();
        dto.setEmpresaId(empresa.getId());
        dto.setRazaoSocial(empresa.getRazaoSocial());
        dto.setNomeFantasia(empresa.getNomeFantasia());
        dto.setMercadorias(empresa.getMercadorias().stream()
                .map(this::toMercDto).collect(Collectors.toSet()));
        dto.setServicos(empresa.getServicos().stream()
                .map(this::toServDto).collect(Collectors.toSet()));
        return dto;
    }

    private MercadoriaDto toMercDto(Mercadoria m) {
        MercadoriaDto dto = new MercadoriaDto();
        dto.setId(m.getId()); dto.setNome(m.getNome()); dto.setDescricao(m.getDescricao());
        dto.setValor(m.getValor()); dto.setQuantidade(m.getQuantidade());
        dto.setValidade(m.getValidade()); dto.setFabricao(m.getFabricao());
        dto.setCadastro(m.getCadastro());
        return dto;
    }

    private ServicoDto toServDto(Servico s) {
        ServicoDto dto = new ServicoDto();
        dto.setId(s.getId()); dto.setNome(s.getNome());
        dto.setDescricao(s.getDescricao()); dto.setValor(s.getValor());
        return dto;
    }
}
