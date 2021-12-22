package Root;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Root extends Thread{
    protected ServerSocket serverSocket;
    protected int LISTENPORT;
    protected InetAddress host;
    protected synchronized void openListenPort(int PORT){
        this.LISTENPORT = PORT;
        System.out.println("Opening port");
        try {
            host = InetAddress.getLocalHost();
            serverSocket = new ServerSocket(LISTENPORT); //Step 1. 
        } catch (IOException ioEx) {
            System.out.println(
                "Unable to attach to port for receiver!");
            System.exit(1);
        }
    }
    public class Rsender{
        private Scanner sendInput;
        private PrintWriter sendOutput;
        private Socket socket;
        public Rsender(Socket socket){
            this.socket = socket;
            try {
                sendInput = new Scanner(this.socket.getInputStream());
                sendOutput = new PrintWriter(this.socket.getOutputStream(), true);
            } catch (IOException ioEx) {
                System.err.println("Root.Router.startSenderService()");
                System.exit(1);
            }
        }
        public void sendMessage(String message){
            sendOutput.println(message);
        }
        public String getRequest(){

            if (sendInput.hasNext()) {
                return sendInput.nextLine();
            }
            return null;
        }
    }
}