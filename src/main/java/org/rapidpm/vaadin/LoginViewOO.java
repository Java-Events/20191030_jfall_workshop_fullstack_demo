package org.rapidpm.vaadin;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.rapidpm.dependencies.core.logger.HasLogger;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.START;
import static org.rapidpm.vaadin.LoginViewOO.NAV_LOGIN_VIEW;

@Route(NAV_LOGIN_VIEW)
public class LoginViewOO
    extends Composite<HorizontalLayout>
    implements HasLogger {

  public static final String NAV_LOGIN_VIEW = "login";
  public static final String USERNAME       = "username";
  public static final String PASSWORD       = "password";
  public static final String BTN_LOGIN      = "btnLogin";
  public static final String BTN_CANCEL     = "btnCancel";

  private final TextField     username  = new TextField();
  private final PasswordField password  = new PasswordField();
  private final Button        btnLogin  = new Button();
  private final Button        btnCancel = new Button();

  public LoginViewOO() {
    VerticalLayout groupV = new VerticalLayout(new HorizontalLayout(username, password),
                                               new HorizontalLayout(btnLogin, btnCancel));

    groupV.setDefaultHorizontalComponentAlignment(START);
    groupV.setSizeUndefined();

    username.setPlaceholder("username");
    username.setId(USERNAME);
    password.setPlaceholder("password");
    password.setId(PASSWORD);

    btnLogin.setText("Login");
    btnLogin.setId(BTN_LOGIN);
    btnLogin.addClickListener(e -> {
      boolean ok = new LoginService(BasicTestUIRunner.datasource)
          .checkLogin(username.getValue(), password.getValue());
      username.setValue("");
      password.setValue("");
      UI.getCurrent()
        .navigate((ok)
                  ? MainView.class
                  : LoginViewOO.class);
    });

    btnCancel.setText("Cancel");
    btnCancel.setId(BTN_CANCEL);

    HorizontalLayout content = getContent();
    content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    content.setSizeFull();
    content.add(groupV);
  }

}