package utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtils {
    public static final long WAIT = 60;

    public String dateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Logger log() {
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }

    public static String screenshotPath;
    public static String screenshotName;
    public static void captureScreenshot() {

        screenshotPath = "./screenshots/"; // Later give this path into config.
        Date d = new Date();
        screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".png";

        //TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
        TakesScreenshot ts = new DriverManager().getDriver();
        File srcFile = ts.getScreenshotAs(OutputType.FILE);
        File destFile = new File(screenshotPath + screenshotName);
        try {
            FileUtils.copyFile(srcFile, destFile); // To use FileUtils Add Archive commons-io-2.6.jar or add dependency
            // in pom.xml
        } catch (IOException e) {
            System.out.println(e.fillInStackTrace());
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
    }

}
