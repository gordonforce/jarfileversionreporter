package com.github.gordonfforce.jarfileversionreporter.jar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.gordonfforce.jarfileversionreporter.filefilter.FileMetaDataFilter;
import com.github.gordonfforce.jarfileversionreporter.jar.JarOperationsFactory.DefaultJarOperationsImpl;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultJarOperationsImplTest {

  private static final FileMatchingPredicatesFactory MATCHING_FACTORY =
      FileMatchingPredicatesFactory.getInstance();

  final FileMetaDataFilter fileFilter = mock(FileMetaDataFilter.class);


  final Path basePath = mock(Path.class);

  final JarOperations jarOps = new DefaultJarOperationsImpl(fileFilter);

  @BeforeEach
  void setUp() throws Exception {

    final Path thisJarFilePath = mock(Path.class);

    final Path thatJarFilePath = mock(Path.class);

    final Path notJarFilePath = mock(Path.class);

    when(basePath.toString()).thenReturn(".");

    when(thisJarFilePath.toString()).thenReturn("/this.jar");

    when(thatJarFilePath.toString()).thenReturn("/that.jar");

    when(notJarFilePath.toString()).thenReturn("some-file.txt");

    when(fileFilter.filter(basePath, MATCHING_FACTORY.createJarFile()))
        .thenReturn(Stream.of(thisJarFilePath, thatJarFilePath))
        .thenReturn(Stream.of(thisJarFilePath, thatJarFilePath));

  }

  @Test
  void findAll() throws IOException {

    assertThat(jarOps.findAll(basePath)).hasSize(2);

  }

  @Test
  void findFileNameContaining() throws IOException {

    assertThat(jarOps.findFileNameContaining(basePath, Collections.singletonList("this")))
        .hasSize(1);

    assertThat(jarOps.findFileNameContaining(basePath, Collections.singletonList("that")))
        .hasSize(1);

  }

  @Test
  void filter() {
    // TODO
    assertThat(true).isTrue();
  }

}