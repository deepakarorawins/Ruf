package iosTests;

import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import utils.TestUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicTestNG {
    public void createAndRunTestNgXml(Map<String, String> testngParams) {
        TestUtils utils = new TestUtils();
        //Create an instance on TestNG
        TestNG testNG = new TestNG();
        List<Class<? extends ITestNGListener>> listenerClasses = new ArrayList<>();
        List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
        List<XmlTest> myTests = new ArrayList<XmlTest>();
        List<XmlClass> myClasses = new ArrayList<XmlClass>();

        //Create an instance of XML Suite and assign a name for it.
        XmlSuite mySuite = new XmlSuite();
        mySuite.setName("baseline");
        mySuite.setParallel(XmlSuite.ParallelMode.TESTS);
        ArrayList<String> listeners = new ArrayList<String>();
        listeners.add("listeners.DeeListeners");
        mySuite.setListeners(listeners);

        //Create an instance of XmlTest and assign a name for it.
        XmlTest myTest = new XmlTest(mySuite);
        myTest.setName("iphone8");
        myTest.setPreserveOrder(true);
        //Add any parameters that you want to set to the Test.
        myTest.setParameters(testngParams);

        //Create an instance of XmlClass and assign a name for it.
        XmlClass class1 = new XmlClass();
        class1.setName("iosTests.LoginTest");
        myClasses.add(class1);

        myTest.setXmlClasses(myClasses);
        mySuites.add(mySuite);
        utils.log().info(mySuite.toXml());
        // Create physical XML file based on the virtual XML content
        for (XmlSuite suite : mySuites) {
            createXmlFile(suite);
        }

        testNG.setXmlSuites(mySuites);
        testNG.run();
    }

    // This method will create an Xml file based on the XmlSuite data
    public void createXmlFile(XmlSuite mSuite) {
        FileWriter writer;
        try {
            writer = new FileWriter(new File("dynamicTestNG.xml"));
            writer.write(mSuite.toXml());
            writer.flush();
            writer.close();
            System.out.println(new File("dynamicTestNg.xml").getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        DynamicTestNG dynamicTestNG = new DynamicTestNG();

        // This Map can hold your testng Parameters.
        Map<String, String> testngParams = new HashMap<>();
        testngParams.put("isRealDevice", "false");
        testngParams.put("platformName", "ios");
        testngParams.put("deviceName", "iSim8FromTestNGParam");
        testngParams.put("udid", "57D62164-6520-40F0-90D2-213F27B77F4F");
        testngParams.put("wdaLocalPort", "10004");
        testngParams.put("webkitDebugProxyPort", "11004");
        dynamicTestNG.createAndRunTestNgXml(testngParams);


    }


}
