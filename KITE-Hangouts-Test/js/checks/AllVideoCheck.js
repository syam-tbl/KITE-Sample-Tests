const {TestStep, Status, KiteTestError} = require('kite-common');
const TEN_SECONDS = 10000;

class AllVideoCheck extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.numberOfParticipant = kiteBaseTest.numberOfParticipant;
    this.timeout = kiteBaseTest.timeout;
    this.page = kiteBaseTest.page;
    this.usersPerRoom = kiteBaseTest.payload.usersPerRoom;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return "Check the other videos are being received OK";
  }

  async step() {
    let indexArray = await this.page.getVideoIndex();
    if (indexArray.length === 0) {
      throw new KiteTestError(Status.FAILED, "There is no valid video on page");
    }
    if (indexArray.length < this.numberOfParticipant) {
      throw new KiteTestError(Status.FAILED, "There are not enough video to match the number of participants");

    }
    console.log("indexArray-> " + indexArray);
    let index;
    let result = "";
    let error = false;
    for (index = 0; index < indexArray.length; index ++) {
      let tmp = await this.page.videoCheck(indexArray[index], TEN_SECONDS);
      console.log('-> Received video[' + index + '] ' + tmp);
      result += tmp;
      if (index < indexArray.length - 1) {
        result += ' | ';
      }
      if (tmp !== 'video') {
        error = true;
      }
    }
    if (error) {
      this.testReporter.textAttachment(this.report, "Received videos", result, "plain");
      throw new KiteTestError(Status.FAILED, "Some videos are still or blank: " + result);
    }
  }
}

module.exports = AllVideoCheck;