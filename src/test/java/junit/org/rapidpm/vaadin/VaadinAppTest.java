package junit.org.rapidpm.vaadin;


import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchTestCase;
import junit.org.rapidpm.vaadin.extensions.PersistenceExtension;
import junit.org.rapidpm.vaadin.extensions.ServletContainerExtension;
import junit.org.rapidpm.vaadin.extensions.WebDriverParameterResolver;
import junit.org.rapidpm.vaadin.extensions.WebDriverParameterResolver.WebDriverInfo;
import junit.org.rapidpm.vaadin.extensions.WebdriverExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rapidpm.vaadin.LoginViewOO;
import org.rapidpm.vaadin.MainView;

@ExtendWith(PersistenceExtension.class)
@ExtendWith(ServletContainerExtension.class)
@ExtendWith(WebdriverExtension.class)
@ExtendWith(WebDriverParameterResolver.class)
public class VaadinAppTest {

  private final TestBenchTestCase testCase = new TestBenchTestCase() { };

  @Test
  void test001(WebDriverInfo webDriverInfo) {
    System.out.println("webDriverInfo = " + webDriverInfo.getVncAdress());

    testCase.setDriver(webDriverInfo.getWebdriver());

    testCase.getDriver()
            .get("http://" + webDriverInfo.getHostIpAddress() + ":7777/");

    testCase.$(TextFieldElement.class)
            .id(LoginViewOO.USERNAME)
            .setValue("admin");
    testCase.$(PasswordFieldElement.class)
            .id(LoginViewOO.PASSWORD)
            .setValue("admin");
    testCase.$(ButtonElement.class)
            .id(LoginViewOO.BTN_LOGIN)
            .click();

    final String text = testCase.$(SpanElement.class)
                                .id(MainView.SPAN_ID)
                                .getText();
    Assertions.assertEquals(MainView.TEXT, text);
  }
}
