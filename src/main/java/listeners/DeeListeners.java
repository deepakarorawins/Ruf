package listeners;

import baseTest.BaseTest;
import org.apache.logging.log4j.ThreadContext;
import org.junit.runners.Suite;
import org.openqa.selenium.OutputType;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class DeeListeners implements ISuiteListener, ITestListener {
    TestUtils utils = new TestUtils();
    @Override
    public void onStart(ISuite suite) {
        System.out.println("@onStart");

//        GlobalParams params = new GlobalParams();
//        params.initializeGlobalParams();
//
//        System.out.println(params.getPlatformName() + " - " + params.getDeviceName());
//        ThreadContext.put("ROUTINGKEY", params.getPlatformName() + " - " + params.getDeviceName());
//        new ServerManager().startServer();
//        try {
//            new DriverManager().initializeDriver();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        new VideoManager().startRecording();
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("@onFinish");

//        try {
//            new VideoManager().stopRecording("ScenarioName");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        DriverManager driverManager = new DriverManager();
//        if (driverManager.getDriver() != null) {
//            driverManager.getDriver().quit();
//            driverManager.setDriver(null);
//        }
//
//        ServerManager serverManager = new ServerManager();
//        if (serverManager.getServer() != null) {
//            serverManager.getServer().stop();
//        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if(result.getThrowable() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            result.getThrowable().printStackTrace(pw);
            utils.log().error(sw.toString());
        }

        BaseTest base = new BaseTest();
        File file = new DriverManager().getDriver().getScreenshotAs(OutputType.FILE);

    }
}
