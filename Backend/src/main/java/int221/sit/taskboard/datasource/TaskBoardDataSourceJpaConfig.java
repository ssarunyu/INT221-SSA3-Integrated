package int221.sit.taskboard.datasource;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "int221.sit.taskboard.repositories.task",
        entityManagerFactoryRef = "taskBoardEntityManagerFactory",
        transactionManagerRef = "taskBoardTransactionManager"
)
public class TaskBoardDataSourceJpaConfig {
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean taskBoardEntityManagerFactory(
            @Qualifier("taskBoardDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
                return builder.dataSource(dataSource)
                        .packages("int221.sit.taskboard.entities.itbkk_db")
                        .build();
    }

    @Bean
    public PlatformTransactionManager taskBoardTransactionManager(
            @Qualifier("taskBoardEntityManagerFactory") LocalContainerEntityManagerFactoryBean taskBoardEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(taskBoardEntityManagerFactory.getObject()));
    }
}
