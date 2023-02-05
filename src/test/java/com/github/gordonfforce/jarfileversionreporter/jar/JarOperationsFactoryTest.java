package com.github.gordonfforce.jarfileversionreporter.jar;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.gordonfforce.jarfileversionreporter.filefilter.FileFilterFactory;
import com.github.gordonfforce.jarfileversionreporter.filefilter.FileMetaDataFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class JarOperationsFactoryTest {

  private final JarOperationsFactory jarOpsFactory = JarOperationsFactory.getInstance();

  @Test
  void testGetInstance() {

    assertThat(jarOpsFactory).isNotNull();

  }

  @Test
  void testDefaultCreate() {

    final JarOperations jarOps = jarOpsFactory.create();

    assertThat(jarOps).isNotNull();

    assertThat(jarOpsFactory.create(FileFilterFactory.getInstance().createFileMetaDataFilter()))
        .isEqualTo(jarOps);

  }

  @Test
  void testCreateWithFileFilterFactory() {

    final JarOperations jarOps = jarOpsFactory.create();

    assertThat(jarOpsFactory.create(new FileMetaDataFilter() {
      @Override
      public Stream<Path> filter(Path base,
          BiPredicate<Path, BasicFileAttributes> filter) throws IOException {
        return FileMetaDataFilter.super.filter(base, filter);
      }

    })).isNotEqualTo(jarOps);

  }

}
