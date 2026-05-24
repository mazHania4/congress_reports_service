package ayd2.ps2026.congress.auth.jwt;

import ayd2.ps2026.congress.common.enums.PublicEndpointsEnum;
import ayd2.ps2026.congress.common.exceptions.jwt.JwtMalformedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenInspector jwtTokenInspector;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void doFilterInternal_whenEndpointIsPublic_shouldSkipValidation() throws Exception {
        PublicEndpointsEnum[] endpoints = PublicEndpointsEnum.values();
        if (endpoints.length > 0) {
            PublicEndpointsEnum target = endpoints[0];
            String pathValido = target.getPath().replace("**", "any/path").replace("{id}", "1");
            when(request.getRequestURI()).thenReturn(pathValido);

            if (target.getMethod() != null) {
                when(request.getMethod()).thenReturn(String.valueOf(target.getMethod()));
            } else {
                when(request.getMethod()).thenReturn("GET");
            }

            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verifyNoInteractions(jwtTokenInspector);
        }
    }

    @Test
    void doFilterInternal_whenUserDetailsOptionalIsEmpty_shouldNotAuthenticateButContinueChain() throws Exception {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/private/resource");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer token_no_valido");

        when(jwtTokenInspector.extractUsername("token_no_valido")).thenReturn("john_doe");
        when(jwtTokenInspector.extractUserType("token_no_valido")).thenReturn("USER");
        when(jwtTokenInspector.isTokenValid("token_no_valido")).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_whenEndpointIsPrivateAndNoToken_shouldContinueChainWithoutAuth() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/private/resource");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(null); // No token

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_whenTokenIsInvalid_shouldWriteErrorToResponse() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/private/resource");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer token_invalido");

        when(jwtTokenInspector.extractUsername("token_invalido")).thenThrow(new JwtMalformedException());
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        assertTrue(stringWriter.toString().contains("Token inválido o expirado."));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_success_shouldAuthenticateUser() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/private/resource");
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader("Authorization")).thenReturn("Bearer token_valido");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        when(jwtTokenInspector.extractUsername("token_valido")).thenReturn("john_doe");
        when(jwtTokenInspector.extractUserType("token_valido")).thenReturn("ADMIN");
        when(jwtTokenInspector.isTokenValid("token_valido")).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("john_doe", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void isPublicEndpoint_shouldReturnFalse_whenNoMatch() {
        when(request.getRequestURI()).thenReturn("/ruta/que/no/existe/nunca/en/el/enum");

        boolean result = jwtAuthenticationFilter.isPublicEndpoint(request);

        assertFalse(result);
    }


    @Test
    void extractTokenFromHeader_shouldReturnEmpty_whenHeaderDoesNotStartWithBearer() {
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");
        Optional<String> token = jwtAuthenticationFilter.extractTokenFromHeader(request);
        assertTrue(token.isEmpty());
    }


    @Test
    void validateToken_shouldReturnEmpty_whenUsernameIsNull() {
        when(jwtTokenInspector.extractUsername("token")).thenReturn(null);
        when(jwtTokenInspector.extractUserType("token")).thenReturn("USER");

        Optional<UserDetails> result = jwtAuthenticationFilter.validateToken("token");

        assertTrue(result.isEmpty());
    }

    @Test
    void validateToken_shouldReturnEmpty_whenSecurityContextAlreadyHasAuth() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(mock(UsernamePasswordAuthenticationToken.class));

        try (MockedStatic<SecurityContextHolder> mockedHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            when(jwtTokenInspector.extractUsername("token")).thenReturn("admin");
            when(jwtTokenInspector.extractUserType("token")).thenReturn("ADMIN");

            Optional<UserDetails> result = jwtAuthenticationFilter.validateToken("token");

            assertTrue(result.isEmpty());
        }
    }

    @Test
    void validateToken_shouldReturnEmpty_whenTokenIsExpiredOrInvalid() {
        when(jwtTokenInspector.extractUsername("token")).thenReturn("user");
        when(jwtTokenInspector.extractUserType("token")).thenReturn("USER");
        when(jwtTokenInspector.isTokenValid("token")).thenReturn(false);
        Optional<UserDetails> result = jwtAuthenticationFilter.validateToken("token");
        assertTrue(result.isEmpty());
    }

    @Test
    void isPublicEndpoint_branches_testMethodNullAndMethodMismatch() {
        PublicEndpointsEnum mockEndpointNullMethod = mock(PublicEndpointsEnum.class);
        when(mockEndpointNullMethod.getPath()).thenReturn("/api/public/all-methods/**");
        when(mockEndpointNullMethod.getMethod()).thenReturn(null); // <--- Rama: endpoint.getMethod() == null
        PublicEndpointsEnum mockEndpointWithMethod = mock(PublicEndpointsEnum.class);
        when(mockEndpointWithMethod.getPath()).thenReturn("/api/public/only-post/**");
        when(mockEndpointWithMethod.getMethod()).thenReturn(HttpMethod.POST);

        try (MockedStatic<PublicEndpointsEnum> mockedEnum = Mockito.mockStatic(PublicEndpointsEnum.class)) {
            mockedEnum.when(PublicEndpointsEnum::values).thenReturn(new PublicEndpointsEnum[]{
                    mockEndpointNullMethod,
                    mockEndpointWithMethod
            });

            // Coincide el Path y el method es NULL
            when(request.getRequestURI()).thenReturn("/api/public/all-methods/test");
            when(request.getMethod()).thenReturn("PUT");

            boolean resultNullMethod = jwtAuthenticationFilter.isPublicEndpoint(request);
            assertTrue(resultNullMethod, "Debería ser true porque el método configurado en el enum es null");

            // Coincide el Path pero el method no coincide
            when(request.getRequestURI()).thenReturn("/api/public/only-post/test");
            when(request.getMethod()).thenReturn("GET");

            boolean resultMethodMismatch = jwtAuthenticationFilter.isPublicEndpoint(request);
            assertFalse(resultMethodMismatch, "Debería ser false porque GET no coincide con POST");

            // Coincide el Path y el method
            when(request.getRequestURI()).thenReturn("/api/public/only-post/test");
            when(request.getMethod()).thenReturn("POST");

            boolean resultMethodMatch = jwtAuthenticationFilter.isPublicEndpoint(request);
            assertTrue(resultMethodMatch, "Debería ser true porque POST coincide con POST");
        }
    }
}