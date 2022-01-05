package Root;
import Root.Root.Rsender;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Router extends Root{
    private Socket listenSocket = null;
    private Scanner listenInput;
    private PrintWriter listenOutput;
    private int SENDPORTS[] = new int[1];
    public Router(int LISTENPORT,int[] SENDPORTS) {
        this.openListenPort(LISTENPORT);
        this.SENDPORTS = SENDPORTS;
    }
    public Router(int LISTENPORT,int SENDPORTT) {
        this.openListenPort(LISTENPORT);
        this.SENDPORTS[0] = SENDPORTT;
    }
    private void startListenService() throws IOException{
        do {            
            listenSocket = serverSocket.accept();
            listenInput = new Scanner(listenSocket.getInputStream());
            listenOutput = new PrintWriter(listenSocket.getOutputStream(), true);
            Thread CH = new ClientHandler(listenSocket,listenInput,listenOutput,this.SENDPORTS,this.LISTENPORT);
            CH.start();
        } while (true);
    }
    @Override
    public void run() {
        try {
            this.startListenService();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } finally {
            try {
                System.out.println(
                    "\n* Closing connections (Router side)*");
                listenSocket.close();
               
            } catch (IOException ioEx) {
                System.out.println(
                    "Unable to disconnect!");
                System.exit(1);
            }
        }
    }
    
    
    
    // ClientHandler class
    class ClientHandler extends Thread
    {
        final Scanner dis;
        final PrintWriter dos;
        final Socket s;
        private int SENDPORTS[];
        private final int PORT;
        // Constructor
        public ClientHandler(Socket s, Scanner dis, PrintWriter dos ,int[] SENDPORTS,int port)
        {
            this.s = s;
            this.dis = dis;
            this.dos = dos;
            this.SENDPORTS = SENDPORTS;
            this.PORT = port;
        }
        @Override
        public void run()
        {
            try {
                handleClient();
            } catch (IOException ex) {
                Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        private String handleClient() throws IOException {
            String str2 =null ;
            String message;
            Rsender sender;
            String PortName ;
            Random randomGenerator = new Random();
            ArrayList<Rsender> rsender = new ArrayList<>();
            for (int port : this.SENDPORTS){
                rsender.add(new Rsender(new Socket(host, port)));
            }
            
            
            do {
                sender = rsender.get(getRoute(rsender.size()));
                
                
                message = this.getMessage();
                
                int randomInt = randomGenerator.nextInt(100);
                
                if (randomInt > 9) { //for random probability 20%,each packet has a random number between 0 to 99
                    PortName =" "+ getPortName(this.PORT) +" ";
                    
                    System.out.println("Router -"+getPortName(this.PORT)+"- message from sender " + message);
                    sender.sendMessage(PortName+","+message);
                    String str = sender.getRequest();
                    System.out.println("message from receiver: " + str);
                    this.sendRequest(PortName+","+str);
                   
                } else {
                    System.out.println("Package dropped!!! Generated random number for the packet is: " + randomInt);
                    this.sendRequest(str2);
                }
            } while (!message.equals("***CLOSE***"));
            return null;
        }
       
        //Bu metodlar alıcıdan mesaj almaya ve alıcıya cevap göndermeye yarar
        private void sendRequest(String message){
            dos.println(message);
        }
        private String getMessage(){
            while (true) {     
                if(dis.hasNext()){
                    return dis.nextLine();
                }
            }
        }
       
        public boolean getRoute(){
            Random randomGenerator = new Random();
            return randomGenerator.nextBoolean();
        }

        private int getRoute(int length) {
            Random randomgenarator = new Random();
            return randomgenarator.nextInt(length);
        }
        public String getPortName(int PORT){
            return switch(PORT){
                case 1000->"X";
                case 1010->"A";
                case 1333->"B";
                case 1002->"C";
                case 1003->"D";
                case 1004->"E";
                case 1005->"F";
                case 1006->"G";
                case 1007->"H";
                case 1009->"Y";
                default->"";
            };
        }
    }
    
   
}
