package com.github.gordonfforce.jarfileversionreporter.filefilter;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public interface FileMetaDataFilter {

  default Stream<Path> filter(
      final Path base,
      final BiPredicate<Path, BasicFileAttributes> filter)
      throws IOException {

    return Files.find(base, 100, filter, FOLLOW_LINKS);

  }

}
