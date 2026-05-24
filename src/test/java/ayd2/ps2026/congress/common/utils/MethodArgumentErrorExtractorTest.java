package ayd2.ps2026.congress.common.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class MethodArgumentErrorExtractorTest {

    private MethodArgumentErrorExtractor errorExtractor;

    @BeforeEach
    void setUp() {
        errorExtractor = new MethodArgumentErrorExtractor();
    }

    @Test
    void extractMethodArgumentError_WithMultipleErrors_ShouldReturnFormattedString() throws Exception {
        // Arrange: Crear un BindingResult con múltiples errores de campo
        Object targetBean = new Object();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(targetBean, "targetObject");

        bindingResult.addError(new FieldError("targetObject", "email", "El correo electrónico no es válido"));
        bindingResult.addError(new FieldError("targetObject", "password", "La contraseña es demasiado corta"));

        // Mockear el MethodParameter requerido por el constructor de MethodArgumentNotValidException
        Method method = this.getClass().getDeclaredMethod("dummyMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        // Act
        String result = errorExtractor.extractMethodArgumentError(exception);

        // Assert
        String expectedMessage = "- El correo electrónico no es válido<br>\n- La contraseña es demasiado corta<br>";
        // Nota: El método usa .trim() al final, lo que remueve espacios/saltos al inicio y final del String global
        assertThat(result)
                .contains("- El correo electrónico no es válido<br>")
                .contains("- La contraseña es demasiado corta<br>")
                .isEqualTo("- El correo electrónico no es válido<br>- La contraseña es demasiado corta<br>");
    }

    @Test
    void extractMethodArgumentError_WithNoErrors_ShouldReturnEmptyString() throws Exception {
        // Arrange: Un BindingResult vacío sin ningún error
        Object targetBean = new Object();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(targetBean, "targetObject");

        Method method = this.getClass().getDeclaredMethod("dummyMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        // Act
        String result = errorExtractor.extractMethodArgumentError(exception);

        // Assert
        assertThat(result).isEmpty();
    }

    private void dummyMethod(String param) {}
}