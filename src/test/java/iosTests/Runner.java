package iosTests;

import baseTest.BaseTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class Runner extends BaseTest {
    Map<String, String> testngParams = new HashMap<>();
    //public static String platform = System.getProperty("platform");

    @BeforeSuite
    public void runTestNG() {
        DynamicTestNG dynamicTestNG = new DynamicTestNG();

        // This Map can hold your testng Parameters.
        testngParams.put("isRealDevice", "false");
        testngParams.put("platformName", "ios");
        testngParams.put("deviceName", "iSim8FromTestNGParam");
        testngParams.put("udid", "57D62164-6520-40F0-90D2-213F27B77F4F");
        testngParams.put("wdaLocalPort", "10004");
        testngParams.put("webkitDebugProxyPort", "11004");


        dynamicTestNG.createAndRunTestNgXml(testngParams);
    }
}
