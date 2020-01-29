package cn.ichi.android.network;

import java.net.InetAddress;

public interface TcpServerListener {
    void onClient(InetAddress client);
    void onData(InetAddress client, byte[] data);
    void onError(int id, InetAddress client, String message);
    void onConnected(int id);
    void onSent(int id);
    void onClosed(int id);
    void onStarted(int id);
    void onStoped(int id);
}
