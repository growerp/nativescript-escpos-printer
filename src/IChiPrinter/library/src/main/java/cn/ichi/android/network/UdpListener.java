package cn.ichi.android.network;

import java.net.InetAddress;

public interface UdpListener {
    void onPacket(InetAddress sender, byte[] data);
    void onFinished(int id);
    void onError(int id, String message);
}
