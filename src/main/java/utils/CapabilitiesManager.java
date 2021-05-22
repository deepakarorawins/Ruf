package utils;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class CapabilitiesManager {
    TestUtils utils = new TestUtils();

    public DesiredCapabilities getCaps() throws IOException {
        GlobalParams params = new GlobalParams();
        Properties props = new PropertyManager().getProps();

        try {
            utils.log().info("Getting capabilities");
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, params.getPlatformName());
            caps.setCapability(MobileCapabilityType.UDID, params.getUDID());
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, params.getDeviceName());

            switch (params.getPlatformName()) {
                case "android":
                    caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.getProperty("androidAutomationName"));
                    caps.setCapability("appPackage", props.getProperty("androidAppPackage"));
                    caps.setCapability("appActivity", props.getProperty("androidAppActivity"));
                    caps.setCapability("systemPort", params.getSystemPort());
                    caps.setCapability("chromeDriverPort", params.getChromeDriverPort());
                    //String androidAppUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
                    String androidAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                            + File.separator + "resources" + File.separator + "apps" + File.separator + "Android.SauceLabs.Mobile.Sample.app.2.2.1.apk";
                    utils.log().info("appUrl is" + androidAppUrl);
                    caps.setCapability("app", androidAppUrl);
                    break;
                case "ios":
                    caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.getProperty("iOSAutomationName"));
                    //String iOSAppUrl = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
                    //String iOSAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                    //        + File.separator + "resources" + File.separator + "apps" + File.separator + "SwagLabsMobileApp.app";

                    if (params.getIsRealDevice().equalsIgnoreCase("true")) {
                        String iOSIpaUrl = System.getProperty("user.dir") + props.getProperty("iOSIpaLocation");
                        utils.log().info("ipaUrl is" + iOSIpaUrl);
                        caps.setCapability("app", iOSIpaUrl);
                    } else if (params.getIsRealDevice().equalsIgnoreCase("false")) {
                        String iOSAppUrl = System.getProperty("user.dir") + props.getProperty("iOSAppLocation");
                        utils.log().info("appUrl is" + iOSAppUrl);
                        caps.setCapability("app", iOSAppUrl);
                    }


                    caps.setCapability("bundleId", props.getProperty("iOSBundleId"));
                    caps.setCapability("wdaLocalPort", params.getWdaLocalPort());
                    caps.setCapability("webkitDebugProxyPort", params.getWebkitDebugProxyPort());
                    //caps.setCapability("app", iOSIpaUrl);
                    caps.setCapability(MobileCapabilityType.FULL_RESET, "true");
                    caps.setCapability(MobileCapabilityType.NO_RESET, "false");


                    break;
            }

            return caps;
        } catch (Exception e) {
            e.printStackTrace();
            utils.log().fatal("Failed to load capabilities. ABORT!!" + e.toString());
            throw e;
        }
    }
}
