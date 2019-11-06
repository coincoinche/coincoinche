package com.coincoinche.config;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Programmatic configuration of the database connection. */
@Configuration
public class DataSourceConfig {

  private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

  /**
   * Programmatically configure a connection to the Heroku Postgres database.
   * This is only avaiable for the heroku application profile.
   * @return the datasource used for the connection.
   */
  @Bean
  @Profile("heroku")
  public DataSource postgresHerokuDataSource() {
    String databaseUrl = System.getenv("DATABASE_URL");
    logger.info("Initializing PostgreSQL database: {}", databaseUrl);

    URI dbUri;
    try {
      dbUri = new URI(databaseUrl);
    } catch (URISyntaxException e) {
      logger.error(String.format("Invalid DATABASE_URL: %s", databaseUrl), e);
      return null;
    }

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

    DataSource dataSource = new DataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl(dbUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setTestOnBorrow(true);
    dataSource.setTestWhileIdle(true);
    dataSource.setTestOnReturn(true);
    dataSource.setValidationQuery("SELECT 1");
    return dataSource;
  }
}
