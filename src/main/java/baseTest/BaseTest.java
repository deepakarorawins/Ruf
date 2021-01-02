package baseTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
//mvn test -DplatformName=ios -DdeviceName=iPhone6s -Dmaven.surefire.debug
//mvn test -DplatformName=ios -DdeviceName=iPhone6s

@Listeners(listeners.DeeListeners.class)
public class BaseTest {
    GlobalParams params = new GlobalParams();
    Properties props;

    {
        try {
            props = new PropertyManager().getProps();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
    //protected static ThreadLocal <Properties> props = new ThreadLocal<Properties>();
    protected static ThreadLocal<HashMap<String, String>> strings = new ThreadLocal<HashMap<String, String>>();
    protected static ThreadLocal<String> platform = new ThreadLocal<String>();
    protected static ThreadLocal<String> dateTime = new ThreadLocal<String>();
    protected static ThreadLocal<String> deviceName = new ThreadLocal<String>();
    private static AppiumDriverLocalService server;
    TestUtils utils = new TestUtils();


    @BeforeSuite
    public void initialize_suite() {
        System.out.println("@BeforeSuite");

    }

    @Parameters({"platformName", "isRealDevice", "udid", "deviceName",
            "wdaLocalPort", "webkitDebugProxyPort"})
    @BeforeClass
    public void initialize(String platformName, String isRealDevice, String udid, String deviceName,
                           String wdaLocalPort, @Optional("iOSOnly") String webkitDebugProxyPort) {
        System.out.println("@BeforeClass: - Starting Appium Server");

        GlobalParams params = new GlobalParams();
        params.initializeGlobalParams();
        params.setPlatformName(platformName);
        params.setUDID(udid);
        params.setDeviceName(deviceName);
        params.setIsRealDevice(isRealDevice);
        params.setWdaLocalPort(wdaLocalPort);


        System.out.println("****************wdport: " + params.getWdaLocalPort());
        System.out.println("****************Device Name: " + params.getDeviceName());


        System.out.println(params.getPlatformName() + " - " + params.getDeviceName());
        ThreadContext.put("ROUTINGKEY", params.getPlatformName() + " - " + params.getDeviceName());
        new ServerManager().startServer();

        try {
            this.driver = new DriverManager().initializeDriver();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("@BeforeTest");


    }

    @AfterTest
    public void afterTest() {
        System.out.println("@AfterTest");


    }

    @BeforeMethod
    public synchronized void beforeMethod() {
        System.out.println("@BeforeMethod");
        String bundleId = props.getProperty("iOSBundleId");

        //String bundleId = props.getProperty("iOSBundleId");
        if (this.driver.get().isAppInstalled(bundleId)) {
            this.driver.get().removeApp(bundleId);
        }

        if (!this.driver.get().isAppInstalled(bundleId)) {
            if (params.getIsRealDevice().equalsIgnoreCase("true")) {
                String iOSIpaUrl = System.getProperty("user.dir") + props.getProperty("iOSIpaLocation");
                utils.log().info("ipaUrl is" + iOSIpaUrl);
                this.driver.get().installApp(iOSIpaUrl);
            } else if (params.getIsRealDevice().equalsIgnoreCase("false")) {
                String iOSAppUrl = System.getProperty("user.dir") + props.getProperty("iOSAppLocation");
                utils.log().info("appUrl is" + iOSAppUrl);
                this.driver.get().installApp(iOSAppUrl);
            }
        }
        this.driver.get().activateApp(bundleId);

        new VideoManager().startRecording();
    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult result) {
        System.out.println("@AfterMethod");


        String dirPath = "videos" + File.separator + params.getPlatformName() + "_" + params.getDeviceName()
                + File.separator + utils.dateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();

        File videoDir = new File(dirPath);

        try {
            new VideoManager().stopRecording(result.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @AfterClass
    public void quit() {
        System.out.println("@AfterClass: - Stopping Appium Server");

        DriverManager driverManager = new DriverManager();
        if (driverManager.getDriver() != null) {
            driverManager.getDriver().quit();
            driverManager.setDriver(null);
        }


        ServerManager serverManager = new ServerManager();
        if (serverManager.getServer() != null) {
            serverManager.getServer().stop();
        }

    }

    @AfterSuite
    public void quit_suite() {
        System.out.println("@AfterSuite");
    }

}
