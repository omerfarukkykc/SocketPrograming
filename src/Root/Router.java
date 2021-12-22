package Root;
import java.io.*;
import java.net.*;
import java.util.*;

public class Router extends Root{
    private boolean doubleConn = false;
    private Socket listenSocket = null;
    private Socket sendSocket = null;
    
    private Scanner listenInput;
    private PrintWriter listenOutput;
    
    private int CONTROLLERPORT;
    private int SENDPORTT;
    private int SENDPORTF = 0; 
    
    public Router(int LISTENPORT,int[] SENDPORTS) {
        this.openListenPort(LISTENPORT);
        this.SENDPORTT = SENDPORTS[0];
        this.SENDPORTF = SENDPORTS[1];
        this.doubleConn = true;
        this.CONTROLLERPORT = 4444;
        
        
        
    }
    public Router(int LISTENPORT,int SENDPORTT) {
        this.openListenPort(LISTENPORT);
        this.SENDPORTT = SENDPORTT;
        
        
    }

    @Override
    public void run() {
        this.startListenService();
        try {
            handleClient();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } finally {
            try {
                System.out.println(
                    "\n* Closing connections (Router side)*");
                listenSocket.close();
                sendSocket.close();
            } catch (IOException ioEx) {
                System.out.println(
                    "Unable to disconnect!");
                System.exit(1);
            }
        }
    }
    
    private String handleClient() throws IOException {
        String str2 =null ,str3= null;
        String message;
        Rsender sender;
      
        if(this.SENDPORTF != 0){
            if (getRoute()) {
                sender = new Rsender(new Socket(host, this.SENDPORTT));
            }else{
                sender = new Rsender(new Socket(host, this.SENDPORTF));
            }
        }else{
            sender = new Rsender(new Socket(host, this.SENDPORTT));
        }
        
        do {
            message = this.getMessage();
            System.out.println("message from sender " + message);
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(100);
            System.out.println("Generated random number for the packet is: " + randomInt);
            if (randomInt > 19) { //for random probability 20%,each packet has a random number between 0 to 99
                sender.sendMessage(message);
                String str = sender.getRequest();
                System.out.println("message from receiver: " + str);
                this.sendRequest(str);
            } else {
                this.sendRequest(str2);
            }
        } while (!message.equals("***CLOSE***"));
       

        
        return null;
    }
    
    //Bu metodlar alıcıdan mesaj almaya ve alıcıya cevap göndermeye yarar
    private void sendRequest(String message){
        listenOutput.println(message);
    }
    private String getMessage(){
        while (true) {            
            if (listenInput.hasNext()) {
                return listenInput.nextLine();
            }
        }
    }
    
    //Dinleme servisini başlatır 
    private void startListenService(){
        try {
            listenSocket = serverSocket.accept();
            listenInput = new Scanner(listenSocket.getInputStream());
            listenOutput = new PrintWriter(listenSocket.getOutputStream(), true);
            
        } catch (IOException ioEx) {
            System.out.println(
                "Unable to attach to port for router!");
            System.exit(1);
        }
    }
    
    public boolean getRoute(){
        Random randomGenerator = new Random();
        return randomGenerator.nextBoolean();
    }
    
    
   
}
