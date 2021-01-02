package utils;

import com.github.device.Device;
import com.github.device.DeviceManager;
import com.github.device.DeviceType;

import java.util.List;

public class GlobalParams {
    private static ThreadLocal<String> platformName = new ThreadLocal<String>();
    private static ThreadLocal<String> isRealDevice = new ThreadLocal<String>();
    private static ThreadLocal<String> udid = new ThreadLocal<String>();
    private static ThreadLocal<String> deviceName = new ThreadLocal<String>();
    private static ThreadLocal<String> systemPort = new ThreadLocal<String>();
    private static ThreadLocal<String> chromeDriverPort = new ThreadLocal<String>();
    private static ThreadLocal<String> wdaLocalPort = new ThreadLocal<String>();
    private static ThreadLocal<String> webkitDebugProxyPort = new ThreadLocal<String>();

    public void setIsRealDevice(String isRealDevice1){
        isRealDevice.set(isRealDevice1);
    }

    public String getIsRealDevice(){
        return isRealDevice.get();
    }

    public void setPlatformName(String platformName1){
        platformName.set(platformName1);
    }

    public String getPlatformName(){
        return platformName.get();
    }

    public String getUDID() {
        return udid.get();
    }

    public void setUDID(String udid2) {
        udid.set(udid2);
    }

    public String getDeviceName() {
        return deviceName.get();
    }

    public void setDeviceName(String deviceName2) {
        deviceName.set(deviceName2);
    }

    public String getSystemPort() {
        return systemPort.get();
    }

    public void setSystemPort(String systemPort2) {
        systemPort.set(systemPort2);
    }

    public String getChromeDriverPort() {
        return chromeDriverPort.get();
    }

    public void setChromeDriverPort(String chromeDriverPort2) {
        chromeDriverPort.set(chromeDriverPort2);
    }

    public String getWdaLocalPort() {
        return wdaLocalPort.get();
    }

    public void setWdaLocalPort(String wdaLocalPort2) {
        wdaLocalPort.set(wdaLocalPort2);
    }

    public String getWebkitDebugProxyPort() {
        return webkitDebugProxyPort.get();
    }

    public void setWebkitDebugProxyPort(String webkitDebugProxyPort2) {
        webkitDebugProxyPort.set(webkitDebugProxyPort2);
    }

    public void initializeGlobalParams() {
        GlobalParams params = new GlobalParams();
        params.setIsRealDevice(System.getProperty("isRealDevice", "false"));
        params.setPlatformName(System.getProperty("platformName", "ios"));
        if(params.getIsRealDevice().equalsIgnoreCase("true")){
            params.setUDID(System.getProperty("udid", "417a4a9981685bcc05e15d32c5d76879807fe62b"));
        } else if (params.getIsRealDevice().equalsIgnoreCase("false")){
            params.setUDID(System.getProperty("udid", "57D62164-6520-40F0-90D2-213F27B77F4F"));
        }

        try {
            //params.setUDID(System.getProperty("udid", new DeviceManager().getDevices().get(0).getUdid()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.setDeviceName(System.getProperty("deviceName", "iPhone6s"));

        switch(params.getPlatformName()){
            case "Android":
                params.setSystemPort(System.getProperty("systemPort", "10000"));
                params.setChromeDriverPort(System.getProperty("chromeDriverPort", "11000"));
                break;
            case "ios":
                params.setWdaLocalPort(System.getProperty("wdaLocalPort", "10001"));
                params.setWebkitDebugProxyPort(System.getProperty("webkitDebugProxyPort", "11001"));
                break;
            default:
                throw new IllegalStateException("Invalid Platform Name! *** It should be ios or android");
        }
    }
}
