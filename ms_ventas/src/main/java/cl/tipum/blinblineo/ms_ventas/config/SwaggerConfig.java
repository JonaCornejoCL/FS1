package cl.tipum.blinblineo.ms_ventas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Blinblineo - Microservicio de Ventas")
                        .version("1.0")
                        .description("Documentación oficial para el motor transaccional de ventas"));
    }
}