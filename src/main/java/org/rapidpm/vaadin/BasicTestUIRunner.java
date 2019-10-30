/**
 * Copyright © 2017 Sven Ruppert (sven.ruppert@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rapidpm.vaadin;

import static org.rapidpm.frp.StringFunctions.notEmpty;
import static org.rapidpm.frp.StringFunctions.notStartsWith;
import static org.rapidpm.frp.Transformations.not;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.apache.meecrowave.Meecrowave;
import org.rapidpm.frp.Transformations;
import org.rapidpm.frp.functions.CheckedPredicate;
import org.rapidpm.frp.functions.CheckedSupplier;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class BasicTestUIRunner {
  private BasicTestUIRunner() {
  }

  public static DataSource datasource;
  public static Meecrowave meecrowave;

  public static void main(String[] args) {
    if (args.length != 3) throw new RuntimeException(" arguments needed...");
    start(args[0] , args[1] , args[2]);
  }

  public static void start(String jdbcURL ,
                           String username ,
                           String password) {

    datasource = createDataSource(jdbcURL ,
                                  username ,
                                  password);

    meecrowave = new Meecrowave(new Meecrowave.Builder() {
      {
//        randomHttpPort();
        setHttpPort(7777);
        setHost(localeIP().get());
        setTomcatScanning(true);
        setTomcatAutoSetup(true);
        setHttp2(true);
      }
    });
    meecrowave
        .bake();
  }

  public static void stop() {
    meecrowave.close();
  }

  private static DataSource createDataSource(String jdbcURL ,
                                             String username ,
                                             String password) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(jdbcURL);
    hikariConfig.setUsername(username);
    hikariConfig.setPassword(password);
    return new HikariDataSource(hikariConfig);
  }

  public static Supplier<String> localeIP() {
    return () -> {
      final CheckedSupplier<Enumeration<NetworkInterface>> checkedSupplier =
          NetworkInterface::getNetworkInterfaces;

      return Transformations.<NetworkInterface>enumToStream()
          .apply(checkedSupplier.getOrElse(Collections::emptyEnumeration))
          .filter((CheckedPredicate<NetworkInterface>) NetworkInterface::isUp)
          .map(NetworkInterface::getInetAddresses)
          .flatMap(iaEnum -> Transformations.<InetAddress>enumToStream().apply(iaEnum))
          .filter(inetAddress -> inetAddress instanceof Inet4Address)
          .filter(not(InetAddress::isMulticastAddress)).filter(not(InetAddress::isLoopbackAddress))
          .map(InetAddress::getHostAddress).filter(notEmpty())
          .filter(adr -> notStartsWith().apply(adr, "127"))
          .filter(adr -> notStartsWith().apply(adr, "169.254"))
          .filter(adr -> notStartsWith().apply(adr, "255.255.255.255"))
          .filter(adr -> notStartsWith().apply(adr, "255.255.255.255"))
          .filter(adr -> notStartsWith().apply(adr, "0.0.0.0"))
          // .filter(adr -> range(224, 240).noneMatch(nr -> adr.startsWith(valueOf(nr))))
          .findFirst().orElse("localhost");
    };
  }
}
