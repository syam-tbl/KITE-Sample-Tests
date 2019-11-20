package org.webrtc.kite.owt.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebElement;
import org.webrtc.kite.owt.pages.OWTPage;

import java.util.List;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.ReportUtils.getStackTrace;
import static io.cosmosoftware.kite.util.TestUtils.videoCheck;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class AllVideoCheck extends TestStep {

   
  private final int numberOfParticipants;
  private final OWTPage owtPage;


  public AllVideoCheck(Runner runner, int numberOfParticipants, OWTPage owtPage) {
    super(runner);
    this.numberOfParticipants = numberOfParticipants;
    this.owtPage = owtPage;
  }

  @Override
  public String stepDescription() {
    return "Check the other videos are being received OK";
  }

  @Override
  protected void step() throws KiteTestException {
    try {
      //wait a while to allow all videos to load.;
      List<WebElement> videos = owtPage.getVideoElements();
      int waitingTime = 0;
      while(videos.size() < numberOfParticipants && waitingTime < 10 * numberOfParticipants) {
        logger.debug("numberOfParticipants = " + numberOfParticipants + ", waitingTime = " + waitingTime);
        waitAround(ONE_SECOND_INTERVAL);
        videos = owtPage.getVideoElements();
        waitingTime += 1;
      }
      if (videos.size() < numberOfParticipants) {
        logger.error("videos.size() < numberOfParticipants");
        throw new KiteTestException(
          "Unable to find " + numberOfParticipants + " <video> element on the page. No video found = "
            + videos.size(), Status.FAILED);
      }
      waitAround(numberOfParticipants * ONE_SECOND_INTERVAL);
      String videoCheck = "";
      boolean error = false;
      for (int i = 1; i < numberOfParticipants; i++) {
        String v = videoCheck(webDriver, i);
        videoCheck += v;
        if (i < numberOfParticipants - 1) {
          videoCheck += "|";
        }
        if (!"video".equalsIgnoreCase(v)) {
          error = true;
        }
      }
      if (error) {
        reporter.textAttachment(report, "Received Videos", videoCheck, "plain");
        throw new KiteTestException("Some videos are still or blank: " + videoCheck, Status.FAILED);
      } else {
        logger.info("Video checks: " + videoCheck);
      }
    } catch (KiteTestException e) {
      logger.error(getStackTrace(e));
      throw e;
    } catch (Exception e) {
      logger.error(getStackTrace(e));
      throw new KiteTestException("Error looking for the video", Status.BROKEN, e);
    }
  }
}
