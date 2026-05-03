package ayd2.ps2026.congress.auth.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ayd2.ps2026.congress.common.enums.PublicEndpointsEnum;
import ayd2.ps2026.congress.common.exceptions.jwt.InvalidTokenException;
import ayd2.ps2026.congress.common.models.dto.response.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenInspector jwtTokenInspector;

    /**
     * Método principal que intercepta cada solicitud HTTP y ejecuta la
     * validación del token JWT.
     *
     * @param request     La solicitud HTTP entrante.
     * @param response    La respuesta HTTP saliente.
     * @param filterChain La cadena de filtros de seguridad.
     * @throws ServletException Si ocurre un error de filtro.
     * @throws IOException      Si ocurre un error de E/S.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Saltar validación si es endpoint público
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> tokenOptional = extractTokenFromHeader(request);

        if (tokenOptional.isPresent()) {

            try {
                String token = tokenOptional.get();
                Optional<UserDetails> userDetailsOptional = validateToken(token);
                if (userDetailsOptional.isPresent()) {
                    authenticateUser(userDetailsOptional.get(), token, request);
                }

            } catch (InvalidTokenException ex) {
                log.warn(ex.getMessage());

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(
                        new ErrorDTO("Token inválido o expirado.")
                ));

            }

        }
        filterChain.doFilter(request, response);
    }

    /**
     * Verifica si la URI corresponde a un endpoint público permitido sin
     * autenticación.
     *

     */
    protected boolean isPublicEndpoint(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        String requestMethod = request.getMethod(); // <-- ESTA LÍNEA
        AntPathMatcher matcher = new AntPathMatcher();

        for (PublicEndpointsEnum endpoint : PublicEndpointsEnum.values()) {

            // 1. Validar path
            if (!matcher.match(endpoint.getPath(), requestPath)) {
                continue;
            }

            // 2. Validar método HTTP
            if (endpoint.getMethod() == null) {
                return true; // Permitir todos los métodos
            }

            if (endpoint.getMethod().matches(requestMethod)) {
                return true; // Solo permite si coincide el método
            }
        }

        return false;
    }


    /**
     * Extrae el token JWT del encabezado `Authorization` de la solicitud.
     *
     * @param request La solicitud HTTP de la que se extraerá el token.
     * @return Un {@code Optional} que contiene el token JWT si está presente y
     *         es válido.
     */
    protected Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7));
    }

    /**
     * Valida el token JWT y devuelve los detalles del usuario si el token es
     * válido.
     *
     * @param jwt El token JWT a validar.
     * @return Un {@code Optional} con los detalles del usuario si el token es
     *         válido.
     * @throws InvalidTokenException
     */
    protected Optional<UserDetails> validateToken(String jwt) {

        String username = jwtTokenInspector.extractUsername(jwt);
        String userType = jwtTokenInspector.extractUserType(jwt);

        // Validar si el token ya ha sido autenticado
        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return Optional.empty();
        }

        // creamos el usuario Spring para que sea cargado en el contexto
        User user = new User(username, "", List.of(new SimpleGrantedAuthority("ROLE_" + userType)));

        if (jwtTokenInspector.isTokenValid(jwt)) {
            log.info("Usuario autenticado exitosamente: {}", username);
            return Optional.of(user);
        }

        log.warn("Token JWT inválido para el usuario: {}", username);
        return Optional.empty();

    }

    /**
     * Autentica al usuario y establece el contexto de seguridad de Spring.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @param token       El token JWT usado para la autenticación.
     * @param request     La solicitud HTTP actual.
     */
    protected void authenticateUser(UserDetails userDetails, String token, HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());

        //authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        Map<String, Object> authDetails = new HashMap<>();
        authDetails.put("jwt", token);
        authDetails.put("ip", request.getRemoteAddr());
        authenticationToken.setDetails(authDetails);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
