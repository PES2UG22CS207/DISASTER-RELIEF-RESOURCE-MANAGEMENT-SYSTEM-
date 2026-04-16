package com.disaster.relief_system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSchemaInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSchemaInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseSchemaInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Ensure core auth tables exist so first-time registration does not fail on empty databases.
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id BIGINT NOT NULL AUTO_INCREMENT,
                    name VARCHAR(255),
                    email VARCHAR(255) NOT NULL,
                    password VARCHAR(255),
                    role VARCHAR(255),
                    PRIMARY KEY (user_id),
                    UNIQUE KEY uk_users_email (email)
                )
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS admins (
                    user_id BIGINT NOT NULL,
                    PRIMARY KEY (user_id)
                )
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS camp_managers (
                    user_id BIGINT NOT NULL,
                    camp_id BIGINT,
                    PRIMARY KEY (user_id)
                )
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS logistics_officers (
                    user_id BIGINT NOT NULL,
                    warehouse_id BIGINT,
                    PRIMARY KEY (user_id)
                )
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS warehouse_managers (
                    user_id BIGINT NOT NULL,
                    warehouse_id BIGINT,
                    PRIMARY KEY (user_id)
                )
                """);

        log.info("Core auth tables ensured (users/admin subtypes).");
    }
}
