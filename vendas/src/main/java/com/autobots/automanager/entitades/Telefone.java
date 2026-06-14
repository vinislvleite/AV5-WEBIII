package com.autobots.automanager.entitades;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class Telefone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String ddd;
    @Column(nullable = false)
    private String numero;
}
