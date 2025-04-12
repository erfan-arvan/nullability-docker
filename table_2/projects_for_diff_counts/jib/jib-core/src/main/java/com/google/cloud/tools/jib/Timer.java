package com.google.cloud.tools.jib;
public class Timer implements Closeable {
  private final BuildLogger buildLogger;
  private final int depth;
   private String label;
  private long startTime = System.nanoTime();
  public Timer(BuildLogger buildLogger, String label) {
    this(buildLogger, label, 0);
  }
  private Timer(BuildLogger buildLogger, String label, int depth) {
    this.buildLogger = buildLogger;
    this.label = label;
    this.depth = depth;
    if (buildLogger != null) {
      buildLogger.debug(getTabs().append("TIMING\t").append(label));
      if (depth == 0) {
        buildLogger.info("RUNNING\t" + label);
      }
    }
  }
  public Timer subTimer(String label) {
    return new Timer(buildLogger, label, depth + 1);
  }
  public void lap( String label) {
    if (this.label == null) {
      throw new IllegalStateException("Tried to lap Timer after closing");
    }
    if (buildLogger != null) {
      double timeInMillis = (System.nanoTime() - startTime) / 1000 / 1000.0;
      buildLogger.debug(
          getTabs()
              .append("TIMED\t")
              .append(this.label)
              .append(" : ")
              .append(timeInMillis)
              .append(" ms"));
      if (depth == 0) {
        buildLogger.info(this.label + " : " + timeInMillis + " ms");
      }
    }
    this.label = label;
    startTime = System.nanoTime();
  }
  private StringBuilder getTabs() {
    StringBuilder tabs = new StringBuilder();
    for (int i = 0; i < depth; i++) {
      tabs.append("\t");
    }
    return tabs;
  }
  @Override
  public void close() {
    lap(null);
  }
}
