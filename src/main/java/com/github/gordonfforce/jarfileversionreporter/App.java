package com.github.gordonfforce.jarfileversionreporter;

import com.github.gordonfforce.jarfileversionreporter.jar.JarOperations;
import com.github.gordonfforce.jarfileversionreporter.jar.JarOperationsFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  private static final Logger ERROR_LOGGER = LoggerFactory.getLogger(App.class);

  private static final JarOperations OPS = JarOperationsFactory.getInstance().create();

  public static void main(String[] args) {

    final Path basePath = Paths.get("/home/gordonff/.gradle/caches");

    try {

      OPS.findAll(basePath)
          .flatMap(jarPath -> OPS.filter(basePath, jarPath).stream())
          .forEach(System.out::println);

    } catch (java.io.IOException e) {

      ERROR_LOGGER.error("Could not generate jar file version report", e);

    }

  }

}