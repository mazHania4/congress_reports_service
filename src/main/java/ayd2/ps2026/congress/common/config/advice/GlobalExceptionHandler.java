package ayd2.ps2026.congress.common.config.advice;


import ayd2.ps2026.congress.common.exceptions.ConflictException;
import ayd2.ps2026.congress.common.exceptions.DuplicateResourceException;
import ayd2.ps2026.congress.common.exceptions.NotFoundException;
import ayd2.ps2026.congress.common.exceptions.jwt.InvalidTokenException;
import ayd2.ps2026.congress.common.models.dto.response.ErrorDTO;
import ayd2.ps2026.congress.common.utils.MethodArgumentErrorExtractor;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Clase para el manejo global de errores
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-27
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MethodArgumentErrorExtractor argumentErrorExtractor;

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleConstraintViolationException(ConstraintViolationException ex) {
        return new ErrorDTO("Error inesperado en la Base de Datos.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ErrorDTO(argumentErrorExtractor.extractMethodArgumentError(ex));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleBadCredentialsException(BadCredentialsException ex) {
        return new ErrorDTO("Autenticación fallida: El correo electrónico o la contraseña son incorrectos."
                + " Por favor, verifica tus credenciales e intenta de nuevo.");
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return new ErrorDTO("No tienes autorización para acceder a este recurso. "
                + "Verifica que tengas los permisos adecuados o contacta al equipo de soporte si consideras que esto es un error.");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(NotFoundException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleDuplicateResourceException(DuplicateResourceException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleIllegalStateException(IllegalStateException ex) {
        return new ErrorDTO(ex.getMessage());
    }


    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleInvalidTokenException(InvalidTokenException ex) {
        return new ErrorDTO(
                """
                        No se pudo validar tu sesión correctamente.
                        Te recomendamos cerrar sesión e intentar ingresar nuevamente.
                        Si el problema persiste, contacta al equipo de soporte
                """
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGlobalException(Exception ex) {
        ex.printStackTrace();
        return new ErrorDTO("Ocurrió un error inesperado: " + ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleConflictException(ConflictException ex) {
        return new ErrorDTO(ex.getMessage());
    }

}
