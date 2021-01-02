package pages;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.testng.Assert;
import utils.TestUtils;

public class InventoryPage extends BasePage {
    TestUtils utils = new TestUtils();

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(id = "Inventory Load Complete")
    private MobileElement txtInventoryLoadComplete;



    public InventoryPage() {
    }

    public void validateLabelInventoryLoadComplete(String expected) throws InterruptedException {
        Assert.assertEquals(getAttribute(txtInventoryLoadComplete, "label"), expected);
    }






}

