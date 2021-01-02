package utils;

import com.github.device.Device;
import com.github.device.DeviceManager;

import java.util.List;

public class Testing {
    public static void main(String[] args) throws Exception {
        DeviceManager deviceManager = new DeviceManager();
        List<Device> devices = deviceManager.getDevices();
        for(Device d: devices)
            System.out.println(d);



    }
}
