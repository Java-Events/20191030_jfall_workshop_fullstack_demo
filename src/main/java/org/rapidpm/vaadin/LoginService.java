package org.rapidpm.vaadin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import javax.sql.DataSource;

public class LoginService {

  private DataSource dataSource;

  public LoginService(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public boolean checkLogin(String username , String password) {

    if (Objects.isNull(username)) return false;
    if (Objects.isNull(password)) return false;

    try {
      final int count = performQuery(dataSource ,
                                     "SELECT count(*) "
                                     + "FROM login "
                                     + "WHERE username='" + username + "' "
                                     + "AND password='" + password + "'")
          .getInt(1);
      return count == 1;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }


  private ResultSet performQuery(DataSource ds , String sql)
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
