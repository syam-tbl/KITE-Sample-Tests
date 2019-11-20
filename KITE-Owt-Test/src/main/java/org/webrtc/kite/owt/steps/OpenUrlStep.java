package org.webrtc.kite.owt.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.steps.TestStep;
import org.webrtc.kite.owt.pages.OWTPage;

public class OpenUrlStep extends TestStep {
  
  private final String url;
  private final OWTPage mainPage;
  
  
  public OpenUrlStep(Runner runner, String url) {
    super(runner);
    this.url = url;
    this.mainPage = new OWTPage(runner);
  }
  
  
  @Override
  public String stepDescription() {
    return "Open " + url;
  }
  
  @Override
  protected void step() throws KiteTestException {
    mainPage.open(url);
  }
}
