package cl.tipum.blinblineo.ms_inventario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Blinblineo - Microservicio de Inventario")
                        .version("1.0")
                        .description("Documentación oficial para el control estricto de existencias y stock físico en bodega")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo TiPum SpA")
                                .email("cafetatipum@gmail.com")
                                .url("https://linktr.ee/cafetatipum")));
    }
}