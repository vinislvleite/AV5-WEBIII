package com.autobots.automanager.seguranca;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutenticacaoServico implements UserDetailsService {
    public AutenticacaoServico(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }



    private final RepositorioUsuario repositorioUsuario;

    @Override
    public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {
        Usuario usuario = repositorioUsuario.findAll().stream()
                .filter(u -> u.getCredenciais().stream()
                        .filter(c -> c instanceof CredencialUsuarioSenha)
                        .map(c -> (CredencialUsuarioSenha) c)
                        .anyMatch(c -> c.getNomeUsuario().equals(nomeUsuario) && !c.isInativo()))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + nomeUsuario));

        String senha = usuario.getCredenciais().stream()
                .filter(c -> c instanceof CredencialUsuarioSenha)
                .map(c -> (CredencialUsuarioSenha) c)
                .filter(c -> c.getNomeUsuario().equals(nomeUsuario))
                .map(CredencialUsuarioSenha::getSenha)
                .findFirst().orElse("");

        List<SimpleGrantedAuthority> authorities = usuario.getPerfis().stream()
                .map(p -> new SimpleGrantedAuthority("ROLE_" + p.name()))
                .collect(Collectors.toList());

        return new User(nomeUsuario, senha, authorities);
    }
}
