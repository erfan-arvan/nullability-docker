package com.google.cloud.tools.jib.cache;
public class CacheReader {
  private static FileTime getLastModifiedTime(Path path) throws IOException {
    FileTime lastModifiedTime = Files.getLastModifiedTime(path);
    if (Files.isReadable(path)) {
      try (Stream<Path> fileStream = Files.walk(path)) {
        Optional<FileTime> maxLastModifiedTime =
            fileStream
                .map(
                    subFilePath -> {
                      try {
                        return Files.getLastModifiedTime(subFilePath);
                      } catch (IOException ex) {
                        throw new UncheckedIOException(ex);
                      }
                    })
                .max(FileTime::compareTo);
        if (!maxLastModifiedTime.isPresent()) {
          throw new IllegalStateException(
              "Could not get last modified time for all files in directory '" + path + "'");
        }
        if (maxLastModifiedTime.get().compareTo(lastModifiedTime) > 0) {
          lastModifiedTime = maxLastModifiedTime.get();
        }
      } catch (UncheckedIOException ex) {
        throw ex.getCause();
      }
    }
    return lastModifiedTime;
  }
  private final Cache cache;
  public CacheReader(Cache cache) {
    this.cache = cache;
  }
  public CachedLayer getLayer(DescriptorDigest layerDigest) throws LayerPropertyNotFoundException {
    return cache.getMetadata().getLayers().get(layerDigest);
  }
  public Path getLayerFile(List<Path> sourceFiles) throws CacheMetadataCorruptedException {
    CacheMetadata cacheMetadata = cache.getMetadata();
    ImageLayers<CachedLayerWithMetadata> cachedLayers =
        cacheMetadata.filterLayers().bySourceFiles(sourceFiles).filter();
    FileTime newestLastModifiedTime = FileTime.from(Instant.MIN);
    Path newestLayerFile = null;
    for (CachedLayerWithMetadata cachedLayer : cachedLayers) {
      if (cachedLayer.getMetadata() == null) {
        throw new IllegalStateException("Layers with sourceFiles should have metadata");
      }
      FileTime cachedLayerLastModifiedTime = cachedLayer.getMetadata().getLastModifiedTime();
      if (cachedLayerLastModifiedTime.compareTo(newestLastModifiedTime) > 0) {
        newestLastModifiedTime = cachedLayerLastModifiedTime;
        newestLayerFile = cachedLayer.getContentFile();
      }
    }
    return newestLayerFile;
  }
  public CachedLayer getUpToDateLayerBySourceFiles(List<Path> sourceFiles)
      throws IOException, CacheMetadataCorruptedException {
    ImageLayers<CachedLayerWithMetadata> cachedLayersWithSourceFiles =
        cache.getMetadata().filterLayers().bySourceFiles(sourceFiles).filter();
    if (cachedLayersWithSourceFiles.isEmpty()) {
      return null;
    }
    FileTime sourceFilesLastModifiedTime = FileTime.from(Instant.MIN);
    for (Path path : sourceFiles) {
      FileTime lastModifiedTime = getLastModifiedTime(path);
      if (lastModifiedTime.compareTo(sourceFilesLastModifiedTime) > 0) {
        sourceFilesLastModifiedTime = lastModifiedTime;
      }
    }
    for (CachedLayerWithMetadata cachedLayer : cachedLayersWithSourceFiles) {
      if (cachedLayer.getMetadata() == null) {
        throw new IllegalStateException("Layers with sourceFiles should have metadata");
      }
      if (sourceFilesLastModifiedTime.compareTo(cachedLayer.getMetadata().getLastModifiedTime())
          <= 0) {
        return cachedLayer;
      }
    }
    return null;
  }
}
