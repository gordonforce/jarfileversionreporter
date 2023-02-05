package com.github.gordonfforce.jarfileversionreporter.filefilter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class FileMetaDataFilterTest {

  @Test
  void filter() throws IOException {

    assertThat(FileMetaDataFilter
        .getInstance()
        .filter(
            Paths.get("."), (
                path, attributes) ->
                "build.gradle".equals(path.getFileName().toString())
                    && attributes.isRegularFile()))
        .hasSize(1);

  }

  @Test
  void getInstance() {

    assertThat(FileMetaDataFilter.getInstance())
        .isNotNull()
        .isInstanceOf(FileMetaDataFilter.DefaultFileMetaDataFilter.class);

  }

}