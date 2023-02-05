package com.github.gordonfforce.jarfileversionreporter.filefilter;

public interface FileFilterFactory {

  default FileMetaDataFilter createFileMetaDataFilter() {

    return FileMetaDataFilter.getInstance();

  }

  static FileFilterFactory getInstance() {

    return new FileFilterFactory() {
    };

  }

}
