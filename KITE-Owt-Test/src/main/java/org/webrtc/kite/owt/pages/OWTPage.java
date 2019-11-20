package org.webrtc.kite.owt.pages;

import io.cosmosoftware.kite.exception.KiteInteractionException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

import static io.cosmosoftware.kite.entities.Timeouts.SHORT_TIMEOUT_IN_SECONDS;
import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class OWTPage extends BasePage {



  @FindBy(tagName = "video")
  private List<WebElement> videos;

  @FindBy(xpath = "/html/body/div[1]")
  private WebElement localDiv;
  
  
  public OWTPage(Runner runner) {
    super(runner);
  }
  
  public void open(String url) throws KiteInteractionException {
    loadPage(webDriver, url, SHORT_TIMEOUT_IN_SECONDS);
    waitUntilVisibilityOf(localDiv, SHORT_TIMEOUT_IN_SECONDS);
    click(localDiv);
  }
  

  /** @return the list of video elements */
  public List<WebElement> getVideoElements() {
    return videos;
  }

  public List<String> getRemotePC() {
    List<String> peerConnectionsList = new ArrayList<String>();
    for (int i = 1; i < videos.size(); i++) {
      peerConnectionsList.add("window.remotePc[" + i + "]");
    }
    return peerConnectionsList;
  }
}
