package butterknife.compiler;
interface ResourceBinding {
  Id id();
  boolean requiresResources(int sdk);
  CodeBlock render(int sdk);
}
