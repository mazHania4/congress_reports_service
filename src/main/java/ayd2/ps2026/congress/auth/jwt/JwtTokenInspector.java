package ayd2.ps2026.congress.auth.jwt;

import java.util.Date;

import ayd2.ps2026.congress.common.exceptions.jwt.*;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase para manejar internamente metodos para inspeccionar el jwt
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenInspector {

    private final JwtConfig jwtConfig;

    public String extractUserType(String token) throws InvalidTokenException {
        try {

            String userType = extractAllClaims(token).get(JwtGeneratorService.CLAIM_NAME_USER_ROLE, String.class);

            if (userType == null) {
                throw new JwtNoUserTypeException();
            }
            return userType;

        } catch (RequiredTypeException ex) {
            throw new JwtClaimTypeMismatchException();
        } catch (InvalidTokenException ex) {
            throw ex;
        }
    }

    /**
     * Extrae y devuelve el nombre de usuario del token jwt.
     *
     * @param token el token jwt del cual se extraerá el nombre de usuario.
     * @return el nombre de usuario contenido en el token.
     */
    public String extractUsername(String token) throws InvalidTokenException {

        String username = extractAllClaims(token).getSubject();

        if (username == null) {
            throw new JwtNoUsernameException();
        }

        return username;

    }

    /**
     * Verifica si expiracion del token es antes que la fecha actual
     *
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) throws InvalidTokenException {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae y devuelve la fecha de expiración del token jwt.
     *
     * @param token el token jwt del cual se extraerá la fecha de expiración.
     * @return la fecha de expiración contenida en el token.
     */
    public Date extractExpiration(String token) throws InvalidTokenException {

        Date expiration = extractAllClaims(token).getExpiration();

        if (expiration == null) {// si viene nulo entonces debemos decir error al usuario
            throw new JwtNoExpirationException();
        }

        return expiration;

    }

    /**
     * Valida si un token jwt es valido verificando que el token no haya expirado.
     *
     * @param token el token jwt a validar.
     * @return true si el token es válido, false en caso contrario.
     */
    public Boolean isTokenValid(String token) throws InvalidTokenException {

        return !isTokenExpired(token);

    }

    /**
     * Extrae y devuelve los claims de un token jwt firmado.
     *
     * @param token el token jwt a analizar.
     * @return los claims contenidos en el token.
     */
    private Claims extractAllClaims(String token) throws InvalidTokenException {
        try {
            // crea un parser para validar el jwt
            return Jwts.parserBuilder()
                    // aqui se verifica el jwt con la llave secretea
                    .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecretBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedException();
        } catch (MalformedJwtException e) {
            throw new JwtMalformedException();
        } catch (SignatureException e) {
            throw new JwtSignatureInvalidException();
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException();
        } catch (IllegalArgumentException e) {
            throw new JwtIllegalArgumentException();
        }

    }

}
