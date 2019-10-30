package junit.org.rapidpm.vaadin.extensions;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static org.rapidpm.vaadin.BasicTestUIRunner.localeIP;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.WebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

public class WebDriverParameterResolver implements ParameterResolver {

  @Override
  public boolean supportsParameter(ParameterContext parameterContext ,
                                   ExtensionContext extensionContext)
      throws ParameterResolutionException {
    final Class<?> type = parameterContext.getParameter().getType();
    return WebDriverInfo.class.isAssignableFrom(type);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext ,
                                 ExtensionContext extensionContext)
      throws ParameterResolutionException {

    final BrowserWebDriverContainer container = extensionContext
        .getStore(GLOBAL)
        .get(BrowserWebDriverContainer.class.getSimpleName() , BrowserWebDriverContainer.class);


    final String property = System.getProperty("os.name");
    final boolean osx = property.contains("Mac OS X");

    return new WebDriverInfo(container.getWebDriver() ,
                             (osx) ? "host.docker.internal" : localeIP().get(),
                             container.getVncAddress());
  }

  public static class WebDriverInfo {
    private WebDriver webdriver;
    private String hostIpAddress;
    private String vncAdress;

    public WebDriverInfo(WebDriver webdriver , String hostIpAddress , String vncAdress) {
      this.webdriver = webdriver;
      this.hostIpAddress = hostIpAddress;
      this.vncAdress = vncAdress;
    }

    public WebDriver getWebdriver() {
      return webdriver;
    }

    public String getHostIpAddress() {
      return hostIpAddress;
    }

    public String getVncAdress() {
      return vncAdress;
    }
  }
}
