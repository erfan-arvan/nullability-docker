package com.google.cloud.tools.jib.image;
public class ImageReference {
  private static final String DOCKER_HUB_REGISTRY = "registry.hub.docker.com";
  private static final String DEFAULT_TAG = "latest";
  private static final String LIBRARY_REPOSITORY_PREFIX = "library/";
  private static final String REGISTRY_COMPONENT_REGEX =
      "(?:[a-zA-Z\\d]|(?:[a-zA-Z\\d][a-zA-Z\\d-]*[a-zA-Z\\d]))";
  private static final String REGISTRY_REGEX =
      String.format("%s(?:\\.%s)*(?::\\d+)?", REGISTRY_COMPONENT_REGEX, REGISTRY_COMPONENT_REGEX);
  private static final String REPOSITORY_COMPONENT_REGEX =
      "[a-z\\d]+(?:(?:[_.]|__|[-]*)[a-z\\d]+)*";
  private static final String REPOSITORY_REGEX =
      String.format("(?:%s/)*%s", REPOSITORY_COMPONENT_REGEX, REPOSITORY_COMPONENT_REGEX);
  private static final String TAG_REGEX = "[\\w][\\w.-]{0,127}";
  private static final String REFERENCE_REGEX =
      String.format(
          "^(?:(%s)/)?(%s)(?:(?::(%s))|(?:@(%s)))?$",
          REGISTRY_REGEX, REPOSITORY_REGEX, TAG_REGEX, DescriptorDigest.DIGEST_REGEX);
  private static final Pattern REFERENCE_PATTERN = Pattern.compile(REFERENCE_REGEX);
  public static ImageReference parse(String reference) throws InvalidImageReferenceException {
    Matcher matcher = REFERENCE_PATTERN.matcher(reference);
    if (!matcher.find() || matcher.groupCount() < 4) {
      throw new InvalidImageReferenceException(reference);
    }
    String registry = matcher.group(1);
    String repository = matcher.group(2);
    String tag = matcher.group(3);
    String digest = matcher.group(4);
    if (Strings.isNullOrEmpty(registry)) {
      registry = DOCKER_HUB_REGISTRY;
    }
    if (Strings.isNullOrEmpty(repository)) {
      throw new InvalidImageReferenceException(reference);
    }
    if (!registry.contains(".") && !registry.contains(":") && !"localhost".equals(registry)) {
      repository = registry + "/" + repository;
      registry = DOCKER_HUB_REGISTRY;
    }
    if (DOCKER_HUB_REGISTRY.equals(registry) && repository.indexOf('/') < 0) {
      repository = LIBRARY_REPOSITORY_PREFIX + repository;
    }
    if (!Strings.isNullOrEmpty(tag)) {
      if (!Strings.isNullOrEmpty(digest)) {
        throw new InvalidImageReferenceException(reference);
      }
    } else if (!Strings.isNullOrEmpty(digest)) {
      tag = digest;
    } else {
      tag = DEFAULT_TAG;
    }
    return new ImageReference(registry, repository, tag);
  }
  public static ImageReference of(
       String registry, String repository,  String tag) {
    if (Strings.isNullOrEmpty(registry)) {
      registry = DOCKER_HUB_REGISTRY;
    }
    if (Strings.isNullOrEmpty(tag)) {
      tag = DEFAULT_TAG;
    }
    return new ImageReference(registry, repository, tag);
  }
  public static boolean isValidRegistry(String registry) {
    return registry.matches(REGISTRY_REGEX);
  }
  public static boolean isValidRepository(String repository) {
    return repository.matches(REPOSITORY_REGEX);
  }
  public static boolean isValidTag(String tag) {
    return tag.matches(TAG_REGEX);
  }
  private final String registry;
  private final String repository;
  private final String tag;
  private ImageReference(String registry, String repository, String tag) {
    this.registry = registry;
    this.repository = repository;
    this.tag = tag;
  }
  public String getRegistry() {
    return registry;
  }
  public String getRepository() {
    return repository;
  }
  public String getTag() {
    return tag;
  }
  @Override
  public String toString() {
    StringBuilder referenceString = new StringBuilder();
    if (!DOCKER_HUB_REGISTRY.equals(registry)) {
      referenceString.append(registry).append('/').append(repository);
    } else if (repository.startsWith(LIBRARY_REPOSITORY_PREFIX)) {
      referenceString.append(repository.substring(LIBRARY_REPOSITORY_PREFIX.length()));
    } else {
      referenceString.append(repository);
    }
    if (!DEFAULT_TAG.equals(tag)) {
      referenceString.append(':').append(tag);
    }
    return referenceString.toString();
  }
}
