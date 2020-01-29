package cn.ichi.android.bluetooth;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cn.ichi.android.Utils;

/**
 * Created by mozj on 2018/2/26.
 */

public class BlueToothPrinter {
    private static final int OPEN_BLUETOOTH_REQUEST = 111;
    private static final UUID MY_UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;

    private BluetoothDevice myDevice;

    private BluetoothSocket mSocket;
    private InputStream mInStream;
    private OutputStream mOutStream;

    private String printerName;
    private String errorMsg="";

    public BlueToothPrinter(String name) {
        printerName = name;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static List<String> getPrinters() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

        List<String> lstPrinterName = new ArrayList<String>();
        for(BluetoothDevice device : devices){
            lstPrinterName.add(device.getName());
        }

        return lstPrinterName;
    }


    private void getDevice() {
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

        for(BluetoothDevice device : devices){
            if (printerName.equals(device.getName())) {
                myDevice = device;
            }
        }
    }


    /**
     * 连接设备
     */
    private boolean openDevice() {
        BluetoothSocket tmp = null;
        try {
            tmp = myDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (Exception e) {
            errorMsg ="无法打开连接通道: " +e.getMessage();
//                Log.e(TAG, "Socket Type: " + mSocketType + " create() failed", e);
            return false;
        }

        //打开设备
        try {
            mSocket = tmp;
            mSocket.connect();

            mInStream = mSocket.getInputStream();
            mOutStream = mSocket.getOutputStream();

            return  true;
        } catch (Exception e) {
            errorMsg ="无法连接蓝牙设备: " +e.getMessage();
            System.out.println(e.getMessage());
//                Toast.makeText(this,"无法打开连接通道。",Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public boolean connectPrinter() {
        errorMsg = "";
        if (bluetoothAdapter.isEnabled()) {
            getDevice();

            if(myDevice!=null ) {
                return openDevice();
            } else {
                errorMsg = "没有找到指定的蓝牙设备";
            }
        } else {
            Activity activity = Utils.getActivity();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, OPEN_BLUETOOTH_REQUEST);

            errorMsg = "系统蓝牙已关闭，不能连接蓝牙设备";
        }
        return  false;
    }


    /**
     * 发送数据
     * @param buffer
     */
    public boolean sendMessage(byte[] buffer) {
        errorMsg = "";
        if (mSocket == null) {
            errorMsg = "连接已关闭";
            return false;
        }
        try {
            mOutStream.write(buffer);
            return true;
        } catch (Exception e) {
            errorMsg = "发送失败: " + e.getMessage();
            return false;
        }
    }


    public int receiveMessage(byte[] buffer) {
        errorMsg = "";
        if (mSocket == null) {
            errorMsg = "连接已关闭";
            return -1;
        }

        try {
            return mInStream.read(buffer);
        } catch (Exception e) {
            errorMsg = "接送数据失败: " + e.getMessage();
            return -1;
        }
    }


    public boolean closeDevice() {
        errorMsg = "";
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (Exception e) {
                errorMsg = "连接关闭失败: " + e.getMessage();
            }
            mInStream = null;
            mOutStream = null;
            mSocket = null;
        }

        return true;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
