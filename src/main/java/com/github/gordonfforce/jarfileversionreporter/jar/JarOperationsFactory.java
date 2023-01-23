package com.github.gordonfforce.jarfileversionreporter.jar;

import com.github.gordonfforce.jarfileversionreporter.filefilter.FileFilterFactory;
import com.github.gordonfforce.jarfileversionreporter.filefilter.FileMetaDataFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface JarOperationsFactory {

  default JarOperations create() {
    return new DefaultJarOperationsImpl();
  }

  class DefaultJarOperationsImpl implements JarOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJarOperationsImpl.class);

    private static final Predicate<String> POM_PROPERTIES =
        Pattern.compile("META-INF/maven/.+/pom\\.properties").asPredicate();

    private static final FileMetaDataFilter fileFilter =
        FileFilterFactory.getInstance().createFileMetaDataFilter();

    @Override
    public Stream<Path> findAll(Path base) throws IOException {

      return fileFilter.filter(
          base,
          (curPath, attributes) ->
              attributes.isRegularFile() && curPath.toString().endsWith("jar"));

    }

    @Override
    public Stream<Path> findFileNameContaining(Path base, Collection<String> partialNames)
        throws IOException {

      return findAll(base)
          .filter(path -> partialNames.stream().anyMatch(path.toString()::contains));

    }

    private static Collection<String> entryLines(final JarFile file, final ZipEntry entry) {

      Collection<String> lines;

      try (final BufferedReader reader =
          new BufferedReader(new InputStreamReader(file.getInputStream(entry)))) {

        lines = reader.lines().collect(Collectors.toList());

      } catch (IOException e) {

        LOGGER.error(
            "Can not read the lines from zip file entry file:{}, entry:{}",
            file,
            entry,
            e);

        lines = Collections.emptyList();

      }

      return lines;

    }

    @Override
    public Collection<String> filter(Path base, Path jarPath) {

      final Collection<String> results = new ArrayList<>();

      try (final JarFile jarFile = new JarFile(jarPath.toFile())) {

        jarFile.entries()
            .asIterator()
            .forEachRemaining(entry -> Optional.of(entry)
                .filter(e -> POM_PROPERTIES.test(e.getName()))
                .map(e -> {

                  final String basePathName = base.toString();

                  final String jarFileName = jarFile.getName();

                  final String relativeJarFileName = jarFileName.substring(
                      jarFileName.indexOf(basePathName) + basePathName.length() + 1);

                  return String.join(" ",
                      relativeJarFileName,
                      entryLines(jarFile, entry)
                          .stream()
                          .filter(line -> line.contains("version"))
                          .findAny()
                          .orElse("No version specified"));

                })
                .ifPresent(results::add));

      } catch (IOException e) {

        LOGGER.error("Could not access jar file {}", jarPath, e);

      }

      return results;

    }

  }

  static JarOperationsFactory getInstance() {
    return new JarOperationsFactory() {
    };
  }

}
