package com.google.cloud.tools.jib.builder;
public class BuildConfiguration {
  public static class Builder {
     private ImageReference baseImageReference;
     private ImageReference targetImageReference;
    private List<String> credentialHelperNames = new ArrayList<>();
    private RegistryCredentials knownRegistryCredentials = RegistryCredentials.none();
    private boolean enableReproducibleBuilds = true;
     private String mainClass;
    private List<String> jvmFlags = new ArrayList<>();
    private Map<String, String> environmentMap = new HashMap<>();
    private Class<? extends BuildableManifestTemplate> targetFormat = V22ManifestTemplate.class;
    private BuildLogger buildLogger;
    private Builder(BuildLogger buildLogger) {
      this.buildLogger = buildLogger;
    }
    public Builder setBaseImage( ImageReference imageReference) {
      baseImageReference = imageReference;
      return this;
    }
    public Builder setTargetImage( ImageReference imageReference) {
      targetImageReference = imageReference;
      return this;
    }
    public Builder setCredentialHelperNames( List<String> credentialHelperNames) {
      if (credentialHelperNames != null) {
        this.credentialHelperNames = credentialHelperNames;
      }
      return this;
    }
    public Builder setKnownRegistryCredentials(
         RegistryCredentials knownRegistryCredentials) {
      if (knownRegistryCredentials != null) {
        this.knownRegistryCredentials = knownRegistryCredentials;
      }
      return this;
    }
    public Builder setEnableReproducibleBuilds(boolean isEnabled) {
      enableReproducibleBuilds = isEnabled;
      return this;
    }
    public Builder setMainClass( String mainClass) {
      this.mainClass = mainClass;
      return this;
    }
    public Builder setJvmFlags( List<String> jvmFlags) {
      if (jvmFlags != null) {
        this.jvmFlags = jvmFlags;
      }
      return this;
    }
    public Builder setEnvironment( Map<String, String> environmentMap) {
      if (environmentMap != null) {
        this.environmentMap = environmentMap;
      }
      return this;
    }
    public Builder setTargetFormat(Class<? extends BuildableManifestTemplate> targetFormat) {
      this.targetFormat = targetFormat;
      return this;
    }
    public BuildConfiguration build() {
      List<String> errorMessages = new ArrayList<>();
      if (baseImageReference == null) {
        errorMessages.add("base image is required but not set");
      }
      if (targetImageReference == null) {
        errorMessages.add("target image is required but not set");
      }
      if (mainClass == null) {
        errorMessages.add("main class is required but not set");
      }
      switch (errorMessages.size()) {
        case 0: 
          if (baseImageReference == null || targetImageReference == null || mainClass == null) {
            throw new IllegalStateException("Required fields should not be null");
          }
          return new BuildConfiguration(
              buildLogger,
              baseImageReference,
              targetImageReference,
              credentialHelperNames,
              knownRegistryCredentials,
              enableReproducibleBuilds,
              mainClass,
              jvmFlags,
              environmentMap,
              targetFormat);
        case 1:
          throw new IllegalStateException(errorMessages.get(0));
        case 2:
          throw new IllegalStateException(errorMessages.get(0) + " and " + errorMessages.get(1));
        default:
          StringBuilder errorMessage = new StringBuilder(errorMessages.get(0));
          for (int errorMessageIndex = 1;
              errorMessageIndex < errorMessages.size();
              errorMessageIndex++) {
            if (errorMessageIndex == errorMessages.size() - 1) {
              errorMessage.append(", and ");
            } else {
              errorMessage.append(", ");
            }
            errorMessage.append(errorMessages.get(errorMessageIndex));
          }
          throw new IllegalStateException(errorMessage.toString());
      }
    }
  }
  private final BuildLogger buildLogger;
  private ImageReference baseImageReference;
  private ImageReference targetImageReference;
  private List<String> credentialHelperNames;
  private RegistryCredentials knownRegistryCredentials;
  private boolean enableReproducibleBuilds;
  private String mainClass;
  private List<String> jvmFlags;
  private Map<String, String> environmentMap;
  private Class<? extends BuildableManifestTemplate> targetFormat;
  public static Builder builder(BuildLogger buildLogger) {
    return new Builder(buildLogger);
  }
  private BuildConfiguration(
      BuildLogger buildLogger,
      ImageReference baseImageReference,
      ImageReference targetImageReference,
      List<String> credentialHelperNames,
      RegistryCredentials knownRegistryCredentials,
      boolean enableReproducibleBuilds,
      String mainClass,
      List<String> jvmFlags,
      Map<String, String> environmentMap,
      Class<? extends BuildableManifestTemplate> targetFormat) {
    this.buildLogger = buildLogger;
    this.baseImageReference = baseImageReference;
    this.targetImageReference = targetImageReference;
    this.credentialHelperNames = Collections.unmodifiableList(credentialHelperNames);
    this.knownRegistryCredentials = knownRegistryCredentials;
    this.enableReproducibleBuilds = enableReproducibleBuilds;
    this.mainClass = mainClass;
    this.jvmFlags = Collections.unmodifiableList(jvmFlags);
    this.environmentMap = Collections.unmodifiableMap(environmentMap);
    this.targetFormat = targetFormat;
  }
  public BuildLogger getBuildLogger() {
    return buildLogger;
  }
  public String getBaseImageRegistry() {
    return baseImageReference.getRegistry();
  }
  public String getBaseImageRepository() {
    return baseImageReference.getRepository();
  }
  public String getBaseImageTag() {
    return baseImageReference.getTag();
  }
  public String getTargetRegistry() {
    return targetImageReference.getRegistry();
  }
  public String getTargetRepository() {
    return targetImageReference.getRepository();
  }
  public String getTargetTag() {
    return targetImageReference.getTag();
  }
  public RegistryCredentials getKnownRegistryCredentials() {
    return knownRegistryCredentials;
  }
  public List<String> getCredentialHelperNames() {
    return credentialHelperNames;
  }
  public boolean getEnableReproducibleBuilds() {
    return enableReproducibleBuilds;
  }
  public String getMainClass() {
    return mainClass;
  }
  public List<String> getJvmFlags() {
    return jvmFlags;
  }
  public Map<String, String> getEnvironment() {
    return environmentMap;
  }
  public Class<? extends BuildableManifestTemplate> getTargetFormat() {
    return targetFormat;
  }
}
