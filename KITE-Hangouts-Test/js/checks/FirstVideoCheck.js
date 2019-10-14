const {TestStep, KiteTestError, Status} = require('kite-common');
const TEN_SECONDS = 10000;

/**
 * Class: FirstVideoCheck
 * Extends: TestStep
 * Description:
 */
class FirstVideoCheck extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.numberOfParticipant = kiteBaseTest.numberOfParticipant;
    this.timeout = kiteBaseTest.timeout;
    this.page = kiteBaseTest.page;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return "Check the first video is being sent OK";
  }

  async step() {
    let indexArray = await this.page.getVideoIndex();
    if (indexArray.length === 0) {
      throw new KiteTestError(Status.FAILED, "There is no valid video on page");
    }
    console.log("indexArray-> " + indexArray);
    let index;
    let error = false;
    let result = await this.page.videoCheck(indexArray[0], TEN_SECONDS);
    if (result !== 'video') {
      this.testReporter.textAttachment(stepInfo.report, "Received videos", result, "plain");
      throw new KiteTestError(Status.FAILED, "Some videos are still or blank: " + result);
    }



  }
}

module.exports = FirstVideoCheck;