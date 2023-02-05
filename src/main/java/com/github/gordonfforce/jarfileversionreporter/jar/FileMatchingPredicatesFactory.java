package com.github.gordonfforce.jarfileversionreporter.jar;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface FileMatchingPredicatesFactory {

  default BiPredicate<Path, BasicFileAttributes> createJarFile() {
    return (curPath, attributes) ->
        attributes.isRegularFile() && curPath.toString().endsWith("jar");
  }

  default Predicate<Path> createPathContains(final Collection<String> partialNames) {

    return curPath -> partialNames.stream()
        .anyMatch(partialName -> curPath.toString().contains(partialName));

  }

  static FileMatchingPredicatesFactory getInstance() {
    return new FileMatchingPredicatesFactory() {
    };

  }

}
