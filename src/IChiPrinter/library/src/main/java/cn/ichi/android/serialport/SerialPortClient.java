package cn.ichi.android.serialport;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import cn.ichi.android.Client;

public class SerialPortClient implements Client {
    private SerialPortClientListener mListener;
    private ExecutorService mExecutor;
    private SerialPortPrinter mSerialPortPrinter;
    private AtomicInteger mId;

    private final int mPollSize = 5;

    SerialPortClient(SerialPortPrinter serialPortPrinter, AtomicInteger id, SerialPortClientListener listener) {
        mListener = listener;
        mSerialPortPrinter = serialPortPrinter;
        mId = id;
        mExecutor = Executors.newFixedThreadPool(mPollSize);
    }

    public SerialPortClient(SerialPortClientListener listener) {
        mListener = listener;
        mExecutor = Executors.newFixedThreadPool(mPollSize);
        mId = new AtomicInteger();
    }

    @Override
    public int connect(final String printerName, final int port) {
        final int id = mId.getAndIncrement();
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    mSerialPortPrinter = new SerialPortPrinter(printerName);
                    if (mSerialPortPrinter.connectPrinter()) {
                        mListener.onConnected(id);
                    } else {
                        mListener.onError(id, mSerialPortPrinter.getErrorMsg());
                    };
                } catch (Exception e) {
                    mListener.onError(id, e.getMessage());
                }
            }
        });
        return id;
    }

    @Override
    public int close() {
        final int id = mId.getAndIncrement();
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    mSerialPortPrinter.closeDevice();
                    mListener.onClosed(id);
                } catch (Exception e) {
                    mListener.onError(id, e.getMessage());
                }
            }
        });
        mExecutor.shutdown();
        mExecutor = null;
        return id;
    }

    @Override
    public int send(final byte[] data) {
        final int id = mId.getAndIncrement();
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mSerialPortPrinter.sendMessage(data)) {
                        mListener.onSent(id);
                    } else {
                        mListener.onError(id, mSerialPortPrinter.getErrorMsg());
                    }
                } catch (Exception e) {
                    mListener.onError(id, e.getMessage());
                }
            }
        });
        return id;
    }

    @Override
    public int receive() {
        final int id = mId.getAndIncrement();
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] mBuffer = new byte[1024 * 1024];
                    int size = mSerialPortPrinter.receiveMessage(mBuffer);
                    byte [] sub = null;
                    if (size > 0) {
                        sub = Arrays.copyOfRange(mBuffer, 0, size);
                    } else {
                        mListener.onError(id, mSerialPortPrinter.getErrorMsg());
                    }
                    mListener.onData(sub);
                } catch (Exception e) {
                    mListener.onError(id, e.getMessage());
                }
            }
        });
        return id;
    }
}
