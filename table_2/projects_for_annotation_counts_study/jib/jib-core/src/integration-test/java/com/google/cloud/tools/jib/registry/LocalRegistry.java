package com.google.cloud.tools.jib.registry;
public class LocalRegistry extends ExternalResource {
  private final int port;
  private final String containerName = "registry-" + UUID.randomUUID();
  public LocalRegistry(int port) {
    this.port = port;
  }
  @Override
  protected void before() throws Throwable {
    new Command(
            "docker",
            "run",
            "-d",
            "-p",
            port + ":5000",
            "--restart=always",
            "--name",
            containerName,
            "registry:2")
        .run();
    new Command("docker", "pull", "busybox").run();
    new Command("docker", "tag", "busybox", "localhost:" + port + "/busybox").run();
    new Command("docker", "push", "localhost:" + port + "/busybox").run();
  }
  @Override
  protected void after() {
    try {
      new Command("docker", "stop", containerName).run();
      new Command("docker", "rm", "-v", containerName).run();
    } catch (InterruptedException | IOException ex) {
      throw new RuntimeException("Could not stop local registry fully: " + containerName, ex);
    }
  }
  private void printLogs() throws IOException, InterruptedException {
    Process process = Runtime.getRuntime().exec("docker logs " + containerName);
    try (InputStreamReader inputStreamReader =
        new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)) {
      System.out.println(CharStreams.toString(inputStreamReader));
    }
    try (InputStreamReader inputStreamReader =
        new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8)) {
      System.err.println(CharStreams.toString(inputStreamReader));
    }
    process.waitFor();
  }
}
