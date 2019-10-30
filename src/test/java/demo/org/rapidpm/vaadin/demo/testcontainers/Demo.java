package demo.org.rapidpm.vaadin.demo.testcontainers;

import org.testcontainers.utility.TestEnvironment;

public class Demo {


  public static void main(String[] args) {
    final boolean b = TestEnvironment.dockerIsDockerMachine();
    System.out.println("b = " + b);
  }

}
