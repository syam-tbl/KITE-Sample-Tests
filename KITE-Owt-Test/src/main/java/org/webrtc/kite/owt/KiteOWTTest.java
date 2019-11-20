package org.webrtc.kite.owt;

import io.cosmosoftware.kite.steps.*;
import org.webrtc.kite.owt.checks.AllVideoCheck;
import org.webrtc.kite.owt.checks.FirstVideoCheck;
import org.webrtc.kite.owt.pages.OWTPage;
import org.webrtc.kite.owt.steps.GetStatsStep;
import org.webrtc.kite.owt.steps.OpenUrlStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import javax.json.JsonObject;

public class KiteOWTTest extends KiteBaseTest {

  private JsonObject getChartsConfig = null;
  private boolean allureCharts = false;
  
  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    if (this.payload != null) {
      this.getChartsConfig = this.payload.getJsonObject("getCharts");
      this.allureCharts = this.getChartsConfig != null && this.getChartsConfig.getBoolean("enabled");
    }
  }

  @Override
  public void populateTestSteps(TestRunner runner) {
    final OWTPage owtPage = new OWTPage(runner);
    runner.addStep(new OpenUrlStep(runner, url));
    runner.addStep(new FirstVideoCheck(runner, owtPage));

    if (this.takeScreenshotForEachTest()) {
      runner.addStep(new ScreenshotStep(runner));
    }
    runner.addStep(new AllVideoCheck(runner, getMaxUsersPerRoom(), owtPage));
    if (this.getStats()) {
      runner.addStep(new GetStatsStep(runner, getStatsConfig, owtPage));
    }

    if (this.allureCharts) {
      runner.addStep(new StartGetStatsStep(runner, getChartsConfig));
    }

    if (this.takeScreenshotForEachTest()) {
      runner.addStep(new ScreenshotStep(runner));
    }
    if (this.meetingDuration > 0) {
      runner.addStep(new StayInMeetingStep(runner, meetingDuration));
    }

    if (this.allureCharts) {
      runner.addStep(new StopGetStatsStep(runner));
      runner.addStep(new GenerateChartsStep(runner, getChartsConfig, getTestJar()));
    }
    
    
  }

}
