package com.github.gordonfforce.jarfileversionreporter.jar;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

public interface JarOperations {

  Stream<Path> findAll(Path base) throws IOException;

  Stream<Path> findFileNameContaining(
      Path base,
      Collection<String> partialNames) throws IOException;

  Collection<String> filter(Path base, Path jarFile);

}
