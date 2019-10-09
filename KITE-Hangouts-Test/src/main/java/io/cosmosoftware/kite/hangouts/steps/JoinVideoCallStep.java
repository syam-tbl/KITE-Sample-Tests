package io.cosmosoftware.kite.hangouts.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.hangouts.pages.MainPage;
import io.cosmosoftware.kite.manager.RoomManager;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.webrtc.kite.tests.TestRunner;

import static io.cosmosoftware.kite.entities.Timeouts.DEFAULT_TIMEOUT;
import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.entities.Timeouts.SHORT_TIMEOUT;
import static io.cosmosoftware.kite.entities.Timeouts.THREE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class JoinVideoCallStep extends TestStep {

  private final int id;
  private final RoomManager roomManager;
  private final MainPage mainPage;
  
  
  
  public JoinVideoCallStep(TestRunner runner, RoomManager roomManager, MainPage mainPage) {
    super(runner);
    this.id = runner.getId();
    logger.info( "TestRunner id = " + this.id + " userPerRoom = " + roomManager.getUsersPerRoom());
    this.roomManager = roomManager;
    this.mainPage = mainPage;
  }
  
  
  @Override
  public String stepDescription() {
    return "Joining the call";
  }
  
  @Override
  protected void step() throws KiteTestException {
    waitForRoomId ();
    String roomUrl = roomManager.getDynamicUrl(0);
    logger.info("Joining call at: " + roomUrl);
    mainPage.open(roomUrl);
    mainPage.clickJoin();
  }


  private void waitForRoomId () throws KiteTestException {
    for (int i = 0; i< DEFAULT_TIMEOUT; i += ONE_SECOND_INTERVAL) {
      if (roomManager.getDynamicUrl(this.id) == null) {
        waitAround(ONE_SECOND_INTERVAL);
      } else {
        return;
      }
    }
    throw new KiteTestException("Waited too long for room url", Status.FAILED);
  }
}
