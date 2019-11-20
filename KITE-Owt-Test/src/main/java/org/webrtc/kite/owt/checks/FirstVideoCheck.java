package org.webrtc.kite.owt.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebElement;
import org.webrtc.kite.owt.pages.OWTPage;

import java.util.List;

import static io.cosmosoftware.kite.util.ReportUtils.saveScreenshotPNG;
import static io.cosmosoftware.kite.util.ReportUtils.timestamp;
import static io.cosmosoftware.kite.util.TestUtils.videoCheck;


public class FirstVideoCheck extends TestStep {

  private final OWTPage owtPage;
  
  public FirstVideoCheck(Runner runner, OWTPage owtPage) {
    super(runner);
    this.owtPage = owtPage;
  }

  @Override
  public String stepDescription() {
    return "Check the first video is being sent OK";
  }

  @Override
  protected void step() throws KiteTestException {
    try {
      logger.info("Looking for video object");
      List<WebElement> videos = owtPage.getVideoElements();
      if (videos.isEmpty()) {
        throw new KiteTestException(
          "Unable to find any <video> element on the page", Status.FAILED);
      }

      String videoCheck = videoCheck(webDriver, 0);
      if (!"video".equalsIgnoreCase(videoCheck)) {
        reporter.screenshotAttachment(report,
          "FirstVideoCheck_" + timestamp(), saveScreenshotPNG(webDriver));
        reporter.textAttachment(report, "Sent Video", videoCheck, "plain");
        throw new KiteTestException("The first video is " + videoCheck, Status.FAILED);
      }
    } catch (KiteTestException e) {
      throw e;
    } catch (Exception e) {
      throw new KiteTestException("Error looking for the video", Status.BROKEN, e);
    }
  }
}
