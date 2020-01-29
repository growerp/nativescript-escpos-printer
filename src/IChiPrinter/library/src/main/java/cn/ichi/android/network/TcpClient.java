package cn.ichi.android.network;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import cn.ichi.android.Client;

public class TcpClient implements Client {
    private TcpClientListener mListener;
    private ExecutorService mExecutor;
    private Socket mSocket;
    private AtomicInteger mId;

    private int mPollSize = 5;

    TcpClient(Socket socket, AtomicInteger id, TcpClientListener listener) {
        mListener = listener;
        mSocket = socket;
        mId = id;
        mExecutor = Executors.newFixedThreadPool(mPollSize);
    }

    public TcpClient(TcpClientListener listener) {
        mListener = listener;
        mExecutor = Executors.newFixedThreadPool(mPollSize);
        mId = new AtomicInteger();
    }

    public Socket getNativeSocket() {
        return mSocket;
    }

    @Override
    public int connect(final String serverName, final int port) {
        final int id = mId.getAndIncrement();
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(serverName, port);
                    mListener.onConnected(id);
                } catch (IOException e) {
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
                    mSocket.close();
                    mListener.onClosed(id);
                } catch (IOException e) {
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
                    mSocket.getOutputStream().write(data);
                    mListener.onSent(id);
                } catch (IOException e) {
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
                    int size = mSocket.getInputStream().read(mBuffer);
                    byte [] sub =null;
                    if (size > 0) {
                        sub = Arrays.copyOfRange(mBuffer, 0, size);
                    }
                    mListener.onData(sub);
                } catch (IOException e) {
                    mListener.onError(id, e.getMessage());
                }
            }
        });
        return id;
    }
}
