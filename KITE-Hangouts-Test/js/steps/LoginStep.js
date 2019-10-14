const {TestStep, TestUtils} = require('kite-common');
const waitAround = TestUtils.waitAround;

module.exports = class LoginStep extends TestStep {

  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.timeout = kiteBaseTest.timeout;
    this.url = kiteBaseTest.url;
    this.uuid = kiteBaseTest.uuid;
    this.page = kiteBaseTest.page;
    this.id = kiteBaseTest.id;
    this.email = kiteBaseTest.payload.users[this.id].user;
    this.pass = kiteBaseTest.payload.users[this.id].pass;
  }

  stepDescription() {
    return 'Open ' + this.url + ' and login with username: ' + this.email + ' and password: ' + this.pass;
  }

  async step() {
    await this.page.open(this.url, 10000);
    // await this.page.resize(540, 960);
    // this.driver.manage().window().setRect(540, 960, 960 * (this.id % 2), 540 * (( this.id - (this.id % 2))/2));
    await this.page.signIn(this.email, this.pass);
    // await this.page.enterEmail(this.email);
    // await this.page.enterPassword(this.pass);
    await waitAround(2000); 
  }
}