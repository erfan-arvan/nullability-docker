package com.google.cloud.tools.jib;
public class Command {
  private final List<String> command;
  public Command(String... command) {
    this.command = Arrays.asList(command);
  }
  public String run() throws IOException, InterruptedException {
    return run(null);
  }
  public String run( byte[] stdin) throws IOException, InterruptedException {
    Process process = new ProcessBuilder(command).start();
    if (stdin != null) {
      try (OutputStream outputStream = process.getOutputStream()) {
        outputStream.write(stdin);
      }
    }
    try (InputStreamReader inputStreamReader =
        new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)) {
      String output = CharStreams.toString(inputStreamReader);
      if (process.waitFor() != 0) {
        throw new RuntimeException("Command '" + String.join(" ", command) + "' failed");
      }
      return output;
    }
  }
}
