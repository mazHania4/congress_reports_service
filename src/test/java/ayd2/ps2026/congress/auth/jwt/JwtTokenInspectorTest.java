package ayd2.ps2026.congress.auth.jwt;

import ayd2.ps2026.congress.common.exceptions.jwt.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenInspectorTest {

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private JwtTokenInspector jwtTokenInspector;

    private Key key;

    private static final String CLAIM_NAME_USER_ROLE = "role";
    private static final String CLAIM_USER_ID = "id";

    @BeforeEach
    void setUp() {
        String SECRET_STRING = "clave_secreta_muy_secreta_y_muy_larga_y_segura_para_las_pruebas_unitarias";
        key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
        lenient().when(jwtConfig.getSecretBytes()).thenReturn(SECRET_STRING.getBytes());
    }

    private String createToken(String subject, String role, Long userId, Date expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(CLAIM_NAME_USER_ROLE, role)
                .claim(CLAIM_USER_ID, userId)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    @Test
    void extractUserType_success() throws Exception {
        String token = createToken("user1", "ADMIN", 1L, new Date(System.currentTimeMillis() + 60000));
        String role = jwtTokenInspector.extractUserType(token);
        assertEquals("ADMIN", role);
    }

    @Test
    void extractUserType_throwsJwtNoUserTypeException_whenRoleIsNull() {
        String token = createToken("user1", null, 1L, new Date(System.currentTimeMillis() + 60000));
        assertThrows(JwtNoUserTypeException.class, () -> jwtTokenInspector.extractUserType(token));
    }

    @Test
    void extractUserType_throwsJwtClaimTypeMismatchException_whenTypeIsNotString() {
        String token = Jwts.builder()
                .claim(CLAIM_NAME_USER_ROLE, 12345) // Provoca RequiredTypeException al pedir String.class
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        assertThrows(JwtClaimTypeMismatchException.class, () -> jwtTokenInspector.extractUserType(token));
    }


    @Test
    void extractUsername_success() throws Exception {
        String token = createToken("john_doe", "USER", 1L, new Date(System.currentTimeMillis() + 60000));
        String username = jwtTokenInspector.extractUsername(token);
        assertEquals("john_doe", username);
    }

    @Test
    void extractUsername_throwsJwtNoUsernameException_whenSubjectIsNull() {
        String token = createToken(null, "USER", 1L, new Date(System.currentTimeMillis() + 60000));
        assertThrows(JwtNoUsernameException.class, () -> jwtTokenInspector.extractUsername(token));
    }


    @Test
    void isTokenValid_returnsTrue_whenNotExpired() throws Exception {
        String token = createToken("user", "USER", 1L, new Date(System.currentTimeMillis() + 60000));
        assertTrue(jwtTokenInspector.isTokenValid(token));
        assertFalse(jwtTokenInspector.isTokenExpired(token));
    }

    @Test
    void extractExpiration_throwsJwtNoExpirationException_whenNull() {
        String token = Jwts.builder()
                .setSubject("user")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        assertThrows(JwtNoExpirationException.class, () -> jwtTokenInspector.extractExpiration(token));
    }


    @Test
    void extractAllClaims_throwsJwtExpiredException() {
        String token = createToken("user", "USER", 1L, new Date(System.currentTimeMillis() - 600000));
        assertThrows(JwtExpiredException.class, () -> jwtTokenInspector.extractUsername(token));
    }

    @Test
    void extractAllClaims_throwsJwtSignatureInvalidException() {
        String token = createToken("user", "USER", 1L, new Date(System.currentTimeMillis() + 60000));
        String invalidSignatureToken = token + "corrupted";
        assertThrows(JwtSignatureInvalidException.class, () -> jwtTokenInspector.extractUsername(invalidSignatureToken));
    }

    @Test
    void extractAllClaims_throwsJwtMalformedException() {
        String malformedToken = "esto.no.esun_jwt_valido";
        assertThrows(JwtMalformedException.class, () -> jwtTokenInspector.extractUsername(malformedToken));
    }

    @Test
    void extractAllClaims_throwsJwtIllegalArgumentException_whenTokenIsEmptyOrNull() {
        assertThrows(JwtIllegalArgumentException.class, () -> jwtTokenInspector.extractUsername(""));
        assertThrows(JwtIllegalArgumentException.class, () -> jwtTokenInspector.extractUsername(null));
    }

    @Test
    void extractAllClaims_throwsJwtUnsupportedException() {
        String unsignedToken = Jwts.builder().setSubject("user").compact() + ".";
        assertThrows(JwtMalformedException.class, () -> jwtTokenInspector.extractUsername(unsignedToken));
    }

}