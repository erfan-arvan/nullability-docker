package de.zuellich.meal_planner.controller;
@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = MealPlanner.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class PinterestExplorerTest {
  @LocalServerPort private int port;
  @Autowired private TestRestTemplate testRestTemplate;
  @Test
  public void returnsAListOfBoards() {}
}
