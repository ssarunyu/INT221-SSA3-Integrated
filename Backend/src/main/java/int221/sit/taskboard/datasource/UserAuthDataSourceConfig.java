package int221.sit.taskboard.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class UserAuthDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.auth.datasource")
    public DataSourceProperties userAuthDataSourceProps() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource userAuthDataSource() {
        return userAuthDataSourceProps().initializeDataSourceBuilder().build();
    }
}
