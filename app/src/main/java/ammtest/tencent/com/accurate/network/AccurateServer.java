package ammtest.tencent.com.accurate.network;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by eureka on 10/25/15.
 */
public class AccurateServer {
    static private ServerSocket server;
    static final int SERVER_PORT = 7777;

    static void startServer() throws IOException{
        server = new ServerSocket(SERVER_PORT);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {



            }
        };

        new Thread(runnable).start();



    }



}
