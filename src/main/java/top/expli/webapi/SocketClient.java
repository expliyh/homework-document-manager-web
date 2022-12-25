package top.expli.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketClient {
    protected PrintWriter printWriter;
    protected BufferedReader bufferedReader;
    protected ObjectMapper objectMapper;
    private HeartBeat heartBeat;
    private Socket socket;
    private ScheduledExecutorService scheduledExecutorService;
    public Response sendMessage(Request request) throws IOException {
        if (bufferedReader.ready()){
            String aaa = bufferedReader.readLine();
        }
        String requestJson = objectMapper.writeValueAsString(request);
        printWriter.println(requestJson);
        String responseJson = bufferedReader.readLine();
        return objectMapper.readValue(responseJson, Response.class);
    }
    public SocketClient() {
        try {
            socket = new Socket("127.0.0.1", 11451);
            socket.setSoTimeout(60000);
            objectMapper = new ObjectMapper();
            printWriter =new PrintWriter(socket.getOutputStream(),true);
            bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            heartBeat = new HeartBeat();
            scheduledExecutorService.scheduleWithFixedDelay(heartBeat,0,10, TimeUnit.SECONDS);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected class HeartBeat extends Thread{
        @Override
        public void run(){
            try {
                String requestJson = objectMapper.writeValueAsString(new Request(Request.NameSpace.SYSTEM,Request.Operations.HEARTBEAT));
                printWriter.println(requestJson);
            } catch (JsonProcessingException ignored) {
                System.out.println("Heartbeat failed!");
            }
        }
        public HeartBeat(){
        }
    }
}
