package com.github.gordonfforce.jarfileversionreporter.jar;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JarOperationsFactoryTest {

  private final JarOperationsFactory jarOps = JarOperationsFactory.getInstance();

  @Test
  void testCreate() {

    assertThat(jarOps.create()).isNotNull();

  }

}
