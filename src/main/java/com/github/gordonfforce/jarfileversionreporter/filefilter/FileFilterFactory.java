package com.github.gordonfforce.jarfileversionreporter.filefilter;

public interface FileFilterFactory {

  default FileMetaDataFilter createFileMetaDataFilter() {

    return new FileMetaDataFilter() {
    };

  }

  static FileFilterFactory getInstance() {

    return new FileFilterFactory() {
    };

  }

}
