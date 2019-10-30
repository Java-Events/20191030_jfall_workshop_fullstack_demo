package junit.org.rapidpm.vaadin.extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.rapidpm.vaadin.BasicTestUIRunner;
import org.testcontainers.containers.JdbcDatabaseContainer;

public class ServletContainerExtension
    implements
    BeforeEachCallback,
    AfterEachCallback {

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    final JdbcDatabaseContainer container = extensionContext
        .getStore(ExtensionContext.Namespace.GLOBAL)
        .get(JdbcDatabaseContainer.class.getSimpleName() , JdbcDatabaseContainer.class);

    BasicTestUIRunner.start(
        container.getJdbcUrl() ,
        container.getUsername() ,
        container.getPassword()
    );
  }


  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {
    BasicTestUIRunner.stop();
  }
}
