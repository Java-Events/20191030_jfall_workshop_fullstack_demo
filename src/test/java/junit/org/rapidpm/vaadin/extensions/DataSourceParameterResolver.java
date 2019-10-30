package junit.org.rapidpm.vaadin.extensions;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

import javax.sql.DataSource;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.testcontainers.containers.JdbcDatabaseContainer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceParameterResolver implements ParameterResolver {


  @Override
  public boolean supportsParameter(ParameterContext parameterContext ,
                                   ExtensionContext extensionContext)
      throws ParameterResolutionException {
    final Class<?> type = parameterContext.getParameter().getType();
    return DataSource.class.isAssignableFrom(type);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext ,
                                 ExtensionContext extensionContext)
      throws ParameterResolutionException {


    final JdbcDatabaseContainer c = extensionContext
        .getStore(GLOBAL)
        .get(JdbcDatabaseContainer.class.getSimpleName() , JdbcDatabaseContainer.class);

    return createDataSource(c.getJdbcUrl() , c.getUsername() , c.getPassword());
  }

  private static DataSource createDataSource(String jdbcURL , String username , String password) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(jdbcURL);
    hikariConfig.setUsername(username);
    hikariConfig.setPassword(password);
    return new HikariDataSource(hikariConfig);
  }
}
