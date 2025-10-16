package com.nutritrack.NutriTrack.service;

import com.nutritrack.NutriTrack.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import java.util.function.Function;

/**
 * Serviço responsável por todas as operações de manipulação de Tokens Web JSON (JWT).
 * <p>
 * Inclui funcionalidades para:
 * <ul>
 * <li>Extrair informações (username e ID do usuário) de um token.</li>
 * <li>Gerar novos tokens com base nos detalhes do usuário.</li>
 * <li>Validar a integridade e a expiração de um token.</li>
 * </ul>
 * <p>
 * A chave secreta e o tempo de expiração são injetados a partir das propriedades
 * de configuração do Spring ({@code application.properties} ou similar).
 */
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Extrai o nome de usuário (Subject/Assunto) do token JWT.
     * <p>
     * O username é tipicamente o email do usuário na aplicação NutriTrack.
     *
     * @param token O JWT a ser processado.
     * @return O nome de usuário (email) contido no token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * Extrai uma claim específica do token JWT.
     * <p>
     * Este é um método utilitário genérico que permite resolver qualquer
     * claim a partir de uma função fornecida.
     *
     * @param token O JWT a ser processado.
     * @param claimsResolver Função para resolver e retornar o valor da claim desejada.
     * @param <T> O tipo de dado da claim esperada.
     * @return O valor da claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Gera um novo token JWT para o usuário especificado.
     * <p>
     * Este método sobrecarregado automaticamente adiciona o ID do usuário como uma
     * claim extra no token.
     *
     * @param userDetails Os detalhes do usuário (deve ser uma instância de {@code Usuario})
     * para quem o token será gerado.
     * @return O token JWT assinado e compactado.
     */
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> extraClaims = new HashMap<>();

        var user = (Usuario) userDetails;
        extraClaims.put("userId", user.getId());

        return generateToken(extraClaims, userDetails);
    }

    /**
     * Gera um novo token JWT, incluindo claims extras fornecidas.
     * <p>
     * Este é o método base que chama o {@code buildToken} e é usado quando
     * claims adicionais precisam ser incluídas. Também garante a inclusão
     * do ID do usuário se não estiver presente.
     *
     * @param extraClaims Claims adicionais a serem incluídas no payload do token.
     * @param userDetails Os detalhes do usuário para o Subject e data de expiração.
     * @return O token JWT assinado e compactado.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        if (!extraClaims.containsKey("userId") && userDetails instanceof Usuario) {
            var user = (Usuario) userDetails;
            extraClaims.put("userId", user.getId());
        }
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Verifica se um token JWT é válido.
     * <p>
     * A validação é feita em duas etapas:
     * 1. O username (email) extraído do token deve ser igual ao username do {@code UserDetails}.
     * 2. O token não deve ter expirado.
     *
     * @param token O JWT a ser validado.
     * @param userDetails Os detalhes do usuário que se espera ser o dono do token.
     * @return {@code true} se o token for válido e não expirado, {@code false} caso contrário.
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}