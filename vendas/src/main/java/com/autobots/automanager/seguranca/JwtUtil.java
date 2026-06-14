package com.autobots.automanager.seguranca;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String gerarToken(String nomeUsuario) {
        return Jwts.builder()
                .setSubject(nomeUsuario)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String extrairNomeUsuario(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validarToken(String token, String nomeUsuario) {
        try {
            String usuario = extrairNomeUsuario(token);
            Date exp = Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token).getBody().getExpiration();
            return usuario.equals(nomeUsuario) && exp.after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }
}
