package com.autobots.automanager.repositorios;

import com.autobots.automanager.entitades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioEmpresa extends JpaRepository<Empresa, Long> {
}
