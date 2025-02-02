package org.js.swiftcodes.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.apache.ibatis.session.SqlSessionFactory;
import org.flywaydb.core.Flyway;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan("org.js.swiftcodes.service.dao.mapper")
public class DatabaseConfig {
    @Bean
    @Profile("prod")
    public DataSource prodDataSource(@Value("${datasource.url}") String url, @Value("${datasource.username}") String username, @Value("${datasource.password}") String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    @Profile("prod")
    public Flyway prodFlyway(DataSource prodDataSource) {
        Flyway flyway = Flyway.configure()
            .dataSource(prodDataSource)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource() throws IOException {
        EmbeddedPostgres embeddedPostgres = EmbeddedPostgres.builder()
            .setPort(5432) // You can specify another port if needed
            .start();

        final String user = "postgres";
        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(embeddedPostgres.getJdbcUrl(user, user))
            .username(user)
            .password(user)
            .build();
    }

    @Bean
    @Profile("test")
    public Flyway testFlyway(DataSource testDataSource) {
        Flyway flyway = Flyway.configure()
            .dataSource(testDataSource)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
