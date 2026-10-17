# Carrito Service

Microservicio encargado de la sesión de compra: agregar juegos al carrito, listar por usuario, eliminar items y calcular el total.

A diferencia de Usuario, Juegos e Inventario, este microservicio **llama a otros microservicios por REST** antes de guardar nada:

1. Le pregunta a `usuario-service` si el usuario existe.
2. Le pregunta a `inventario-service` si hay stock suficiente.
3. Si ambas validaciones pasan, recién ahí guarda el item en su propia base de datos.
4. Para calcular el total, le pregunta a `juegos-service` el precio de cada juego.

Esto se hace con un `RestTemplate` marcado como `@LoadBalanced` (ver `config/RestTemplateConfig.java`), que usa Eureka para encontrar en qué puerto está corriendo cada microservicio, sin tenerlo que hardcodear.

## Requisitos previos y orden de ejecución

Para que Carrito funcione de verdad (no solo que compile) necesitas **todo el ecosistema arriba**, en este orden:

1. `infra/eureka-server` → `http://localhost:8761`
2. `infra/api-gateway` → `http://localhost:8080`
3. `usuario-service` → puerto 8081
4. `juegos-service` → puerto 8082
5. `inventario-service` → puerto 8083
6. `carrito-service` (este) → puerto 8084

Si Carrito arranca sin que Usuario o Inventario estén corriendo, sus llamadas van a fallar cuando intentes agregar un item (no al arrancar la app, sino al hacer la petición).

## 1. Clonar el repositorio

```bash
git clone <URL_DE_TU_REPOSITORIO>
cd carrito-service
```

## 2. Configurar la base de datos

Abre `src/main/resources/application.properties` y reemplaza `TU_PASSWORD_AQUI`.

## 3. Compilar y ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

## 4. Generar el .jar

```bash
mvn clean package
java -jar target/carrito-service-1.0.0.jar
```

## 5. Probar con Postman

Antes de probar Carrito, asegúrate de tener datos de prueba:
- Un usuario creado en `usuario-service` (por ejemplo id=1).
- Un juego creado en `juegos-service` (por ejemplo id=1).
- Un registro de inventario en `inventario-service` para ese juego, con stock > 0.

| Método | URL | Body (JSON) |
|---|---|---|
| POST | `http://localhost:8084/api/carrito` | `{"usuarioId":1,"juegoId":1,"cantidad":2}` |
| GET | `http://localhost:8084/api/carrito/usuario/1` | — |
| GET | `http://localhost:8084/api/carrito/usuario/1/total` | — |
| DELETE | `http://localhost:8084/api/carrito/1` | — |

Casos de error a mostrar en el video (para el indicador IE3, que pide evidenciar también errores):
- POST con un `usuarioId` que no existe → responde `404 Not Found`.
- POST con `cantidad` mayor al stock disponible → responde `409 Conflict`.
