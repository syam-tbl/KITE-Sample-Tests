const {By, Key, until} = require('selenium-webdriver');
const {BasePage,TestUtils, KiteTestError, Status} = require('kite-common');
const verifyVideoDisplayByIndex = TestUtils.verifyVideoDisplayByIndex;
const waitAround = TestUtils.waitAround;

const signInButton = By.id('gb_70');
const emailInput = By.id('identifierId');
const passwordInput = By.xpath('//*[@id=\"password\"]/div[1]/div/div[1]/input');
const idNextButton = By.id('identifierNext');
const pwNextButton = By.id('passwordNext');
const nextButton = By.xpath('//*[@id=\"yDmH0d\"]/div[8]/div[2]/div/div[3]');
const videoCallButton = By.xpath('//*[@id=\"yDmH0d\"]/div[4]/div[4]/div/div/ul/li[1]/div[1]');
const joinButton = By.xpath('//*[@id=\"yDmH0d\"]/div[4]/div[2]/div/div/div[2]/div[2]/div/span/span');

const closePopup = By.xpath('//*[@id=\"yDmH0d\"]/div[6]/div/div[2]/div[2]/div[3]/div/span');
const videoElements = By.css('video');

const ONE_SECOND = 1000;

module.exports =  class MainPage extends BasePage {

  constructor(driver) {
    super(driver);
  }

  /**
   * Sign in through a series of steps
   * @param email{String} : user's email
   * @param password{String} : user's password
   * @returns {Promise<void>}
   */
  async signIn(email, password) {
    console.log("Signing in");
    await this.click(signInButton);
    await this.enterEmail(email);
    await this.click(idNextButton);
    await this.enterPassword(password);
    await this.click(pwNextButton);
    // await this.skipPresentation();
  }

  /**
   * Clicks join button
   * @returns {Promise<void>}
   */
  async clickJoin() {
    console.log("Click Join button");
    return this.click(joinButton);
  }

  /**
   * Inputs email to email input field
   * @param email{String} user's email
   * @returns {Promise<void>}
   */
  async enterEmail(email) {
    console.log("Entering email (" + email + ")");
    await this.waitForElement(emailInput);
    await this.sendKeys(emailInput, email);
  }

  /**
   * Inputs password to password input field
   * @param password{String} user's password
   * @returns {Promise<void>}
   */
  async enterPassword(password) {
    console.log("Entering password (" + password + ")");
    await waitAround(ONE_SECOND);
    await this.waitForElement(passwordInput);
    await this.sendKeys(passwordInput, password);
    await waitAround(ONE_SECOND);
  }

  /**
   * Skips hangout presentation if exists
   * @returns {Promise<void>}
   */
  async skipPresentation () {
    try {
      console.log("Skipping presentation");
      await this.click(nextButton);
      await this.click(nextButton);
      await this.click(nextButton);
      await this.click(nextButton);
    } catch (err) {
      console.log("No presentation to skip");
    }
  }

  /**
   * Gets page's video element array
   * @returns {Promise<*>}
   */
  async getVideos() {
    return this.driver.findElements(videoElements);
  }

  /**
   * Starts video call through a series of steps
   * @returns {Promise<void>}
   */
  async startVideoCall() {
    console.log("Starting video call");
    await this.click(videoCallButton);
    await waitAround(3 * ONE_SECOND);
    await this.switchWindowHandler();
    await this.closePopup();
  }

  /**
   * Closes invite popup in hangout call
   * @returns {Promise<void>}
   */
  async closePopup() {
    console.log("Closing invite popup");
    this.click(closePopup);
  }

  /**
   * Returns an array of index of valid videos (display:true and dimension != 0)
   * @returns {Promise<void>}
   */
  async getVideoIndex() {
    let videos = await this.getVideos();
    let i;
    const indexArr = [];
    for (i = 0; i < videos.length; i++) {
      let video = videos[i];
      if (await video.isDisplayed()) {
        if (await video.getCssValue("height") !== "0px" && await video.getCssValue("width") !== "0px") {
          indexArr.push(i);
        }
      }
    }
    return indexArr;
  }

}