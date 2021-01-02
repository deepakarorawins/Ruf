package listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reports.ExtentReport;
import utils.GlobalParams;
import utils.TestUtils;

public class DeeListeners implements ISuiteListener, ITestListener {
    TestUtils utils = new TestUtils();

    @Override
    public void onStart(ISuite suite) {
        System.out.println("@onStart");


    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("@onTestStart");
        GlobalParams params = new GlobalParams();
        ExtentReport.startTest(result.getName(), result.getMethod().getDescription())
                .assignCategory(params.getPlatformName() + "_" + params.getDeviceName())
                .assignAuthor("Dee Arora");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String logText = "<b>" + "Test Case:- " + methodName + " Passed" + "</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        ExtentReport.getTest().pass(m);
        //test.get().pass(m);
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("@onFinish");
        ExtentReport.getReporter().flush();

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
        TestUtils.captureScreenshot();
        String exceptionMessage = "";
        //exceptionMessage = result.getThrowable().getClass().getName();
        //exceptionMessage = exceptionMessage + " " + Arrays.toString(result.getThrowable().getStackTrace());
        exceptionMessage = exceptionMessage + " " + result.getThrowable().getLocalizedMessage();
        ExtentReport.getTest().log(Status.FAIL,
                "<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured:Click to see"
                        + "</font>" + "</b >" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>");

        try {
            ExtentReport.getTest().fail("<b>" + "<font color=" + "red>" + "ScreenShot of failure" + "</font>" + "</b>",
                    MediaEntityBuilder.createScreenCaptureFromPath(TestUtils.screenshotPath + TestUtils.screenshotName)
                            .build());
        } catch (Exception e) {
            // TODO: handle exception
        }

        String text = "This Test case got failed";
        Markup m = MarkupHelper.createLabel(text, ExtentColor.RED);
        ExtentReport.getTest().log(Status.FAIL, m);


    }
}
