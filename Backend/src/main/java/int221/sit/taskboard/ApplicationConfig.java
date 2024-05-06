package int221.sit.taskboard;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @Bean
//    public DateTimeFormatter dateTimeFormatter() {
//        return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/{id}").allowedOrigins("http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th");
            }
        };
    }
}