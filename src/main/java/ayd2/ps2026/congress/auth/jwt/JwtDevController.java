package ayd2.ps2026.congress.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/dev")
public class JwtDevController {

    @GetMapping("/token")
    public String token() {

        String secret = "1NQqKmdyO0k36QEjFb6Ck8dDgESj8nSDTXVtTrzCQVc";

        return Jwts.builder()
                .claim("id", 1)
                .claim("username", "admin")
                .claim("role", "ADMIN")
                .setSubject("admin")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(
                        Keys.hmacShaKeyFor(secret.getBytes()),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }
}