package ayd2.ps2026.congress.auth.jwt;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ayd2.ps2026.congress.auth.users.models.AppUser;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la generación de tokens JWT para usuarios autenticados.
 * 
 * Este servicio construye tokens firmados con información básica del usuario
 * como el rol y el nombre, utilizando una clave secreta definida en la
 * configuración.
 * 
 * El token generado incluye fecha de emisión, expiración y está firmado con
 * HMAC-SHA256.
 * 
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-01
 */
@Service
@RequiredArgsConstructor
public class JwtGeneratorService {

    private final JwtConfig jwtConfig;

    /**
     * Nombres de las claims que seran incluidas en el toke JWT
     */
    public static final String CLAIM_USER_ID = "id";
    public static final String CLAIM_USER_USERNAME = "username";
    public static final String CLAIM_NAME_USER_ROLE = "role";
    public static final String CLAIM_NAME_USER_STATUS = "status";

    /**
     * Cnfiguracion del timpo de valides del token JWT
     */
    private static final Long JWT_TOKEN_VALIDITY_HOURS = 48L;
    private static final Long JWT_TOKEN_TIME_VALIDITY = Duration.ofHours(JWT_TOKEN_VALIDITY_HOURS).toMillis();


    /**
     * Genera un token JWT para el usuario autenticado.
     * 
     * Este método construye un conjunto básico de claims, incluyendo el rol actual
     * del usuario como autoridad, y luego delega la creación del token firmado.
     *
     */
    public String generateToken(AppUser appUser) {
        // mandamos a cargar las claims (las base porque solo esas son necesarias)
        Map<String, Object> claims = new HashMap<>();

        // Agregar el rol del usuario en las autorities
        claims.put(CLAIM_USER_ID, appUser.getId());
        claims.put(CLAIM_USER_USERNAME, appUser.getUsername());
        claims.put(CLAIM_NAME_USER_ROLE, appUser.getRole().name());

        long validityMillis = Duration.ofMillis(parseDuration(jwtConfig.getJwtExpiresIn())).toMillis();
        // Generar el token
        return createToken(claims, appUser.getUsername(), validityMillis);
    }

    /**
     * Crea un token JWT firmado a partir de las claims y el nombre de usuario.
     * 
     * El token generado incluye una fecha de emisión, fecha de expiración
     * y se firma utilizando la clave secreta configurada.
     *
     */
    private String createToken(Map<String, Object> claims, String username, long validityMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityMillis))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    private long parseDuration(String duration) {                                                                                                                       
        if (duration.endsWith("h")) {
            return Long.parseLong(duration.replace("h", "")) * 60 * 60 * 1000;
        } else if (duration.endsWith("m")) {
            return Long.parseLong(duration.replace("m", "")) * 60 * 1000;
        } else if (duration.endsWith("s")) {
            return Long.parseLong(duration.replace("s", "")) * 1000;
        } else {
            throw new IllegalArgumentException("Formato de duración no válido: " + duration);
        }
    }
}
