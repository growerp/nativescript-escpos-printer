package cn.ichi.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.ichi.android.Client;
import cn.ichi.android.ClientListener;
import cn.ichi.android.bluetooth.BlueToothPrinter;
import cn.ichi.android.bluetooth.BluetoothClient;
import cn.ichi.android.bluetooth.BluetoothClientListener;
import cn.ichi.android.network.TcpClient;
import cn.ichi.android.network.TcpClientListener;
import cn.ichi.android.serialport.SerialPortClient;
import cn.ichi.android.serialport.SerialPortClientListener;
import cn.ichi.android.serialport.SerialPortPrinter;
import cn.ichi.android.usb.UsbClient;
import cn.ichi.android.usb.UsbClientListener;
import cn.ichi.android.usb.UsbPrinter;

public class Printer {
    private ClientListener mListener;
    private Client mClient;

    /***
     *
     * @param listener
     * @param type  0: TCP, 1: USB, 2: Bluetooth, 3: serial port,
     */
    public Printer(ClientListener listener, final int type) {
        mListener = listener;

        switch (type) {
            case 0:
                tcpClient();
                break;

            case 1:
                usbClient();
                break;

            case 2:
                bluetoothClient();
                break;

            case 3:
                serialPortClient();
                break;

            case 4:
                break;

            default:
                break;
        }
    }


    private void tcpClient() {
        mClient = new TcpClient(new TcpClientListener() {
            @Override
            public void onData(byte[] data) {
                String strData = ByteArrayToJsonString(data);
                mListener.onData(strData);
            }

            @Override
            public void onError(int id, String message) {
                mListener.onError(id, message);
            }

            @Override
            public void onConnected(int id){
                mListener.onConnected(id);
            }

            @Override
            public void onSent(int id){
                mListener.onSent(id);
            }

            @Override
            public void onClosed(int id){
                mListener.onClosed(id);
            }
        });
    }


    private  void usbClient(){
        mClient = new UsbClient(new UsbClientListener() {
            @Override
            public void onData(byte[] data) {
                String strData = ByteArrayToJsonString(data);
                mListener.onData(strData);
            }

            @Override
            public void onError(int id, String message) {
                mListener.onError(id, message);
            }

            @Override
            public void onConnected(int id){
                mListener.onConnected(id);
            }

            @Override
            public void onSent(int id){
                mListener.onSent(id);
            }

            @Override
            public void onClosed(int id){
                mListener.onClosed(id);
            }
        });
    }


    private  void bluetoothClient(){
        mClient = new BluetoothClient(new BluetoothClientListener() {
            @Override
            public void onData(byte[] data) {
                String strData = ByteArrayToJsonString(data);
                mListener.onData(strData);
            }

            @Override
            public void onError(int id, String message) {
                mListener.onError(id, message);
            }

            @Override
            public void onConnected(int id){
                mListener.onConnected(id);
            }

            @Override
            public void onSent(int id){
                mListener.onSent(id);
            }

            @Override
            public void onClosed(int id){
                mListener.onClosed(id);
            }
        });
    }


    private  void serialPortClient() {
        mClient = new SerialPortClient(new SerialPortClientListener() {
            @Override
            public void onData(byte[] data) {
                String strData = ByteArrayToJsonString(data);
                mListener.onData(strData);
            }

            @Override
            public void onError(int id, String message) {
                mListener.onError(id, message);
            }

            @Override
            public void onConnected(int id){
                mListener.onConnected(id);
            }

            @Override
            public void onSent(int id){
                mListener.onSent(id);
            }

            @Override
            public void onClosed(int id){
                mListener.onClosed(id);
            }
        });
    }


    public int connect(final String serverName, final int port) {
        return mClient.connect(serverName, port);
    }


    public int close() {
        return mClient.close();
    }


    public int send(final byte[] data) {
        return mClient.send(data);
    }


    public int receive() {
        return mClient.receive();
    }


    private String ByteArrayToJsonString(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }

        try {
            JSONStringer stringer = new JSONStringer();
            stringer.array();
            for (byte value : data) {
                stringer.value(value);
            }
            stringer.endArray();
            return stringer.toString();
        }catch (Exception ex) {
            return "";
        }
    }


    public static String getUsbPrinters() {
        String jsonString = ArrayToJsonString(UsbPrinter.getPrinters());
        return jsonString;
    }


    public static String getBlueToothPrinters() {
        String jsonString = ArrayToJsonString(BlueToothPrinter.getPrinters());
        return jsonString;
    }


    public static String getSerialPortPrinters() {
        String jsonString = ArrayToJsonString(SerialPortPrinter.getPrinters());
        return jsonString;
    }


    private static String ArrayToJsonString(List<String> listPrinters) {
        if (listPrinters == null || listPrinters.size() == 0) {
            return "";
        }

        try {
            JSONStringer stringer = new JSONStringer();
            stringer.array();
            for (String value : listPrinters) {
                stringer.value(value);
            }
            stringer.endArray();
            return stringer.toString();
        }catch (Exception ex) {
            return "";
        }
    }


 }
