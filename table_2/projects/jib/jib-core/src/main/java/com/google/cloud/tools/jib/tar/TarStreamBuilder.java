package com.google.cloud.tools.jib.tar;
public class TarStreamBuilder {
  private final List<TarArchiveEntry> entries = new ArrayList<>();
  private static void writeEntriesAsTarArchive(
      List<TarArchiveEntry> entries, OutputStream tarByteStream) throws IOException {
    try (TarArchiveOutputStream tarArchiveOutputStream =
        new TarArchiveOutputStream(tarByteStream)) {
      tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
      for (TarArchiveEntry entry : entries) {
        tarArchiveOutputStream.putArchiveEntry(entry);
        if (entry.isFile()) {
          InputStream contentStream = new BufferedInputStream(new FileInputStream(entry.getFile()));
          ByteStreams.copy(contentStream, tarArchiveOutputStream);
        }
        tarArchiveOutputStream.closeArchiveEntry();
      }
    }
  }
  public void addEntry(TarArchiveEntry entry) {
    entries.add(entry);
  }
  public Blob toBlob() {
    return Blobs.from(
        outputStream ->
            writeEntriesAsTarArchive(Collections.unmodifiableList(entries), outputStream));
  }
}
