package com.autobots.automanager.seguranca;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.*;
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
        String header = request.getHeader("Authorization");
        String token = null;
        String nomeUsuario = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try { nomeUsuario = jwtUtil.extrairNomeUsuario(token); } catch (Exception ignored) {}
        }

        if (nomeUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = autenticacaoServico.loadUserByUsername(nomeUsuario);
            if (jwtUtil.validarToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }
}
