package utils;

import com.github.device.Device;
import com.github.device.DeviceManager;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Testing {
    public static ExcelReader excel = new ExcelReader(
            System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator
                    + "resources" + File.separator + "excelData" + File.separator + "TestData.xlsx");

    public static void main(String[] args) throws Exception {

        //isSuiteExecutable("baseline");
        getClassesWithRunModeY("baseline");

        //getClassesWithRunModeY();
        //constructIncludes("myClass");


    }

    public void printDevices() throws Exception {
        DeviceManager deviceManager = new DeviceManager();
        List<Device> devices = deviceManager.getDevices();
        for (Device d : devices)
            System.out.println(d);
    }

    public static int getClassesWithRunModeY() {
        String sheetName = "suite";
        int rowCount = excel.getRowCount(sheetName);
        int colCount = excel.getColumnCount(sheetName);
        int dataSetWithRunMode = 0;
        String className;
        for (int rowNum = 2; rowNum <= rowCount; rowNum++) {
            if (excel.getCellData(sheetName, 0, rowNum).equalsIgnoreCase("Y")) {
                className = excel.getCellData(sheetName, 1, rowNum);
                System.out.println("Class Name: - " + className);
                dataSetWithRunMode++;
            }
        }
        return dataSetWithRunMode;
    }

    public static List<XmlInclude> constructIncludes(String className) {
        String sheetName = "suite";
        int rowCount = excel.getRowCount(sheetName);
        int colCount = excel.getColumnCount(sheetName);
        //int dataEndRow = excel.getRowCount(sheetName, 0, "EndOfRows");
        //int dataStartRow = startRow(sheetName, 1, className);
        // int dataStartRow = excel.getRowCount(sheetName);
        List<XmlInclude> includes = new ArrayList<>();
        for (int rowNum = 2; rowNum <= rowCount; rowNum++) {
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

    public static boolean isSuiteExecutable(String suiteName) {
        String sheetName = "suite";
        boolean isExecutable = false;
        int rowCount = excel.getRowCount(sheetName);
        for (int rowNum = 2; rowNum <= rowCount; rowNum++) {
            String testSuiteName = excel.getCellData(sheetName, 0, rowNum);
            if (testSuiteName.equalsIgnoreCase(suiteName)) {
                String runMode = excel.getCellData(sheetName, 1, rowNum);
                if (runMode.equalsIgnoreCase("Y")) {
                    isExecutable = true;
                    System.out.println("Test Suite Name is: " + testSuiteName);

                } else {
                    isExecutable = false;
                }
            }
        }
        return isExecutable;
    }

    public static List<XmlClass> getClassesWithRunModeY(String testClassName) {
        List<XmlClass> testClasses = new ArrayList<>();
        String sheetName = "baseline";
        boolean isExecutable = false;
        int rowCount = excel.getRowCount(sheetName);
        for (int rowNum = 2; rowNum <= rowCount; rowNum++) {
            String className = excel.getCellData(sheetName, "ClassName", rowNum);
            System.out.println("Class Name: - "+ className);
                String runMode = excel.getCellData(sheetName, "ClassRunMode", rowNum);
                if (runMode.equalsIgnoreCase("Y")) {
                    XmlClass testClass = new XmlClass();
                    testClass.setName(testClassName);
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
            System.out.println("Test Method Name: - "+ testMethod);
            String runMode = excel.getCellData(sheetName, "MethodRunMode", rowNum);
            if (runMode.equalsIgnoreCase("Y")) {
                methodsToInclude.add(new XmlInclude(testMethod));
            }
        }
        System.out.println(methodsToInclude.toString());
        return methodsToInclude;
    }

}
