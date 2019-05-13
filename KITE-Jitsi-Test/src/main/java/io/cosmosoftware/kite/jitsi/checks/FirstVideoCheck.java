package io.cosmosoftware.kite.jitsi.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.pages.MeetingPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.videoCheck;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class FirstVideoCheck extends TestStep {

  public FirstVideoCheck(WebDriver webDriver) {
    super(webDriver);
  }

  @Override
  public String stepDescription() {
    return "Check the first video is being sent OK";
  }

  @Override
  protected void step() throws KiteTestException {
    try {
      waitAround(ONE_SECOND_INTERVAL);
      final MeetingPage meetingPage = new MeetingPage(this.webDriver, logger);
      meetingPage.videoIsPublishing(10);
      logger.info("Looking for video object");
      List<WebElement> videos = meetingPage.getVideoElements();
      if (videos.isEmpty()) {
        throw new KiteTestException(
            "Unable to find any <video> element on the page", Status.FAILED);
      }

      String videoCheck = videoCheck(webDriver, 0);
      if (!"video".equalsIgnoreCase(videoCheck)) {
        Reporter.getInstance().textAttachment(report, "Sent Video", videoCheck, "plain");
        throw new KiteTestException("The first video is " + videoCheck, Status.FAILED);
      }
    } catch (KiteTestException e) {
      throw e;
    } catch (Exception e) {
      throw new KiteTestException("Error looking for the video", Status.BROKEN, e);
    }
  }
}