package gui.hud.Ticker;

/**
 * News object to be added to the news Ticker
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.30.15
 */
public class News
{
  private String news;
  /**
   * News Constructor
   */
  public News(String news )
  {
    /* Do nothing */
    this.news = news;
  }

  public String getNews()
  {
    return news;
  }
}
