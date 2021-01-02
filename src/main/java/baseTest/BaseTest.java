package baseTest;

import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.*;
import utils.DriverManager;
import utils.GlobalParams;
import utils.ServerManager;
import utils.VideoManager;

import java.io.IOException;
//mvn test -DplatformName=ios -DdeviceName=iPhone6s -Dmaven.surefire.debug
//mvn test -DplatformName=ios -DdeviceName=iPhone6s

@Listeners(listeners.DeeListeners.class)
public class BaseTest {
    GlobalParams params = new GlobalParams();


    @BeforeSuite
    public void initialize_suite(){
        System.out.println("@BeforeSuite");

    }

    @Parameters({"platformName", "isRealDevice", "udid", "deviceName",
            "wdaLocalPort", "webkitDebugProxyPort"})
    @BeforeClass
    public void initialize(String platformName, String isRealDevice, String udid, String deviceName,
                           String wdaLocalPort, @Optional("iOSOnly")String webkitDebugProxyPort){
        System.out.println("@BeforeClass: - Starting Appium Server");

        GlobalParams params = new GlobalParams();
        params.initializeGlobalParams();
        params.setPlatformName(platformName);
        params.setUDID(udid);
        params.setDeviceName(deviceName);
        params.setIsRealDevice(isRealDevice);
        params.setWdaLocalPort(wdaLocalPort);


        System.out.println("****************wdport: " +params.getWdaLocalPort());
        System.out.println("****************Device Name: " +params.getDeviceName());


        System.out.println(params.getPlatformName() + " - " + params.getDeviceName());
        ThreadContext.put("ROUTINGKEY", params.getPlatformName() + " - " + params.getDeviceName());
        new ServerManager().startServer();

        try {
            new DriverManager().initializeDriver();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @BeforeTest
    public void beforeTest(){
        System.out.println("@BeforeTest");

    }

    @AfterTest
    public void afterTest(){
        System.out.println("@AfterTest");


    }

    @BeforeMethod
    public synchronized void beforeMethod(){
        System.out.println("@BeforeMethod");


        new VideoManager().startRecording();
    }

    @AfterMethod
    public synchronized void afterMethod(){
        System.out.println("@AfterMethod");

        try {
            new VideoManager().stopRecording("ScenarioName");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @AfterClass
    public void quit(){
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
    public void quit_suite(){
        System.out.println("@AfterSuite");
    }

}
