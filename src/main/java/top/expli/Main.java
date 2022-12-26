package top.expli;

import top.expli.GUI.Login;
import top.expli.webapi.SocketClient;
import top.expli.webapi.WebAdapter;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        WebAdapter.connectToServer();
        Login.main(args);
    }
}