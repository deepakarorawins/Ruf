package pages;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import utils.TestUtils;

public class LandingPage extends BasePage {
    TestUtils utils = new TestUtils();

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(id = "Sign In")
    private MobileElement btnSignIn;

    public LandingPage() {
    }

    public LoginPage clickButtonSignIn() {
        click(btnSignIn);
        return new LoginPage();
    }

}
