package org.js.swiftcodes.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@Import(DatabaseConfig.class)
public class BuiltinDbConfig {
    private EmbeddedPostgres embeddedPostgres;

    @Bean
    @Profile("test")
    public DataSource testDataSource() throws IOException {
        this.embeddedPostgres = EmbeddedPostgres.builder()
            .setPort(5433) // You can specify another port if needed
            .start();

        final String user = "postgres";
        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(embeddedPostgres.getJdbcUrl(user, user))
            .username(user)
            .password(user)
            .build();
    }

    @PreDestroy
    public void stopEmbeddedPostgres() {
        if (embeddedPostgres != null) {
            try {
                embeddedPostgres.close();
            } catch (IOException e) {
                throw new RuntimeException("Failed to stop embedded PostgreSQL", e);
            }
        }
    }

}