package de.zuellich.meal_planner.pinterest.services;
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {ContextConfigurationTest.class})
@ActiveProfiles("testing")
public class BoardServiceCachingTest {
  @Autowired private OAuth2RestOperations restTemplateToMock;
  @Autowired private IBoardService boardService;
  @Before
  public void setUp() {
    BoardList boardList = new BoardList();
    boardList.setBoards(Collections.emptyList());
    final ResponseEntity mockedResponse = mock(ResponseEntity.class);
    when(mockedResponse.getBody()).thenReturn(boardList);
    when(restTemplateToMock.getForEntity(eq(BoardService.USERS_BOARDS), any()))
        .thenReturn(mockedResponse);
  }
  @Test
  public void cachesRequestsForGetBoards() {
    List<Board> boards = boardService.getBoards();
    assertTrue(
        "Make sure the rest template first returns an empty list of boards", boards.isEmpty());
    boardService.getBoards();
    verify(restTemplateToMock).getForEntity(eq(BoardService.USERS_BOARDS), any());
    verifyNoMoreInteractions(restTemplateToMock);
  }
}
