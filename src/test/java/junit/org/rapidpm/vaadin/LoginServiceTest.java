package junit.org.rapidpm.vaadin;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rapidpm.vaadin.LoginService;
import junit.org.rapidpm.vaadin.extensions.DataSourceParameterResolver;
import junit.org.rapidpm.vaadin.extensions.PersistenceExtension;

@ExtendWith(PersistenceExtension.class)
@ExtendWith(DataSourceParameterResolver.class)
class LoginServiceTest {
  @Test
  void test001(DataSource dataSource) {
    final boolean checkLogin = new LoginService(dataSource)
        .checkLogin("admin" , "admin");
    Assertions.assertTrue(checkLogin);
  }

  @Test
  void test002(DataSource dataSource) {
    final boolean checkLogin = new LoginService(dataSource)
        .checkLogin("admin" , "XXXX");
    Assertions.assertFalse(checkLogin);
  }
}
