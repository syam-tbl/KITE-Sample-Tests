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
import javax.json.JsonString;
import java.util.ArrayList;
import java.util.List;

public class KiteOWTTest extends KiteBaseTest {

  private JsonObject getStatsSdk = null;
  private JsonObject getChartsConfig = null;
  private boolean allureCharts = false;
  private String localPeerConnection;
  private final List<String> remotePeerConnections = new ArrayList<>();
  
  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    if (this.payload != null) {
      this.getStatsSdk = this.payload.getJsonObject("getStatsSdk");
      this.getChartsConfig = this.payload.getJsonObject("getCharts");
      if (getStatsConfig != null && getStatsConfig.getJsonArray("peerConnections") != null) {
        int index = 0;
        for (JsonString j:getStatsConfig.getJsonArray("peerConnections").getValuesAs(JsonString.class)) {
          if (index == 0) {
            localPeerConnection = getStatsConfig.getJsonArray("peerConnections").getString(0);
          } else {
            remotePeerConnections.add(j.getString());
          }
          index++;
        }
      }
      this.allureCharts = this.getChartsConfig != null && this.getChartsConfig.getBoolean("enabled");
    }
  }

  @Override
  public void populateTestSteps(TestRunner runner) {
    final OWTPage owtPage = new OWTPage(runner);
    runner.addStep(new OpenUrlStep(runner, url));
    runner.addStep(new FirstVideoCheck(runner, owtPage));
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
