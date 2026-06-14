package com.autobots.automanager.seguranca;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFiltro extends OncePerRequestFilter {
    public JwtFiltro(JwtUtil jwtUtil, AutenticacaoServico autenticacaoServico) {
        this.jwtUtil = jwtUtil;
        this.autenticacaoServico = autenticacaoServico;
    }



    private final JwtUtil jwtUtil;

    private final AutenticacaoServico autenticacaoServico;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String nomeUsuario = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            try {
                nomeUsuario = jwtUtil.extrairNomeUsuario(token);
            } catch (Exception ignored) {
            }
        }

        if (nomeUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = autenticacaoServico.loadUserByUsername(nomeUsuario);
            if (jwtUtil.validarToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
