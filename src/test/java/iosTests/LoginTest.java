package iosTests;

import baseTest.BaseTest;
import org.testng.annotations.Test;
import pages.LandingPage;
import pages.LoginPage;


public class LoginTest extends BaseTest {

    @Test
    public void loginWithValidCredentials() throws InterruptedException {
        System.out.println("loginWithValidCredentials: - " + Thread.currentThread().getId());
        new LandingPage().clickButtonSignIn();
        new LoginPage()
                .enterEmail("vmalik@mailinator.com")
                .enterPassword("miP4cvma")
                .clickLoginBtn()
                .validateLabelInventoryLoadComplete("Inventory Load Complete");


    }

    @Test
    public void loginWithInvalidValidCredentials() throws InterruptedException {
        System.out.println("loginWithValidCredentials: - " + Thread.currentThread().getId());
        new LandingPage().clickButtonSignIn();
        LoginPage loginPage = new LoginPage();
        loginPage.enterEmail("qvmalik@mailinator.com")
                .enterPassword("miP4cvma")
                .clickLoginBtn();
        loginPage.validateAlertTitle("Error Signing In");
        loginPage.closeApp();
    }
}
