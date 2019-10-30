package demo.org.rapidpm.vaadin.demo.testcontainers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Testcontainers02 {

  public static void main(String[] args) {
    try (PostgreSQLContainer container = new PostgreSQLContainer().withDatabaseName("foo")
                                                                  .withUsername("foo")
                                                                  .withPassword("secret")) {
      container
          .start();

      final String jdbcUrl = container.getJdbcUrl();
      System.out.println("jdbcUrl = " + jdbcUrl);


      final DataSource dataSource = createDataSource(container);
      ResultSet resultSet = performQuery(dataSource ,
                                         container.getTestQueryString());

      int resultSetInt = resultSet.getInt(1);
      System.out.println("resultSetInt = " + resultSetInt);


      // Create the Flyway instance and point it to the database
      Flyway flyway = Flyway.configure()
                            .dataSource(container.getJdbcUrl(),
                                        container.getUsername(),
                                        container.getPassword())
                            .load();

      // Start the migration
      flyway.migrate();

      final String email = performQuery(dataSource ,
                                        "SELECT email "
                                        + "FROM login "
                                        + "WHERE username='admin'")
          .getString("email");
      System.out.println("email = " + email);



      // ... use the container
      // no need to call stop() afterwards

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  private static DataSource createDataSource(JdbcDatabaseContainer container) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(container.getJdbcUrl());
    hikariConfig.setUsername(container.getUsername());
    hikariConfig.setPassword(container.getPassword());
    return new HikariDataSource(hikariConfig);
  }

  private static ResultSet performQuery(DataSource ds , String sql)
      throws SQLException {
    Statement statement = ds
        .getConnection()
        .createStatement();
    statement.execute(sql);
    ResultSet resultSet = statement.getResultSet();
    resultSet.next();
    return resultSet;
  }

}
