package com.autobots.automanager.servicos;

import com.autobots.automanager.dtos.*;
import com.autobots.automanager.entitades.*;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProdutosServicosServico {
    public ProdutosServicosServico(RepositorioEmpresa repositorioEmpresa) {
        this.repositorioEmpresa = repositorioEmpresa;
    }



    private final RepositorioEmpresa repositorioEmpresa;

    public ProdutosServicosDto listar(Long empresaId) {
        Empresa empresa = buscarEmpresa(empresaId);

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

    public MercadoriaDto obterMercadoria(Long empresaId, Long mercadoriaId) {
        Empresa empresa = buscarEmpresa(empresaId);
        Mercadoria mercadoria = empresa.getMercadorias().stream()
                .filter(m -> m.getId() != null && m.getId().equals(mercadoriaId))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Mercadoria não encontrada com id: " + mercadoriaId));
        return toMercDto(mercadoria);
    }

    public MercadoriaDto criarMercadoria(Long empresaId, MercadoriaDto dto) {
        Empresa empresa = buscarEmpresa(empresaId);
        Mercadoria mercadoria = toMercadoria(dto);
        if (mercadoria.getCadastro() == null) {
            mercadoria.setCadastro(new Date());
        }
        empresa.getMercadorias().add(mercadoria);
        repositorioEmpresa.save(empresa);
        return toMercDto(mercadoria);
    }

    public MercadoriaDto atualizarMercadoria(Long empresaId, Long mercadoriaId, MercadoriaDto dto) {
        Empresa empresa = buscarEmpresa(empresaId);
        Mercadoria mercadoria = empresa.getMercadorias().stream()
                .filter(m -> m.getId() != null && m.getId().equals(mercadoriaId))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Mercadoria não encontrada com id: " + mercadoriaId));
        atualizarMercadoria(mercadoria, dto);
        repositorioEmpresa.save(empresa);
        return toMercDto(mercadoria);
    }

    public void excluirMercadoria(Long empresaId, Long mercadoriaId) {
        Empresa empresa = buscarEmpresa(empresaId);
        empresa.getMercadorias().removeIf(m -> m.getId() != null && m.getId().equals(mercadoriaId));
        repositorioEmpresa.save(empresa);
    }

    public ServicoDto obterServico(Long empresaId, Long servicoId) {
        Empresa empresa = buscarEmpresa(empresaId);
        Servico servico = empresa.getServicos().stream()
                .filter(s -> s.getId() != null && s.getId().equals(servicoId))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Serviço não encontrado com id: " + servicoId));
        return toServDto(servico);
    }

    public ServicoDto criarServico(Long empresaId, ServicoDto dto) {
        Empresa empresa = buscarEmpresa(empresaId);
        Servico servico = toServico(dto);
        empresa.getServicos().add(servico);
        repositorioEmpresa.save(empresa);
        return toServDto(servico);
    }

    public ServicoDto atualizarServico(Long empresaId, Long servicoId, ServicoDto dto) {
        Empresa empresa = buscarEmpresa(empresaId);
        Servico servico = empresa.getServicos().stream()
                .filter(s -> s.getId() != null && s.getId().equals(servicoId))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Serviço não encontrado com id: " + servicoId));
        atualizarServico(servico, dto);
        repositorioEmpresa.save(empresa);
        return toServDto(servico);
    }

    public void excluirServico(Long empresaId, Long servicoId) {
        Empresa empresa = buscarEmpresa(empresaId);
        empresa.getServicos().removeIf(s -> s.getId() != null && s.getId().equals(servicoId));
        repositorioEmpresa.save(empresa);
    }

    private Empresa buscarEmpresa(Long empresaId) {
        return repositorioEmpresa.findById(empresaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Empresa não encontrada com id: " + empresaId));
    }

    private MercadoriaDto toMercDto(Mercadoria m) {
        MercadoriaDto dto = new MercadoriaDto();
        dto.setId(m.getId()); dto.setNome(m.getNome()); dto.setDescricao(m.getDescricao());
        dto.setValor(m.getValor()); dto.setQuantidade(m.getQuantidade());
        dto.setValidade(m.getValidade()); dto.setFabricao(m.getFabricao());
        dto.setCadastro(m.getCadastro());
        return dto;
    }

    private Mercadoria toMercadoria(MercadoriaDto dto) {
        Mercadoria entidade = new Mercadoria();
        entidade.setId(dto.getId());
        entidade.setNome(dto.getNome());
        entidade.setDescricao(dto.getDescricao());
        entidade.setValor(dto.getValor());
        entidade.setQuantidade(dto.getQuantidade());
        entidade.setValidade(dto.getValidade());
        entidade.setFabricao(dto.getFabricao());
        entidade.setCadastro(dto.getCadastro());
        return entidade;
    }

    private void atualizarMercadoria(Mercadoria entidade, MercadoriaDto dto) {
        entidade.setNome(dto.getNome());
        entidade.setDescricao(dto.getDescricao());
        entidade.setValor(dto.getValor());
        entidade.setQuantidade(dto.getQuantidade());
        entidade.setValidade(dto.getValidade());
        entidade.setFabricao(dto.getFabricao());
        if (dto.getCadastro() != null) {
            entidade.setCadastro(dto.getCadastro());
        }
    }

    private ServicoDto toServDto(Servico s) {
        ServicoDto dto = new ServicoDto();
        dto.setId(s.getId()); dto.setNome(s.getNome());
        dto.setDescricao(s.getDescricao()); dto.setValor(s.getValor());
        return dto;
    }

    private Servico toServico(ServicoDto dto) {
        Servico entidade = new Servico();
        entidade.setId(dto.getId());
        entidade.setNome(dto.getNome());
        entidade.setDescricao(dto.getDescricao());
        entidade.setValor(dto.getValor());
        return entidade;
    }

    private void atualizarServico(Servico entidade, ServicoDto dto) {
        entidade.setNome(dto.getNome());
        entidade.setDescricao(dto.getDescricao());
        entidade.setValor(dto.getValor());
    }
}
