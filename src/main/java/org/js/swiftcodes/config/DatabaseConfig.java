package org.js.swiftcodes.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.flywaydb.core.Flyway;
import org.js.swiftcodes.service.dao.BanksDataStore;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

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
    public Flyway prodFlyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
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

    @Bean
    public BanksDataStore createBanksDataStore(BankDataMapper bankDataMapper) {
        return new BanksDataStore(bankDataMapper);
    }
}
