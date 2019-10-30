package junit.org.rapidpm.vaadin.extensions;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class PersistenceExtension
    implements
    BeforeEachCallback,
    AfterEachCallback {


  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {

    PostgreSQLContainer container = new PostgreSQLContainer().withDatabaseName("foo")
                                                             .withUsername("foo")
                                                             .withPassword("secret");
    container.start();
    // Create the Flyway instance and point it to the database
    Flyway flyway = Flyway.configure()
                          .dataSource(container.getJdbcUrl(),
                                      container.getUsername(),
                                      container.getPassword())
                          .load();

    // Start the migration
    flyway.migrate();

    extensionContext
        .getStore(ExtensionContext.Namespace.GLOBAL)
        .put(JdbcDatabaseContainer.class.getSimpleName() , container);

  }

  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {

    extensionContext
        .getStore(ExtensionContext.Namespace.GLOBAL)
        .get(JdbcDatabaseContainer.class.getSimpleName() , JdbcDatabaseContainer.class)
        .stop();
  }
}
