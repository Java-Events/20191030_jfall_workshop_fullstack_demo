package demo.org.rapidpm.vaadin.demo.testcontainers;

import org.testcontainers.containers.GenericContainer;

public class Testcontainers01 {

  public static void main(String[] args) {
    try (GenericContainer container = new GenericContainer("postgres")) {
      container
          .withExposedPorts(5432)
          .start();
      // ... use the container
      // no need to call stop() afterwards

    }
  }

}
