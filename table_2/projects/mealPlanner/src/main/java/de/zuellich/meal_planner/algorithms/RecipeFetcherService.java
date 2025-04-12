package de.zuellich.meal_planner.algorithms;
@Service
public class RecipeFetcherService {
  public String fetchByURL(String url) throws IOException {
    return Jsoup.connect(url).get().html();
  }
}
