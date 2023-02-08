package com.github.gordonfforce.jarfileversionreporter.jar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.gordonfforce.jarfileversionreporter.filefilter.FileMetaDataFilter;
import com.github.gordonfforce.jarfileversionreporter.jar.JarOperationsFactory.DefaultJarOperationsImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DefaultJarOperationsImplTest {

  private static final FileMatchingPredicatesFactory MATCHING_FACTORY =
      FileMatchingPredicatesFactory.getInstance();

  final FileMetaDataFilter fileFilter = mock(FileMetaDataFilter.class);

  final Path basePath = mock(Path.class);

  final JarOperations jarOps = new DefaultJarOperationsImpl(fileFilter);

  private static final Path A_JAR_FILE_PATH = Paths.get("./tmp/test.jar");

  @BeforeAll
  static void beforeAll() {

    Optional.of(A_JAR_FILE_PATH.getParent())
        .filter(path -> !Files.exists(path))
        .ifPresent(path -> {
          try {
            Files.createDirectories(path);
          } catch (IOException e) {
            throw new IllegalStateException("Could not create directory", e);
          }
        });

    Optional.of(A_JAR_FILE_PATH)
        .filter(path -> !Files.exists(path))
        .ifPresent(path -> {
          try {
            Files.createFile(path);
          } catch (IOException e) {
            throw new IllegalStateException("Could not create file", e);
          }
        });
  }

  @AfterAll
  static void afterAll() {

    Optional.of(A_JAR_FILE_PATH)
        .filter(Files::exists)
        .ifPresent(path -> {
          try {
            Files.delete(path);
          } catch (IOException e) {
            throw new IllegalStateException("Could not delete file", e);
          }
        });

    Optional.of(A_JAR_FILE_PATH.getParent())
        .filter(Files::exists)
        .ifPresent(path -> {
          try {
            Files.delete(path);
          } catch (IOException e) {
            throw new IllegalStateException("Could not delete directory", e);
          }
        });

  }

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

  private static Stream<Arguments> provideFilter() {

    return Stream.of(
        Arguments.of(A_JAR_FILE_PATH.getParent(), A_JAR_FILE_PATH, Collections.emptyList()));

  }

  @ParameterizedTest
  @MethodSource("provideFilter")
  void filter(
      final Path basePath,
      final Path jarFilePath,
      final Collection<String> expected)  {

    assertThat(jarOps.filter(basePath, jarFilePath)).isEqualTo(expected);


  }

}