package com.github.gordonfforce.jarfileversionreporter.jar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FileMatchingPredicatesFactoryTest {

  private static final FileMatchingPredicatesFactory FACTORY =
      FileMatchingPredicatesFactory.getInstance();

  private static Path createMockPath(final String name) {

    final Path mockPath = mock(Path.class);
    when(mockPath.toString()).thenReturn(name);
    return mockPath;

  }

  private static BasicFileAttributes createRegularFile(final boolean isRegularFile) {

    final BasicFileAttributes mockAttributes = mock(BasicFileAttributes.class);

    if (isRegularFile) {

      when(mockAttributes.isRegularFile()).thenReturn(true);

    } else {

      when(mockAttributes.isRegularFile()).thenReturn(false);
    }

    return mockAttributes;

  }

  private static final BasicFileAttributes REGULAR_FILE = createRegularFile(true);

  private static Stream<Arguments> providePathsTrue() {

    return Stream.of(
        Arguments.of(createMockPath("./some.jar"), REGULAR_FILE),
        Arguments.of(createMockPath("any.jar"), REGULAR_FILE),
        Arguments.of(createMockPath("/opt/lib/someother.jar"), REGULAR_FILE));

  }

  @ParameterizedTest
  @MethodSource("providePathsTrue")
  @DisplayName("createJarFile() should return true for paths ending in .jar, and is a regular file")
  void createJarFileTrue(final Path path, final BasicFileAttributes attributes) {

    assertThat(FACTORY.createJarFile().test(path, attributes)).isTrue();

  }

  private static Stream<Arguments> providePathsFalse() {

    final BasicFileAttributes isNotRegularFile = createRegularFile(false);

    return Stream.of(
        Arguments.of(createMockPath("./some"), REGULAR_FILE),
        Arguments.of(createMockPath("/opt/lib/someother.jar"), isNotRegularFile),
        Arguments.of(createMockPath("blah"), isNotRegularFile));

  }

  @ParameterizedTest
  @MethodSource("providePathsFalse")
  @DisplayName("createJarFile() should return false for paths not ending in .jar or not a regular file")
  void createJarFileFalse(final Path path, final BasicFileAttributes attributes) {

    assertThat(FACTORY.createJarFile().test(path, attributes)).isFalse();

  }

  private static Stream<Arguments> providesPathsWithNamesTrue() {

    final Path path = createMockPath("some.jar");

    return Stream.of(
        Arguments.of(path, Set.of("some", "other")),
        Arguments.of(path, Set.of("blah", "other", "some", "om")));

  }

  @ParameterizedTest
  @MethodSource("providesPathsWithNamesTrue")
  @DisplayName("createPathContainsTrue() should return true for paths containing names")
  void createPathContainsTrue(final Path path, final Collection<String> names) {

    assertThat(FACTORY.createPathContains(names).test(path)).isTrue();

  }

  private static Stream<Arguments> providesPathsWithNamesFalse() {

    final Path path = createMockPath("emos.jar");

    return Stream.of(
        Arguments.of(path, Set.of("some"),
            Arguments.of(path, Set.of("blah", "other", "some", "om"))));

  }

  @ParameterizedTest
  @MethodSource("providesPathsWithNamesFalse")
  @DisplayName("createPathContainsFalse() should return false for paths not containing names")
  void createPathContainsFalse(final Path path, final Collection<String> names) {

    assertThat(FACTORY.createPathContains(names).test(path)).isFalse();

  }

}