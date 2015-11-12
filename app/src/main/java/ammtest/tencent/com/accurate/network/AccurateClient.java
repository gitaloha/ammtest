package ammtest.tencent.com.accurate.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by eureka on 10/25/15.
 */
public class AccurateClient {

    static final String CMD_CASE_START = "case_start";
    static final String CMD_CASE_STOP = "case_stop";
    static final String CMD_CASE_NAME = "case_name";

    static final String CMD_RESPONSE_OK = "parse_cmd_ok";
    static final String CMD_RESPONSE_FAIL = "parse_cmd_fail";
    private static final String TAG = "ammtest.AccurateClient";

    String host = "127.0.0.1";
    int port = 7000;
    private AccurateClient(){
    }

    private static AccurateClient instance = null;
    public  static AccurateClient getInstance(){
        if (null == instance){
            instance = new AccurateClient();
        }
        return instance;
    }

    private String sendCmd(String msg) throws IOException {
        String response = null;
        Socket client;
        try {
            client = new Socket(host , port);
            OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream());
            InputStreamReader reader = new InputStreamReader(client.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("sendcmd:"+msg);
            writer.write(msg.trim()+"\r\n");
            writer.flush();
            response =  br.readLine().trim();
            System.out.println("response:"+response);
            client.close();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    public void startCase(String caseName) throws IOException {
        String response = sendCmd(String.format("%s:%s", CMD_CASE_START, caseName));
        Log.i(TAG, "startCase response:"+response);
    }

    public void stopCase() throws IOException {
        String response = sendCmd(String.format("%s:", CMD_CASE_STOP));
        Log.i(TAG, "stopCase response:" + response);
    }
}
