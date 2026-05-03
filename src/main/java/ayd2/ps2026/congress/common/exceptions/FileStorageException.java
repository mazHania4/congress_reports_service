package ayd2.ps2026.congress.common.exceptions;

/**
 * Excepción lanzada cuando ocurre un error irrecuperable durante operaciones de
 * almacenamiento de archivos,
 * como copiar, eliminar o acceder a archivos del sistema.
 * 
 * Engloba errores como conflictos de nombres, permisos insuficientes o fallos
 * del sistema de archivos.
 * 
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-02
 */
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

}
