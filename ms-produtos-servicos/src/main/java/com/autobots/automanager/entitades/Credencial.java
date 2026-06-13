package com.autobots.automanager.entitades;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Credencial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Date criacao;
    @Column
    private Date ultimoAcesso;
    @Column(nullable = false)
    private boolean inativo;
}
