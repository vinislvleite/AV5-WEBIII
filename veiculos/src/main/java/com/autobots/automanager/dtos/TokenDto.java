package com.autobots.automanager.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class TokenDto {
    private String token;
    private String tipo = "Bearer";
    public TokenDto(String token) { this.token = token; }
}
