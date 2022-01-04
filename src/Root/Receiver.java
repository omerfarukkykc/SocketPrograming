package Root;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Receiver extends Root{
    Socket link = null;
    Scanner input;
    PrintWriter output;    
    public Receiver(int PORT) {
        this.openListenPort(PORT);
    }
    @Override
    public void run() {
        while (true) {            
            try {
                link = serverSocket.accept();
                input = new Scanner(link.getInputStream()); //Step 3. 
                output = new PrintWriter(link.getOutputStream(), true); //Step 3.  
                Thread th = new ClientHandler(link,input,output);
                th.start();
            } catch (IOException ex) {
                System.err.println("Root.Receiver.startService()");
            }
        }
       
    }
    
    
    class ClientHandler extends Thread
    {
        final Scanner dis;
        final PrintWriter dos;
        final Socket s;

        // Constructor
        public ClientHandler(Socket s, Scanner dis, PrintWriter dos)
        {
            this.s = s;
            this.dis = dis;
            this.dos = dos;
        }

        @Override
        public void run()
        {
            try {
                handleRouter();

            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            } finally {
                try {
                    System.out.println("\n* Closing connections (Receiver side)*");
                    link.close();
                } catch (IOException ioEx) {
                    System.out.println("Unable to disconnect!");
                    System.exit(1);
                }
            }
        }
        private void handleRouter() throws IOException {

            int numMessages = 0;
            String message;
            do {   
                message = this.getMessage();
                this.sendMessage("ACK"+ message.substring(message.length() - 1));
                numMessages++;
                System.out.println(" - Y ," +message);
            } while (!message.equals("***CLOSE***"));

        }

        private String getMessage(){

            while (true) {            
                if (dis.hasNext()) {
                return dis.nextLine();
                }
            }
        }
        private void sendMessage(String message){
             dos.println(message);
        }
    }
}
