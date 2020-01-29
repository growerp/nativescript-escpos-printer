package cn.ichi.android;

public interface ClientListener {
    void onData(String data);
    void onError(int id, String message);
    void onConnected(int id);
    void onSent(int id);
    void onClosed(int id);
}
