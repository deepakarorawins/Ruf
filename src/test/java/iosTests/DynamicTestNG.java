package iosTests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import utils.ExcelReader;
import utils.TestUtils;

public class DynamicTestNG {
    TestUtils utils = new TestUtils();
    ExcelReader excel = new ExcelReader(
            System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator
                    + "resources" + File.separator + "excelData" + File.separator + "TestData.xlsx");
    String sheetName = "settings";
    int numOfRows;

    public void createAndRunTestNgXml(Map<String, String> testngParams) {
        //final Logger logger = Logger.getLogger(Class.class.getName());
        Logger logger = LoggerFactory.getLogger(Class.class.getName());

        // Create an instance on TestNG
        TestNG myTestNG = new TestNG();
        List<Class<? extends ITestNGListener>> listenerClasses = new ArrayList<>();

        // Create an instance of XML Suite and assign a name for it.
        XmlSuite mySuite = new XmlSuite();
        mySuite.setName("MySuite");
        ///////////
        ArrayList<String> listeners = new ArrayList<String>();

        listeners.add("listeners.DeeListeners");

        mySuite.setListeners(listeners);
        /////

        // mySuite.setParallel(XmlSuite.ParallelMode.METHODS);

        // Create an instance of XmlTest and assign a name for it.
        XmlTest myTest = new XmlTest(mySuite);
        myTest.setName("MyTest");
        myTest.setPreserveOrder(true);

        // Add any parameters that you want to set to the Test.
        myTest.setParameters(testngParams);

        // Create a list which can contain the classes that you want to run.
        List<XmlClass> myClasses = new ArrayList<>();

        int dataEndRow = excel.getRowCount(sheetName, 0, "EndOfRows");
        String platform = System.getProperty("platformName");
        for (int rowNum = 3; rowNum <= dataEndRow; rowNum++) {
            String className = excel.getCellData(sheetName, 1, rowNum);
            String classRunMode = excel.getCellData(sheetName, 2, rowNum);
            if (classRunMode.equalsIgnoreCase("Y")) {
                XmlClass testClass = new XmlClass();
                switch (testngParams.get("platformName")) {
                    case "ios":
                        testClass.setName("iosTests."+ className);
                        break;
                    case "android":
                        testClass.setName("com.deesite.deeautomationpoc.test." + platform + ".Android" + className);
                        //testClass.setName("com.deesite.deeautomationpoc.test.android.LoginTest");
                        break;
                    default:
                        testClass.setName("com.deesite.deeautomationpoc.test." + platform + ".WebToolAdditionTest");
                }
                List<XmlInclude> methodsToRun = constructIncludes(className);
                testClass.setIncludedMethods(methodsToRun);
                myClasses.add(testClass);
            }
        }

		/*listenerClasses.add(CustomListeners.class);
		listenerClasses.add(RetryListener.class);*/

        // Assign that to the XmlTest Object created earlier.
        myTest.setXmlClasses(myClasses);

        // Create a list of XmlTests and add the Xmltest you created earlier to it.
        List<XmlTest> myTests = new ArrayList<>();
        myTests.add(myTest);

        // add the list of tests to your Suite.
        mySuite.setTests(myTests);

        // Add the suite to the list of suites.
        List<XmlSuite> mySuites = new ArrayList<>();
        mySuites.add(mySuite);

        // Set the list of Suites to the testNG object you created earlier.
        myTestNG.setXmlSuites(mySuites);
        myTestNG.setListenerClasses(listenerClasses);
        mySuite.setFileName("myTemp.xml");
        mySuite.setThreadCount(3);

        utils.log().info(mySuite.toXml());
        //logger.info(mySuite.toXml());
        myTestNG.run();

        // Create physical XML file based on the virtual XML content
        for (XmlSuite suite : mySuites) {
            createXmlFile(suite);
        }
        System.out.println("Filerated successfully.");

        // Print the parameter values
        Map<String, String> params = myTest.getAllParameters();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }

    public int startRow(String sheetName, int colNum, String testCase) {
        int testcaserownumber = 0;
        int totalrow = excel.getRowCount(sheetName);

        for (int i = 0; i < totalrow; i++) {
            if (excel.getCellData(sheetName, colNum, i).contains(testCase)) {
                testcaserownumber = i;
                break;
            }
        }

        return testcaserownumber;
    }

    public String[] getTestCasesOfSpecificClass(String className) {
        String[] testCasesWithRunModeasY = new String[numOfRows];
        int startingRow = startRow(sheetName, 0, className);
        String nextClassName;
        String testCaseName;
        String testCaseRunMode;
        int counter = 0;

        for (int i = startingRow; i <= numOfRows; i++) {
            nextClassName = excel.getCellData(sheetName, 1, i);
            testCaseName = excel.getCellData(sheetName, 4, i);
            testCaseRunMode = excel.getCellData(sheetName, 5, i);

            if (nextClassName.length() > 0 && !nextClassName.equalsIgnoreCase(className)) {
                break;
            }

            if ((nextClassName == "" || nextClassName.equalsIgnoreCase(className))
                    && testCaseRunMode.equalsIgnoreCase("Y")) {
                testCasesWithRunModeasY[counter] = testCaseName;
                System.out.println(className + "	-	" + testCaseName);
                counter++;
            }

        }
        String[] testCasesForXml = new String[counter];

        for (int i = 0; i < counter; i++) {
            testCasesForXml[i] = testCasesWithRunModeasY[i];
        }

        return testCasesForXml;
    }

    public List<XmlInclude> constructIncludes(String className) {
        int dataEndRow = excel.getRowCount(sheetName, 0, "EndOfRows");
        int dataStartRow = startRow(sheetName, 1, className);
        // int dataStartRow = excel.getRowCount(sheetName);
        List<XmlInclude> includes = new ArrayList<>();
        for (int rowNum = dataStartRow; rowNum <= dataEndRow; rowNum++) {
            String testClassName = excel.getCellData(sheetName, 1, rowNum);
            String testCaseName = excel.getCellData(sheetName, 4, rowNum);
            String testCaseRunMode = excel.getCellData(sheetName, 5, rowNum);

            if (testClassName.length() > 0 && !testClassName.equalsIgnoreCase(className)) {
                break;
            } else if ((testClassName == "" || testClassName.equalsIgnoreCase(className))
                    && testCaseRunMode.equalsIgnoreCase("Y")) {
                includes.add(new XmlInclude(testCaseName));
                System.out.println(className + "	-	" + testCaseName);
            }
        }
        return includes;
    }

    // This method will create an Xml file based on the XmlSuite data
    public void createXmlFile(XmlSuite mSuite) {
        FileWriter writer;
        try {
            writer = new FileWriter(new File("myTemp.xml"));
            writer.write(mSuite.toXml());
            writer.flush();
            writer.close();
            System.out.println(new File("myTemp.xml").getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main Method
    public static void main(String args[]) {
        DynamicTestNG dtestNg = new DynamicTestNG();

        // This Map can hold your testng Parameters.
        Map<String, String> testngParams = new HashMap<>();
        testngParams.put("isRealDevice", "false");
        testngParams.put("platformName", "ios");
        testngParams.put("deviceName", "iSim8FromTestNGParam");
        testngParams.put("udid", "57D62164-6520-40F0-90D2-213F27B77F4F");
        testngParams.put("wdaLocalPort", "10004");
        testngParams.put("webkitDebugProxyPort", "11004");
        dtestNg.createAndRunTestNgXml(testngParams);
    }

}






















/*

import org.testng.ITestNGListener;
        import org.testng.TestNG;
        import org.testng.xml.XmlClass;
        import org.testng.xml.XmlInclude;
        import org.testng.xml.XmlSuite;
        import org.testng.xml.XmlTest;
        import utils.ExcelReader;
        import utils.TestUtils;

        import java.io.File;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class DynamicTestNG {
    TestUtils utils = new TestUtils();
    ExcelReader excel = new ExcelReader(
            System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator
                    + "resources" + File.separator + "testdata" + File.separator + "TestData.xlsx");
    String sheetName = "settings";
    int numOfRows;
    public void createAndRunTestNgXml(Map<String, String> testngParams) {
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
        //myClasses = getClassesWithRunModeY("baseline");
        //XmlClass class1 = new XmlClass();
        //class1.setName("iosTests.LoginTest");
        //myClasses.add(class1);


        int dataEndRow = excel.getRowCount(sheetName, 0, "EndOfRows");
        String platform = System.getProperty("platform");
        for (int rowNum = 3; rowNum <= dataEndRow; rowNum++) {
            String className = excel.getCellData(sheetName, 1, rowNum);
            String classRunMode = excel.getCellData(sheetName, 2, rowNum);
            if (classRunMode.equalsIgnoreCase("Y")) {
                XmlClass testClass = new XmlClass();
                switch (testngParams.get("platform")) {
                    case "ios":
                        testClass.setName("iosTests." + className);
                        break;
                    case "android":
                        testClass.setName("com.deesite.deeautomationpoc.test." + platform + ".Android" + className);
                        //testClass.setName("com.deesite.deeautomationpoc.test.android.LoginTest");
                        break;
                    default:
                        testClass.setName("com.deesite.deeautomationpoc.test." + platform + ".WebToolAdditionTest");
                }
                List<XmlInclude> methodsToRun = constructIncludes(className);
                testClass.setIncludedMethods(methodsToRun);
                myClasses.add(testClass);
            }
        }





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

    public List<XmlInclude> constructIncludes(String className) {
        int dataEndRow = excel.getRowCount(sheetName, 0, "EndOfRows");
        int dataStartRow = startRow(sheetName, 1, className);
        // int dataStartRow = excel.getRowCount(sheetName);
        List<XmlInclude> includes = new ArrayList<>();
        for (int rowNum = dataStartRow; rowNum <= dataEndRow; rowNum++) {
            String testClassName = excel.getCellData(sheetName, 1, rowNum);
            String testCaseName = excel.getCellData(sheetName, 4, rowNum);
            String testCaseRunMode = excel.getCellData(sheetName, 5, rowNum);

            if (testClassName.length() > 0 && !testClassName.equalsIgnoreCase(className)) {
                break;
            } else if ((testClassName == "" || testClassName.equalsIgnoreCase(className))
                    && testCaseRunMode.equalsIgnoreCase("Y")) {
                includes.add(new XmlInclude(testCaseName));
                System.out.println(className + "	-	" + testCaseName);
            }
        }
        return includes;
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

    //************Excel************
    public static ExcelReader excel = new ExcelReader(
            System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator
                    + "resources" + File.separator + "excelData" + File.separator + "TestData.xlsx");

    public static List<XmlClass> getClassesWithRunModeY(String testClassName) {
        List<XmlClass> testClasses = new ArrayList<>();
        String sheetName = "baseline";
        boolean isExecutable = false;
        int rowCount = excel.getRowCount(sheetName);
        for (int rowNum = 2; rowNum <= rowCount; rowNum++) {
            String className = excel.getCellData(sheetName, "ClassName", rowNum);
            System.out.println("Class Name: - " + className);
            String runMode = excel.getCellData(sheetName, "ClassRunMode", rowNum);
            if (runMode.equalsIgnoreCase("Y")) {
                XmlClass testClass = new XmlClass();
                testClass.setName("iosTests."+testClassName);
                List<XmlInclude> methodsToExecute = new ArrayList<>();
                testClass.setIncludedMethods(getMethodsWithRunModeY());
                testClasses.add(testClass);
            } else {
                isExecutable = false;
            }
        }
        System.out.println(testClasses.toString());
        return testClasses;
    }

    public static List<XmlInclude> getMethodsWithRunModeY() {
        List<XmlInclude> methodsToInclude = new ArrayList<>();
        String sheetName = "baseline";
        boolean isExecutable = false;
        int rowCount = excel.getRowCount(sheetName);
        for (int rowNum = 2; rowNum <= rowCount; rowNum++) {
            String testMethod = excel.getCellData(sheetName, "MethodName", rowNum);
            System.out.println("Test Method Name: - " + testMethod);
            String runMode = excel.getCellData(sheetName, "MethodRunMode", rowNum);
            if (runMode.equalsIgnoreCase("Y")) {
                methodsToInclude.add(new XmlInclude(testMethod));
            }
        }
        System.out.println(methodsToInclude.toString());
        return methodsToInclude;
    }


//************Excel************

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

*/
