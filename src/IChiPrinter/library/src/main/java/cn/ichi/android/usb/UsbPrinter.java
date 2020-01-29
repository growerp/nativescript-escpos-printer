package cn.ichi.android.usb;

import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.support.annotation.NonNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cn.ichi.android.Utils;

/**
 * Created by mozj on 2018/2/26.
 */

public class UsbPrinter {
    private UsbManager usbManager;
    /**
     * 满足的设备
     */
    private UsbDevice myUsbDevice;
    /**
     * usb接口
     */
    private UsbInterface usbInterface;
    /**
     * 块输出端点
     */
    private UsbEndpoint epBulkOut;
    private UsbEndpoint epBulkIn;
    /**
     * 控制端点
     */
    private UsbEndpoint epControl;
    /**
     * 中断端点
     */
    private UsbEndpoint epIntEndpointOut;
    private UsbEndpoint epIntEndpointIn;
    /**
     * 连接
     */
    private UsbDeviceConnection myDeviceConnection;

    private String printerName;
    private String errorMsg = "";

    PendingIntent mPermissionIntent;
    BroadcastReceiver mUsbReceiver = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("BroadcastReceiver in\n");
            if (ACTION_DEVICE_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            System.out.println("usb EXTRA_PERMISSION_GRANTED");
                        }
                    } else {
                        System.out.println("usb EXTRA_PERMISSION_GRANTED null!!!");
                    }
                }
            }
        }
    };
    String ACTION_DEVICE_PERMISSION = "cn.ichi.android.USB_PERMISSION";

    public  UsbPrinter(String name) {
        Application application = Utils.getApplication();

        usbManager = (UsbManager) application.getSystemService(Context.USB_SERVICE);

        printerName = name;
    }

    public static List<String> getPrinters() {
        Application application = Utils.getApplication();
        if (application == null) {
            return null;
        }

        UsbManager usbManager = (UsbManager) application.getSystemService(Context.USB_SERVICE);

        Collection<UsbDevice> devices = usbManager.getDeviceList().values();

        List<String> lstPrinterName = new ArrayList<String>();
        for(UsbDevice device: devices) {
            if (device.getInterfaceCount() > 0) {
                UsbInterface anInterface = device.getInterface(0);
                if (anInterface != null && anInterface.getInterfaceClass() == 7) {
                    lstPrinterName.add(device.getDeviceName());
                }
            }
        }

        return lstPrinterName;
    }


    private void getUsbDevice() {
        Collection<UsbDevice> devices = usbManager.getDeviceList().values();

        for(UsbDevice device: devices) {
            if (device.getInterfaceCount() > 0) {
                UsbInterface anInterface = device.getInterface(0);
                if (anInterface.getInterfaceClass() == 7) {
                    if (device.getDeviceName().equals(printerName)) {
                        myUsbDevice = device;
                        usbInterface = anInterface;
                    }
                }
            }
        }
    }


    /**
     * 分配端点，IN | OUT，即输入输出；可以通过判断
     */
    private void assignEndpoint() {
        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            UsbEndpoint ep = usbInterface.getEndpoint(i);
            switch (ep.getType()){
                case UsbConstants.USB_ENDPOINT_XFER_BULK://块
                    if(UsbConstants.USB_DIR_OUT==ep.getDirection()){//输出
                        epBulkOut = ep;
                        System.out.println("Find the BulkEndpointOut," + "index:" + i + "," + "使用端点号："+ epBulkOut.getEndpointNumber());
                    }else{
                        epBulkIn = ep;
                        System.out .println("Find the BulkEndpointIn:" + "index:" + i+ "," + "使用端点号："+ epBulkIn.getEndpointNumber());
                    }
                    break;
                case UsbConstants.USB_ENDPOINT_XFER_CONTROL://控制
                    epControl = ep;
                    System.out.println("find the ControlEndPoint:" + "index:" + i+ "," + epControl.getEndpointNumber());
                    break;
                case UsbConstants.USB_ENDPOINT_XFER_INT://中断
                    if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {//输出
                        epIntEndpointOut = ep;
                        System.out.println("find the InterruptEndpointOut:" + "index:" + i + ","  + epIntEndpointOut.getEndpointNumber());
                    }
                    if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
                        epIntEndpointIn = ep;
                        System.out.println("find the InterruptEndpointIn:" + "index:" + i + ","+ epIntEndpointIn.getEndpointNumber());
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 连接设备
     */
    private boolean openDevice() {

        // 在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限
        UsbDeviceConnection conn = null;
        if(usbManager.hasPermission(myUsbDevice)){
            //有权限，那么打开
            conn = usbManager.openDevice(myUsbDevice);
        } else {
            //没有权限，那么请求USB读写权限
            Application application = Utils.getApplication();
            mPermissionIntent = PendingIntent.getBroadcast(application, 0, new Intent(ACTION_DEVICE_PERMISSION),0);
            IntentFilter permissionFilter = new IntentFilter(ACTION_DEVICE_PERMISSION);
            application.registerReceiver(mUsbReceiver, permissionFilter);
            usbManager.requestPermission(myUsbDevice, mPermissionIntent);

            errorMsg = "没有USB读写权限";
            return false;
        }
        if(null==conn){
            errorMsg = "不能连接到设备";
//                Toast.makeText(this,"不能连接到设备",Toast.LENGTH_SHORT).show();
            return false;
        }
        //打开设备
        if(conn.claimInterface(usbInterface,true)){
            myDeviceConnection = conn;
            if (myDeviceConnection != null)// 到此你的android设备已经连上USB设备
                System.out.println("open设备成功！");
            final String mySerial = myDeviceConnection.getSerial();
            System.out.println("设备serial number：" + mySerial);
            return  true;
        } else {
            errorMsg = "无法打开连接通道";
            System.out.println("无法打开连接通道。");
//                Toast.makeText(this,"无法打开连接通道。",Toast.LENGTH_SHORT).show();
            conn.close();
            myDeviceConnection = null;
        }

        return false;
    }


    public boolean connectPrinter() {
        errorMsg = "";
        myUsbDevice = null;
        usbInterface = null;
        myDeviceConnection = null;

        getUsbDevice();

        if (myUsbDevice == null) {
            errorMsg = "没有找到指定的USB设备";
            return false;
        }

        assignEndpoint();

        return openDevice();
    }

    /**
     * 发送数据
     * @param buffer
     */
    public boolean sendMessage(byte[] buffer) {
        errorMsg = "";
        if (myDeviceConnection == null) {
            errorMsg = "连接已关闭";
            return false;
        }

        if(myDeviceConnection.bulkTransfer(epBulkOut,buffer,buffer.length,0) >= 0){
            //0 或者正数表示成功
//            Toast.makeText(this,"发送成功",Toast.LENGTH_SHORT).show();
            return true;
        }else{
            errorMsg = "发送失败";
//            Toast.makeText(this,"发送失败的",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public int receiveMessage(byte[] buffer) {
        errorMsg = "";
        if (myDeviceConnection != null) {
            return myDeviceConnection.bulkTransfer(epBulkIn, buffer, buffer.length, 3000);
        } else {
            errorMsg = "连接已关闭";
            return -1;
        }
    }


    public boolean closeDevice() {
        errorMsg = "";
        if (myDeviceConnection != null) {
            myDeviceConnection.close();
            myDeviceConnection = null;
            epBulkOut = null;
            epBulkIn = null;
        }

        return true;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
