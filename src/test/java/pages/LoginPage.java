package pages;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.testng.Assert;
import utils.TestUtils;

public class LoginPage extends BasePage {
    TestUtils utils = new TestUtils();

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(id = "email_text_field")
    private MobileElement txtEmailAddress;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(id = "password_text_field")
    private MobileElement txtPassword;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(id = "login_button")
    private MobileElement btnSignIn;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeAlert//XCUIElementTypeStaticText[1]")
    private MobileElement lblAlertTitle;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeAlert//XCUIElementTypeStaticText[2]")
    private MobileElement lblAlertDescription;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeAlert//XCUIElementTypeButton")
    private MobileElement btnAlertOk;


    public LoginPage() {
    }

    public LoginPage enterEmail(String emailAddress) throws InterruptedException {
        clear(txtEmailAddress);
        sendKeys(txtEmailAddress, emailAddress, "login with " + emailAddress);
        return this;
    }

    public LoginPage enterPassword(String password) {
        clear(txtPassword);
        sendKeys(txtPassword, password, "password is " + password);
        return this;
    }

    public InventoryPage clickLoginBtn() {
        click(btnSignIn, "Clicked login button");
        return new InventoryPage();
    }

    //Common Method
    public void validateAlertTitle(String expected) throws InterruptedException {
        Assert.assertEquals(getAttribute(lblAlertTitle, "label"), expected);
    }



}
