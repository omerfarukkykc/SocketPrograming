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
        this.startService();
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
            this.sendMessage("ACK" + numMessages);
            numMessages++;
            System.out.println(numMessages + ":" + message);
        } while (!message.equals("***CLOSE***"));
        
    }
    private boolean startService(){
        try {
            link = serverSocket.accept();
            input = new Scanner(link.getInputStream()); //Step 3. 
            output = new PrintWriter(link.getOutputStream(), true); //Step 3.  
            return true;
        } catch (IOException ex) {
            System.err.println("Root.Receiver.startService()");
            return false;
        }
        
    }
    private String getMessage(){
        
        while (true) {            
            if (input.hasNext()) {
            return input.nextLine();
            }
        }
    }
    private void sendMessage(String message){
         output.println(message);
    }

}