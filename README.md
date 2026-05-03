# congress_reports_service
Service for managing reports for a Congress managment platform. [AyD 2 - final project]

## Estructura del código

### Código de configuración / util
Las siguientes carpetas son para configuración
```
auth
├── jwt                # Configuración de JWT
└── users              # Gestión de usuarios

common
├── config
│   ├── advice         # Manejo global de excepciones
│   ├── env            # Configuración del archivo .env
│   ├── files          # Configuración para subida de archivos (cloud/local)
│   └── web            # Configuraciones web (CORS, MVC, etc.)
│
├── enums              # Enums globales del sistema
├── exceptions         # Excepciones globales
├── models             # Modelos compartidos
└── utils              # Utilidades (correo, archivos, helpers)

```

### Estructura general de la carpeta de ejemplo
La carpeta de ejemplo sigue una estructura definida para que el proyecto este ordenado.
Se recomienda copiarla y por cada entidad en la base de datos se cambie el nombre de `Model` por
el de la entidad que representa
```
example
├── controllers
│
├── dtos
│   ├── request
│   ├── response
│   └── microservice_name      # Opcional (recomendado para integraciones externas)
│       ├── request
│       └── response
│
├── enums                      # Opcional
├── mappers
├── models
├── repositories
│
└── services
    ├── ModelService.java      # Interfaz
    └── ModelServiceImpl.java  # Implementación
```

### Clase Auditor
Hay una clase especial llamada Auditor que contiene atributos comunes en la base de datos,
se recomienda que se incluyan en todas las entidades dichos atributos.

```java
private Integer id;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;
private LocalDateTime deletedAt;
private LocalDateTime deactivateAt;
```

## Autenticación

### Endpoints sin autenticación
Por defecto todos los endpoints validan de que exista el jwt. Si se necesita un endpoint público,
se tiene un archivo donde se deben especificar estos, dicho archivo es:

```
commom.enums.PublicEndpointsEnum
```

### Obtener username del jwt
Ya se tiene configurada la autenticacion, para saber el username del
usuario logueado usa el siguiente comando:

```java
SecurityContextHolder.getContext().getAuthentication().getName();
```

## Configuracion de archivo de entorno .env

Ejemplo de archivo de entorno.<br>
*Nota algunas propiedades pueden cambiar dependiendo de las registradas en el archivo
.propierties y de la configuracion en el codigo*

```
# =========================
# APP
# =========================
SERVER_PORT=8081

# =========================
# DATABASE 
# =========================
DB_HOST=localhost
DB_NAME=demo_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

# =========================
# FRONTEND / BACKEND
# =========================
FRONTEND_ORIGIN=http://localhost:3000
BACKEND_ORIGIN=http://localhost:8080

# =========================
# JWT
# =========================
JWT_SECRET=your_super_secret_key_here
JWT_REFRESH_SECRET=your_refresh_secret_key_here

# =========================
# EMAIL
# =========================
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=your_email_password_or_app_password

# =========================
# AWS S3
# =========================
BUCKET_NAME=your_bucket_name
AWS_REGION=us-east-1
AWS_ACCESS_KEY=your_access_key
AWS_SECRET=your_secret_key

# =========================
# MICROSERVICES
# =========================

# Aqui se puede agregar urls a otros microservicios
```

## Comunicacion REST con otros microservicios
### Configuración inicial
La clase
```
ayd2.ps2026.congress.common.config.web.RestClientConfig
```
Contiene la configuración de comunicación, aqui se puede especificar
los parámetros que el archivo `.properties` recibirá, por ejemplo:
```java
@Value("${app.microservice.url}")
private String MICROSERVICE_NAME;

@Bean
public RestClient getMicroserviceNameRestClient() {
    return RestClient.builder()
            .baseUrl(MICROSERVICE_NAME)
            .build();
}
```
en el archivo `.propierties` se debe agregar
```
app.microservice.url=${MICROSERVICE_URL}
```
y asi mismo en el archivo .env



### Ejemplo de uso
```java
private Optional<MicroserviceUserDTO> getUserFromRemote(String username) {
    Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
    if (details instanceof Map<?,?> map) {
        String jwt = (String) map.get("jwt");
        MicroserviceUserDTO microserviceUserDTO = restClientConf.userRestClient().get()
                .uri("/user/username/{username}", username)
                .header("Authorization", "Bearer " + jwt)
                .retrieve()
                .body(MicroserviceUserDTO.class);
        return Optional.ofNullable(microserviceUserDTO);
    }
    return Optional.empty();
}
 ```

## Inicializar el servidor
```
mvn spring-boot:rum
```

## Pruebas

|                          | Command                                              |
|--------------------------|------------------------------------------------------|
| Run all tests            | `mvn test`                                           |
| Clean and run all tests  | `mvn clean test` (removes old build files first)     |
| Run a specific class     | `mvn -Dtest=ClassName test`                          |
| Run a single method      | `mvn -Dtest=ClassName#methodName test`               |
| Run all tests in package | `mvn -Dtest="com.example.package.*" test`            |
| Run multiple classes     | `mvn -Dtest=Class1,Class2 test`                      |
| Skip tests               | `mvn install -DskipTests` (Compiles but doesn't run) |
| Skip compilation & tests | `mvn install -Dmaven.test.skip=true`                 |

