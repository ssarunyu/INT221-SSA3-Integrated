package int221.sit.taskboard;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}