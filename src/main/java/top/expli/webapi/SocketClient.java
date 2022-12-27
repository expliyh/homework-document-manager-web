package top.expli.webapi;


import top.expli.ConsoleLog;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketClient {
    protected ObjectOutputStream printWriter;
    protected ObjectInputStream bufferedReader;
    private HeartBeat heartBeat;
    private Socket socket;
    private ScheduledExecutorService scheduledExecutorService;
    public Response sendMessage(Request request) throws IOException {
        printWriter.writeObject(request);
        printWriter.flush();
        try {
            ConsoleLog.log(request.toString());
            Response response = (Response) bufferedReader.readObject();
            ConsoleLog.log(response.toString());
            return response;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public SocketClient() {
        try {
            socket = new Socket("127.0.0.1", 11451);
            socket.setSoTimeout(60000);
            printWriter =new ObjectOutputStream(socket.getOutputStream());
            bufferedReader =new ObjectInputStream(socket.getInputStream());
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            heartBeat = new HeartBeat();
            scheduledExecutorService.scheduleWithFixedDelay(heartBeat,0,5, TimeUnit.SECONDS);
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
                printWriter.writeObject(new Request(Request.NameSpace.SYSTEM,Request.Operations.HEARTBEAT));
                bufferedReader.readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        public HeartBeat(){
        }
    }
}
