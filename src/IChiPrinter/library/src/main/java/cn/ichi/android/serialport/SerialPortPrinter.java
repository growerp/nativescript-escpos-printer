package cn.ichi.android.serialport;

import android.app.Application;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.ichi.android.Utils;

/**
 * Created by mozj on 2018/2/26.
 */

public class SerialPortPrinter {

    private String printerName;

    private String errorMsg = "";

    public SerialPortPrinter(String name) {

        printerName = name;
    }

    public static List<String> getPrinters() {
        // TODO
        return null;
    }



    public boolean connectPrinter() {
        errorMsg = "";

        // TODO

        errorMsg = "暂不支持该类型的设备";
        return  false;
    }

    /**
     * 发送数据
     * @param buffer
     */
    public boolean sendMessage(byte[] buffer) {
        errorMsg = "";

        // TODO

        errorMsg = "暂不支持该类型的设备";
        return false;
    }


    public int receiveMessage(byte[] buffer) {
        errorMsg = "";

        // TODO

        errorMsg = "暂不支持该类型的设备";
        return -1;
    }


    public boolean closeDevice() {
        errorMsg = "";

        // TODO

        errorMsg = "暂不支持该类型的设备";
        return false;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
