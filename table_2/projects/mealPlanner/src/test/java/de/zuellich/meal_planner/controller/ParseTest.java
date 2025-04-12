package de.zuellich.meal_planner.controller;
@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = MealPlanner.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ParseTest {
  @LocalServerPort private int port;
  @Autowired private TestRestTemplate testRestTemplate;
  @Before
  public void disableSSLCertificateValidation()
      throws KeyManagementException, NoSuchAlgorithmException {
    SSLUtil.turnOffSslChecking();
  }
  @Test
  public void acceptsAURLParameter() {
    String url = getURL("/parse");
    ResponseEntity<Map> entity =
        testRestTemplate.withBasicAuth("test", "test").getForEntity(url, Map.class);
    then(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    url = getURL("/parse?url=http%3A%2F%2Fexample.com");
    entity = testRestTemplate.getForEntity(url, Map.class);
    then(entity.getStatusCode().is2xxSuccessful()).isTrue();
  }
  @Test
  public void respondsWithErrorWhenNotAValidURL() {
    String url = getURL("/parse?url=test");
    ResponseEntity<Map> entity =
        testRestTemplate.withBasicAuth("test", "test").getForEntity(url, Map.class);
    then(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
  private String getURL(String append) {
    return "https:
  }
}
