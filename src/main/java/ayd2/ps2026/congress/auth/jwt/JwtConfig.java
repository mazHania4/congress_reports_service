package ayd2.ps2026.congress.auth.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de parámetros relacionados con JWT.
 * 
 * Esta clase proporciona el acceso a la clave secreta usada para la
 * codificación y decodificación de tokens JWT, leyendo el valor desde
 * el archivo de propiedades de configuración.
 * 
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Getter
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.refresh-secret}")
    private String refreshSecretKey;

    @Value("${jwt.expires-in}")
    private String jwtExpiresIn;

    @Value("${jwt.refresh-expires-in}")
    private String jwtRefreshExpiresIn;

    /**
     * Devuelve la clave secreta como un arreglo de bytes, decodificándola desde
     * Base64.
     * 
     * @return un arreglo de bytes correspondiente a la clave secreta decodificada.
     */
     /*public byte[] getSecretBytes() {
        return Base64.getDecoder().decode(secretKey);
     }*/
    public byte[] getSecretBytes() {
        return secretKey.getBytes();
    }

    /*public byte[] getRefreshSecretBytes() {
        return Base64.getDecoder().decode(refreshSecretKey);
    }*/
    public byte[] getRefreshSecretBytes(){
        return jwtRefreshExpiresIn.getBytes();
    }

}
