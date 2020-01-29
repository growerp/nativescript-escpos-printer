package cn.ichi.android.bluetooth;

public interface BluetoothClientListener {
    void onData(byte[] data);
    void onError(int id, String message);
    void onConnected(int id);
    void onSent(int id);
    void onClosed(int id);
}
