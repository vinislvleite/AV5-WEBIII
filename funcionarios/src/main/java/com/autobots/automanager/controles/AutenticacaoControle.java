package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.LoginDto;
import com.autobots.automanager.dtos.TokenDto;
import com.autobots.automanager.seguranca.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AutenticacaoControle {
    public AutenticacaoControle(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }



    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getNomeUsuario(), dto.getSenha()));
            return ResponseEntity.ok(new TokenDto(jwtUtil.gerarToken(auth.getName())));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
