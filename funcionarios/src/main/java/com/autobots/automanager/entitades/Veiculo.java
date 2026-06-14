package com.autobots.automanager.entitades;

import java.util.*;
import javax.persistence.*;
import com.autobots.automanager.enumeracoes.TipoVeiculo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"proprietario", "vendas"})
@Entity
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private TipoVeiculo tipo;
    @Column(nullable = false)
    private String modelo;
    @Column(nullable = false)
    private String placa;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Usuario proprietario;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Venda> vendas = new HashSet<>();
}
