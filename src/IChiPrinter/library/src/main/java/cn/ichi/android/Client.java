package cn.ichi.android;

/**
 * Created by mozj on 2018/2/11.
 */

public interface Client {

    int connect(final String serverName, final int port);

    int close();

    int send(final byte[] data);

    int receive();
}
