package com.nutritrack.NutriTrack.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configura as definições globais da documentação da API gerada pelo OpenAPI 3 (Swagger).
 * <p>
 * Esta classe centraliza as informações principais da API, como título, versão e descrição,
 * através da anotação {@code @OpenAPIDefinition}.
 * <p>
 * Além disso, define o esquema de segurança padrão para a autenticação via JWT Bearer Token
 * usando {@code @SecurityScheme}. Isso habilita a funcionalidade de autorização na interface
 * do Swagger UI, permitindo que os desenvolvedores insiram um token JWT para testar
 * os endpoints protegidos diretamente pela documentação. O {@code @SecurityRequirement}
 * aplica essa exigência de segurança globalmente.
 *
 * @see io.swagger.v3.oas.annotations.OpenAPIDefinition
 * @see io.swagger.v3.oas.annotations.security.SecurityScheme
 * @see io.swagger.v3.oas.annotations.security.SecurityRequirement
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(title = "NutriTrack API", version = "v1", description = "API para monitoramento nutricional."),
    security = @SecurityRequirement(name = "bearerAuth"),
    servers = {
        @Server(url = "https://nutritrack-production-cfa6.up.railway.app")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig { }

