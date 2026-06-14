package com.autobots.automanager.entitades;

import java.util.*;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String razaoSocial;
    @Column
    private String nomeFantasia;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Telefone> telefones = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;
    @Column(nullable = false)
    private java.util.Date cadastro;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Usuario> usuarios = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Mercadoria> mercadorias = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Servico> servicos = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Veiculo> veiculos = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Venda> vendas = new HashSet<>();
}
